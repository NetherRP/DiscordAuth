package fr.xen0xys.discordauth.papermc.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.network.PacketTuple;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.exceptions.NullPacketException;
import fr.xen0xys.discordauth.common.network.exceptions.NullSenderException;
import fr.xen0xys.discordauth.common.network.packets.*;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.commands.executors.LoginCommand;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems", "SameParameterValue"})
public class OnPluginMessage implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        if(!channel.equals(PluginInfos.CHANNEL)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        SubChannels subChannel = SubChannels.from(input.readUTF());
        if(Objects.isNull(subChannel)) return;
        try{
            switch (subChannel) {
                case SESSION_RESPONSE -> onSessionResponse(player, input);
                case CONNECTION_RESPONSE -> onConnectionResponse(player, input);
                case SESSION_INVALIDATION_RESPONSE -> onSessionInvalidationResponse(player, input);
                case ACCOUNT_CREATION_RESPONSE -> onAccountCreationResponse(player, input);
                case CHANGE_PASSWORD_RESPONSE -> onChangePasswordResponse(player, input);
                default -> DiscordAuthPlugin.getInstance().getLogger().warning("Unknown sub-channel: " + subChannel);
            }
        } catch (NullPacketException ex){
            ex.printStackTrace();
        }
    }

    private <T extends TargetedResponsePacket> PacketTuple<T, Player> getPacketAndPlayer(Class<T> packetClass, ByteArrayDataInput input){
        return this.getPacketAndPlayer(packetClass, input, true);
    }

    private <T extends TargetedResponsePacket> PacketTuple<T, Player> getPacketAndPlayer(Class<T> packetClass, ByteArrayDataInput input, boolean throwNullTarget){
        T packet = ServerPacket.decryptServer(packetClass, input.readUTF());
        if(Objects.isNull(packet)) throw new NullPacketException("ChangePasswordAskPacket is null");
        Player player = null;
        if(Objects.isNull(packet.getTarget())){
            if(throwNullTarget)
                throw new NullSenderException("Target is null");
        }else{
            player = Bukkit.getPlayer(packet.getTarget());
            if (Objects.isNull(player)) throw new NullSenderException("Player not found for uuid: " + packet.getTarget().toString());
        }
        return new PacketTuple<>(packet, player);
    }

    private void onSessionResponse(Player player, ByteArrayDataInput input){
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(Component.text("Session check for " + tuple.player().getName() + ": " + tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(Component.text("You are connected (session)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(tuple.player().getUniqueId());
        }else{
            tuple.player().sendMessage(Component.text("Please login yourself").color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(tuple.player());
        }
    }

    private void onConnectionResponse(Player player, ByteArrayDataInput input){
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(Component.text("Connection check for " + tuple.player().getName() + ": " + tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(Component.text("You are connected (connection)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(tuple.player().getUniqueId());
        }else{
            tuple.player().sendMessage(Component.text("Invalid password, please login yourself").color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(tuple.player());
        }
    }

    private void onSessionInvalidationResponse(Player player, ByteArrayDataInput input){
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(Component.text("Session invalidation for " + tuple.player().getName() + ": " + tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(Component.text("You are disconnected!").color(NamedTextColor.RED));
            DiscordAuthPlugin.getUnauthenticatedPlayers().put(tuple.player().getUniqueId(), tuple.player().getLocation());
            LoginCommand.displayPasswordAsk(tuple.player());
        }else{
            tuple.player().sendMessage(Component.text("Error when disconnecting!").color(NamedTextColor.RED));
        }
    }

    private void onAccountCreationResponse(Player player, ByteArrayDataInput input){
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input, false);
        if(tuple.packet().isSuccess())
            player.sendMessage(Component.text("Account created!").color(NamedTextColor.GREEN));
        else
            player.sendMessage(Component.text("Error when creating account!").color(NamedTextColor.RED));
    }

    private void onChangePasswordResponse(Player player, ByteArrayDataInput input){
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(Component.text("Password change for " + tuple.player().getName() + ": " + tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(Component.text("Password changed! Please login again").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().put(tuple.player().getUniqueId(), tuple.player().getLocation());
            LoginCommand.displayPasswordAsk(tuple.player());
        }
        else
            tuple.player().sendMessage(Component.text("Error when changing password!").color(NamedTextColor.RED));
    }
}
