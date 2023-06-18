package fr.xen0xys.discordauth.waterfall.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.*;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import fr.xen0xys.discordauth.waterfall.network.ProxyPacket;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class OnPluginMessage implements Listener {
    @EventHandler
    public void onPluginMessage(PluginMessageEvent e){
        if(!e.getTag().equals(PluginInfos.CHANNEL)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());
        SubChannels subChannel = SubChannels.from(input.readUTF());
        if(Objects.isNull(subChannel)) return;
        switch (subChannel) {
            case SESSION_ASK -> onSessionAsk(e.getReceiver(), input);
            case CONNECTION_ASK -> onConnectionAsk(e.getReceiver(), input);
            case SESSION_INVALIDATION_ASK -> onSessionInvalidationAsk(e.getReceiver(), input);
            case ACCOUNT_CREATION_ASK -> onAccountCreationAsk(e.getReceiver(), input);
            default -> DiscordAuthProxy.getInstance().getLogger().warning("Unknown sub-channel: " + subChannel);
        }
    }

    private void onSessionAsk(Connection connection, ByteArrayDataInput input){
        SessionAskPacket packet = ProxyPacket.decryptProxy(SessionAskPacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        ProxiedPlayer player = DiscordAuthProxy.getInstance().getProxy().getPlayer(connection.toString());
        if (Objects.isNull(player)) return;
        SessionResponsePacket outPacket = new SessionResponsePacket(DiscordAuthProxy.getSessions().contains(player.getUniqueId()));
        ProxyPacket.sendProxy(player, SubChannels.SESSION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent session response for " + player.getName());
    }

    private void onConnectionAsk(Connection connection, ByteArrayDataInput input){
        ConnectionAskPacket packet = ProxyPacket.decryptProxy(ConnectionAskPacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        ProxiedPlayer player = DiscordAuthProxy.getInstance().getProxy().getPlayer(connection.toString());
        if (Objects.isNull(player)) return;
        Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(player.getUniqueId());
        boolean state = new Encryption(DiscordAuthProxy.getInstance().getLogger()).compareHash(packet.getPassword(), account.getPassword());
        if(state){
            account.setLastConnection(System.currentTimeMillis());
            account.setLastIp(player.getSocketAddress().toString(), DiscordAuthProxy.getInstance().getLogger());
            DiscordAuthProxy.getDatabaseHandler().updateAccount(account);
        }
        ConnectionResponsePacket outPacket = new ConnectionResponsePacket(state);
        ProxyPacket.sendProxy(player, SubChannels.CONNECTION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent connection response for " + player.getName());
    }

    private void onSessionInvalidationAsk(Connection connection, ByteArrayDataInput input){
        SessionInvalidationAskPacket packet = ProxyPacket.decryptProxy(SessionInvalidationAskPacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        ProxiedPlayer player = DiscordAuthProxy.getInstance().getProxy().getPlayer(connection.toString());
        if (Objects.isNull(player)) return;
        Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(player.getUniqueId());
        account.setLastConnection(0);
        DiscordAuthProxy.getDatabaseHandler().updateAccount(account);
        DiscordAuthProxy.getSessions().remove(player.getUniqueId());
        SessionInvalidationResponsePacket outPacket = new SessionInvalidationResponsePacket(true);
        ProxyPacket.sendProxy(player, SubChannels.SESSION_INVALIDATION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent session invalidation response for " + player.getName());
    }

    private void onAccountCreationAsk(Connection connection, ByteArrayDataInput input){
        AccountCreationAskPacket packet = ProxyPacket.decryptProxy(AccountCreationAskPacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        ProxiedPlayer player = DiscordAuthProxy.getInstance().getProxy().getPlayer(connection.toString());
        if (Objects.isNull(player)) return;
        Account account = new Account(packet.getDiscordId(), null, packet.getUsername(), packet.getPassword(), null, -1);
        boolean state = DiscordAuthProxy.getDatabaseHandler().addAccount(account);
        AccountCreationResponsePacket outPacket = new AccountCreationResponsePacket(state);
        ProxyPacket.sendProxy(player, SubChannels.ACCOUNT_CREATION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent account creation response for " + player.getName());
    }
}
