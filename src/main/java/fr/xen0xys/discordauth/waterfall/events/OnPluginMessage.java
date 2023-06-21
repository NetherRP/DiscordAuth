package fr.xen0xys.discordauth.waterfall.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.Packet;
import fr.xen0xys.discordauth.common.network.PacketTuple;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.exceptions.NullPacketException;
import fr.xen0xys.discordauth.common.network.exceptions.NullSenderException;
import fr.xen0xys.discordauth.common.network.exceptions.PacketEncryptionException;
import fr.xen0xys.discordauth.common.network.packets.*;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import fr.xen0xys.discordauth.waterfall.network.ProxyPacket;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class OnPluginMessage implements Listener {
    @EventHandler
    public void onPluginMessage(@NotNull final PluginMessageEvent e){
        if(!e.getTag().equals(PluginInfos.CHANNEL)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());
        SubChannels subChannel = SubChannels.from(input.readUTF());
        if(Objects.isNull(subChannel)) return;
        Connection connection = e.getReceiver();
        try{
            switch (subChannel) {
                case SESSION_ASK -> onSessionAsk(connection, input);
                case CONNECTION_ASK -> onConnectionAsk(connection, input);
                case SESSION_INVALIDATION_ASK -> onSessionInvalidationAsk(connection, input);
                case ACCOUNT_CREATION_ASK -> onAccountCreationAsk(connection, input);
                case CHANGE_PASSWORD_ASK -> onChangePasswordAsk(connection, input);
                default -> DiscordAuthProxy.getInstance().getLogger().warning("Unknown sub-channel: " + subChannel);
            }
        }catch (NullPacketException | NullSenderException | PacketEncryptionException ex){
            ex.printStackTrace();
        }
    }

    private <T extends Packet> PacketTuple<T, ProxiedPlayer> getPacketAndPlayer(@NotNull final Class<T> packetClass, @NotNull final Connection connection, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException{
        T packet = ProxyPacket.decryptProxy(packetClass, input.readUTF());
        if(Objects.isNull(packet)) throw new NullPacketException("ChangePasswordAskPacket is null");
        ProxiedPlayer player = (ProxiedPlayer) connection;
//        if (Objects.isNull(player)) throw new NullSenderException("Null sender for ChangePasswordAskPacket");
        return new PacketTuple<>(packet, player);
    }

    private void onSessionAsk(@NotNull final Connection connection, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException {
        PacketTuple<SessionAskPacket, ProxiedPlayer> tuple = this.getPacketAndPlayer(SessionAskPacket.class, connection, input);
        DiscordAuthProxy.getInstance().getLogger().info(DiscordAuthProxy.getSessions().toString());
        TargetedResponsePacket outPacket = new TargetedResponsePacket(tuple.packet().getTarget(), DiscordAuthProxy.getSessions().contains(tuple.packet().getTarget()));
        ProxyPacket.sendProxy(tuple.player(), SubChannels.SESSION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent session response for %s: %s".formatted(tuple.player().getName(), outPacket.isSuccess()));
    }

    private void onConnectionAsk(@NotNull final Connection connection, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException {
        PacketTuple<ConnectionAskPacket, ProxiedPlayer> tuple = this.getPacketAndPlayer(ConnectionAskPacket.class, connection, input);
        Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(tuple.player().getUniqueId());
        boolean state = new Encryption().compareHash(tuple.packet().getPassword(), account.getPassword());
        if(state){
            account.setLastConnection(System.currentTimeMillis());
            account.setLastIp(tuple.player().getSocketAddress().toString());
            DiscordAuthProxy.getDatabaseHandler().updateAccount(account);
            DiscordAuthProxy.getSessions().add(tuple.player().getUniqueId());
        }
        TargetedResponsePacket outPacket = new TargetedResponsePacket(account.getUuid(), state);
        ProxyPacket.sendProxy(tuple.player(), SubChannels.CONNECTION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent connection response for " + tuple.player().getName());
    }

    private void onSessionInvalidationAsk(@NotNull final Connection connection, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException {
        PacketTuple<SessionInvalidationAskPacket, ProxiedPlayer> tuple = this.getPacketAndPlayer(SessionInvalidationAskPacket.class, connection, input);
        Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(tuple.player().getUniqueId());
        account.setLastConnection(0);
        DiscordAuthProxy.getDatabaseHandler().updateAccount(account);
        DiscordAuthProxy.getSessions().remove(tuple.player().getUniqueId());
        TargetedResponsePacket outPacket = new TargetedResponsePacket(account.getUuid(), true);
        ProxyPacket.sendProxy(tuple.player(), SubChannels.SESSION_INVALIDATION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent session invalidation response for " + tuple.player().getName());
    }

    private void onAccountCreationAsk(@NotNull final Connection connection, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException {
        PacketTuple<AccountCreationAskPacket, ProxiedPlayer> tuple = this.getPacketAndPlayer(AccountCreationAskPacket.class, connection, input);
        Account account = new Account(tuple.packet().getDiscordId(), null, tuple.packet().getUsername(), tuple.packet().getPassword(), null, -1);
        boolean state = DiscordAuthProxy.getDatabaseHandler().addAccount(account);
        TargetedResponsePacket outPacket = new TargetedResponsePacket(account.getUuid(), state);
        ProxyPacket.sendProxy(tuple.player(), SubChannels.ACCOUNT_CREATION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent account creation response for " + tuple.player().getName());
    }

    private void onChangePasswordAsk(@NotNull final Connection connection, @NotNull final ByteArrayDataInput input) throws NullPacketException, NullSenderException, PacketEncryptionException {
        PacketTuple<ChangePasswordAskPacket, ProxiedPlayer> tuple = this.getPacketAndPlayer(ChangePasswordAskPacket.class, connection, input);
        Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(tuple.player().getUniqueId());
        account.setPassword(tuple.packet().getNewPassword());
        DiscordAuthProxy.getDatabaseHandler().updateAccount(account);
        TargetedResponsePacket outPacket = new TargetedResponsePacket(account.getUuid(), true);
        ProxyPacket.sendProxy(tuple.player(), SubChannels.CHANGE_PASSWORD_RESPONSE, outPacket);
    }
}
