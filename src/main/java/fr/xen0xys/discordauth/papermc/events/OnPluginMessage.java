package fr.xen0xys.discordauth.papermc.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.AccountCreationResponsePacket;
import fr.xen0xys.discordauth.common.network.packets.ConnectionResponsePacket;
import fr.xen0xys.discordauth.common.network.packets.SessionInvalidationResponsePacket;
import fr.xen0xys.discordauth.common.network.packets.SessionResponsePacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.commands.executors.LoginCommand;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        switch (subChannel) {
            case SESSION_RESPONSE -> onSessionResponse(player, input);
            case CONNECTION_RESPONSE -> onConnectionResponse(player, input);
            case SESSION_INVALIDATION_RESPONSE -> onSessionInvalidationResponse(player, input);
            case ACCOUNT_CREATION_RESPONSE -> onAccountCreationResponse(player, input);
            default -> DiscordAuthPlugin.getInstance().getLogger().warning("Unknown sub-channel: " + subChannel);
        }
    }

    private void onSessionResponse(Player player, ByteArrayDataInput input){
        SessionResponsePacket packet = ServerPacket.decryptServer(SessionResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        if(packet.hasSession()){
            player.sendMessage(Component.text("You are connected (session)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(player.getUniqueId());
        }else{
            player.sendMessage(Component.text("Please login yourself").color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(player);
        }
    }

    private void onConnectionResponse(Player player, ByteArrayDataInput input){
        ConnectionResponsePacket packet = ServerPacket.decryptServer(ConnectionResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        if(packet.isConnected()){
            player.sendMessage(Component.text("You are connected (connection)!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().remove(player.getUniqueId());
        }else{
            player.sendMessage(Component.text("Invalid password, please login yourself").color(NamedTextColor.RED));
            LoginCommand.displayPasswordAsk(player);
        }
    }

    private void onSessionInvalidationResponse(Player player, ByteArrayDataInput input){
        SessionInvalidationResponsePacket packet = ServerPacket.decryptServer(SessionInvalidationResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        if(packet.isSuccess()){
            player.sendMessage(Component.text("You are disconnected!").color(NamedTextColor.GREEN));
            DiscordAuthPlugin.getUnauthenticatedPlayers().put(player.getUniqueId(), player.getLocation());
        }else{
            player.sendMessage(Component.text("Error when disconnecting!").color(NamedTextColor.RED));
        }
    }

    private void onAccountCreationResponse(Player player, ByteArrayDataInput input){
        AccountCreationResponsePacket packet = ServerPacket.decryptServer(AccountCreationResponsePacket.class, input.readUTF());
        if(Objects.isNull(packet)) return;
        if(packet.isSuccess())
            player.sendMessage(Component.text("Account created!").color(NamedTextColor.GREEN));
        else
            player.sendMessage(Component.text("Error when creating account!").color(NamedTextColor.RED));
    }
}
