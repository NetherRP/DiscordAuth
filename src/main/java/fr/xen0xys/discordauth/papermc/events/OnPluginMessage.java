package fr.xen0xys.discordauth.papermc.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.exceptions.NullPacketException;
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

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
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

    private void onSessionResponse(Player player, ByteArrayDataInput input){
        TargetedResponsePacket packet = ServerPacket.decryptServer(TargetedResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) throw new NullPacketException("SessionResponsePacket is null");
        Player target = Bukkit.getPlayer(packet.getTarget());
        if(Objects.isNull(target)) return;
        if(!player.equals(target))
            player.sendMessage(Component.text("Session check for " + target.getName() + ": " + packet.isSuccess()).color(NamedTextColor.GRAY));
        if(packet.isSuccess()){
            target.sendMessage(Component.text("You are connected (session)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(target.getUniqueId());
        }else{
            target.sendMessage(Component.text("Please login yourself").color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(target);
        }
    }

    private void onConnectionResponse(Player player, ByteArrayDataInput input){
        TargetedResponsePacket packet = ServerPacket.decryptServer(TargetedResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) throw new NullPacketException("ConnectionResponsePacket is null");
        Player target = Bukkit.getPlayer(packet.getTarget());
        if(Objects.isNull(target)) return;
        if(!player.equals(target))
            player.sendMessage(Component.text("Connection check for " + target.getName() + ": " + packet.isSuccess()).color(NamedTextColor.GRAY));
        if(packet.isSuccess()){
            target.sendMessage(Component.text("You are connected (connection)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(target.getUniqueId());
        }else{
            target.sendMessage(Component.text("Invalid password, please login yourself").color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(target);
        }
    }

    private void onSessionInvalidationResponse(Player player, ByteArrayDataInput input){
        TargetedResponsePacket packet = ServerPacket.decryptServer(TargetedResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) throw new NullPacketException("SessionInvalidationResponsePacket is null");
        Player target = Bukkit.getPlayer(packet.getTarget());
        if(Objects.isNull(target)) return;
        if(!player.equals(target))
            player.sendMessage(Component.text("Session invalidation for " + target.getName() + ": " + packet.isSuccess()).color(NamedTextColor.GRAY));
        if(packet.isSuccess()){
            target.sendMessage(Component.text("You are disconnected!").color(NamedTextColor.RED));
            DiscordAuthPlugin.getUnauthenticatedPlayers().put(target.getUniqueId(), target.getLocation());
            LoginCommand.displayPasswordAsk(target);
        }else{
            target.sendMessage(Component.text("Error when disconnecting!").color(NamedTextColor.RED));
        }
    }

    private void onAccountCreationResponse(Player player, ByteArrayDataInput input){
        TargetedResponsePacket packet = ServerPacket.decryptServer(TargetedResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) throw new NullPacketException("AccountCreationResponsePacket is null");
        if(packet.isSuccess())
            player.sendMessage(Component.text("Account created!").color(NamedTextColor.GREEN));
        else
            player.sendMessage(Component.text("Error when creating account!").color(NamedTextColor.RED));
    }

    private void onChangePasswordResponse(Player player, ByteArrayDataInput input){
        TargetedResponsePacket packet = ServerPacket.decryptServer(TargetedResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) throw new NullPacketException("ChangePasswordResponsePacket is null");
        Player target = Bukkit.getPlayer(packet.getTarget());
        if(Objects.isNull(target)) return;
        if(!player.equals(target))
            player.sendMessage(Component.text("Password change for " + target.getName() + ": " + packet.isSuccess()).color(NamedTextColor.GRAY));
        if(packet.isSuccess()){
            target.sendMessage(Component.text("Password changed! Please login again").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().put(target.getUniqueId(), target.getLocation());
            LoginCommand.displayPasswordAsk(target);
        }
        else
            target.sendMessage(Component.text("Error when changing password!").color(NamedTextColor.RED));
    }
}
