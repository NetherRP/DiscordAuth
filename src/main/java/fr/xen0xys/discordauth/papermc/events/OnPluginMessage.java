package fr.xen0xys.discordauth.papermc.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.ConnectionAskPacket;
import fr.xen0xys.discordauth.common.network.packets.ConnectionResponsePacket;
import fr.xen0xys.discordauth.common.network.packets.SessionResponsePacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class OnPluginMessage implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        if(!channel.equals(PluginInfos.CHANNEL)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        SubChannels subChannel = SubChannels.from(input.readUTF());
        if(Objects.isNull(subChannel)) return;
        switch (subChannel) {
            case SESSION_RESPONSE -> onSessionResponse(player, input);
            case CONNECTION_RESPONSE -> onConnectionResponse(player, input);
            default -> DiscordAuthPlugin.getInstance().getLogger().warning("Unknown sub-channel: " + subChannel);
        }
    }

    private void onSessionResponse(Player player, ByteArrayDataInput input){
        SessionResponsePacket packet = ServerPacket.decryptServer(SessionResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        if(packet.hasSession()){
            player.sendMessage(Component.text("You are connected (session)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getConnectedPlayers().add(player);
        }else{
            player.sendMessage(Component.text("Please login yourself").color(NamedTextColor.RED));
            displayPasswordAsk(player);
        }
    }

    private void onConnectionResponse(Player player, ByteArrayDataInput input){
        ConnectionResponsePacket packet = ServerPacket.decryptServer(ConnectionResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        if(packet.isConnected()){
            player.sendMessage(Component.text("You are connected (connection)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getConnectedPlayers().add(player);
        }else{
            player.sendMessage(Component.text("Invalid password, please login yourself").color(NamedTextColor.RED));
            displayPasswordAsk(player);
        }
    }

    private void displayPasswordAsk(Player player){
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder.plugin(DiscordAuthPlugin.getInstance());
        builder.title("Password :");
        builder.text("Enter your password");
        builder.onClick((slot, stateSnapshot) -> {
            ConnectionAskPacket packet = new ConnectionAskPacket(player.getUniqueId(), stateSnapshot.getText());
            ServerPacket.sendServer(player, SubChannels.CONNECTION_ASK, packet);
            return List.of(AnvilGUI.ResponseAction.close());
        });
        builder.open(player);
    }
}
