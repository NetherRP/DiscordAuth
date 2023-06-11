package fr.xen0xys.discordauth.papermc.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.GsonUtils;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.SessionAskPacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendSessionAsk(e.getPlayer());
                DiscordAuthPlugin.getInstance().getLogger().info("Session ask sent for " + e.getPlayer().getName());
            }
        }.runTaskLaterAsynchronously(DiscordAuthPlugin.getInstance(), 10L);
    }

    private void sendSessionAsk(@NotNull final Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(SubChannels.SESSION_ASK.getName());
        output.writeUTF(GsonUtils.getGson().toJson(new SessionAskPacket(player.getUniqueId())));
        player.sendPluginMessage(DiscordAuthPlugin.getInstance(), PluginInfos.CHANNEL, output.toByteArray());
    }
}
