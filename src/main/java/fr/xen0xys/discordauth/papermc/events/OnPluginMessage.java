package fr.xen0xys.discordauth.papermc.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.config.language.LangField;
import fr.xen0xys.discordauth.common.network.PacketTuple;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.exceptions.NullPacketException;
import fr.xen0xys.discordauth.common.network.exceptions.NullSenderException;
import fr.xen0xys.discordauth.common.network.exceptions.PacketEncryptionException;
import fr.xen0xys.discordauth.common.network.packets.TargetedResponsePacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.commands.executors.LoginCommand;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class OnPluginMessage implements PluginMessageListener {
    @SuppressWarnings({"NullableProblems", "UnstableApiUsage"})
    @Override
    public void onPluginMessageReceived(@NotNull final String channel, @NotNull final Player _player, @NotNull final byte[] bytes) {
        if(!channel.equals(PluginInfos.CHANNEL)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        SubChannels subChannel = SubChannels.from(input.readUTF());
        if(Objects.isNull(subChannel)) return;
        UUID sender = UUID.fromString(input.readUTF());
        Player player = Bukkit.getPlayer(sender);
        if(Objects.isNull(player)) return;
        try{
            switch (subChannel) {
                case SESSION_RESPONSE -> onSessionResponse(player, input);
                case CONNECTION_RESPONSE -> onConnectionResponse(player, input);
                case SESSION_INVALIDATION_RESPONSE -> onSessionInvalidationResponse(player, input);
                case ACCOUNT_CREATION_RESPONSE -> onAccountCreationResponse(player, input);
                case CHANGE_PASSWORD_RESPONSE -> onChangePasswordResponse(player, input);
                default -> DiscordAuthPlugin.getInstance().getLogger().warning("Unknown sub-channel: " + subChannel);
            }
        } catch (NullPacketException | NullSenderException | PacketEncryptionException ex){
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private <T extends TargetedResponsePacket> PacketTuple<T, Player> getPacketAndPlayer(@NotNull final Class<T> packetClass, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException{
        return this.getPacketAndPlayer(packetClass, input, true);
    }

    private <T extends TargetedResponsePacket> PacketTuple<T, Player> getPacketAndPlayer(@NotNull final Class<T> packetClass, @NotNull final ByteArrayDataInput input, final boolean throwNullTarget) throws NullPacketException, NullSenderException{
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

    private void onSessionResponse(@NotNull final Player player, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException{
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(LangField.SESSION_CHECK.asComponent(tuple.player().getName(), tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(LangField.CONNECTED_SESSION.asComponent().color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(tuple.player().getUniqueId());
        }else{
            tuple.player().sendMessage(LangField.LOGIN_ASK.asComponent().color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(tuple.player());
        }
    }

    private void onConnectionResponse(@NotNull final Player player, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException{
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(LangField.CONNECTION_CHECK.asComponent(tuple.player().getName(), tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(LangField.CONNECTED_LOGIN.asComponent().color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(tuple.player().getUniqueId());
        }else{
            tuple.player().sendMessage(LangField.INVALID_PASSWORD.asComponent().color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(tuple.player());
        }
    }

    private void onSessionInvalidationResponse(@NotNull final Player player, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException{
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(LangField.SESSION_INVALIDATION_CHECK.asComponent(tuple.player().getName(), tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(LangField.DISCONNECTED.asComponent().color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().put(tuple.player().getUniqueId(), tuple.player().getLocation());
            LoginCommand.displayPasswordAsk(tuple.player());
        }else{
            tuple.player().sendMessage(LangField.DISCONNECTING_ERROR.asComponent().color(NamedTextColor.RED));
        }
    }

    private void onAccountCreationResponse(@NotNull final Player player, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException{
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input, false);
        if(tuple.packet().isSuccess())
            player.sendMessage(LangField.ACCOUNT_CREATED.asComponent().color(NamedTextColor.GRAY));
        else
            player.sendMessage(LangField.ACCOUNT_CREATION_ERROR.asComponent().color(NamedTextColor.RED));
    }

    private void onChangePasswordResponse(@NotNull final Player player, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException{
        PacketTuple<TargetedResponsePacket, Player> tuple = this.getPacketAndPlayer(TargetedResponsePacket.class, input);
        if(!player.equals(tuple.player()))
            player.sendMessage(LangField.PASSWORD_CHANGE_CHECK.asComponent(tuple.player().getName(), tuple.packet().isSuccess()).color(NamedTextColor.GRAY));
        if(tuple.packet().isSuccess()){
            tuple.player().sendMessage(LangField.PASSWORD_CHANGED.asComponent().color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().put(tuple.player().getUniqueId(), tuple.player().getLocation());
            LoginCommand.displayPasswordAsk(tuple.player());
        }else
            tuple.player().sendMessage(LangField.PASSWORD_CHANGING_ERROR.asComponent().color(NamedTextColor.RED));
    }
}
