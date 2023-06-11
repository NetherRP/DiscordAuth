package fr.xen0xys.discordauth.waterfall.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.ConnectionAskPacket;
import fr.xen0xys.discordauth.common.network.packets.ConnectionResponsePacket;
import fr.xen0xys.discordauth.common.network.packets.SessionAskPacket;
import fr.xen0xys.discordauth.common.network.packets.SessionResponsePacket;
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
            default -> DiscordAuthProxy.getInstance().getLogger().warning("Unknown sub-channel: " + subChannel);
        }
    }

    private void onSessionAsk(Connection connection, ByteArrayDataInput input){
        SessionAskPacket packet = ProxyPacket.decryptProxy(SessionAskPacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        ProxiedPlayer player = DiscordAuthProxy.getInstance().getProxy().getPlayer(connection.toString());
        if (Objects.isNull(player)) return;
        SessionResponsePacket outPacket = new SessionResponsePacket(false); // TODO
        ProxyPacket.sendProxy(player, SubChannels.SESSION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent session response for " + player.getName());
    }

    private void onConnectionAsk(Connection connection, ByteArrayDataInput input){
        ConnectionAskPacket packet = ProxyPacket.decryptProxy(ConnectionAskPacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        ProxiedPlayer player = DiscordAuthProxy.getInstance().getProxy().getPlayer(connection.toString());
        if (Objects.isNull(player)) return;
        // TODO: temporary
        boolean state = new Encryption(DiscordAuthProxy.getInstance().getLogger()).compareHash("passwd", packet.getPassword());
        ConnectionResponsePacket outPacket = new ConnectionResponsePacket(state);
        ProxyPacket.sendProxy(player, SubChannels.CONNECTION_RESPONSE, outPacket);
        DiscordAuthProxy.getInstance().getLogger().info("Sent connection response for " + player.getName());
    }
}
