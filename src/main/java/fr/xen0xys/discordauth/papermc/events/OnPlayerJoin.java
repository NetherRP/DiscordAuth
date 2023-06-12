package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.SessionAskPacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        DiscordAuthPlugin.getUnauthenticatedPlayers().put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                ServerPacket.sendServer(e.getPlayer(), SubChannels.SESSION_ASK, new SessionAskPacket(e.getPlayer().getUniqueId()));
                DiscordAuthPlugin.getInstance().getLogger().info("Session ask sent for " + e.getPlayer().getName());
            }
        }.runTaskLaterAsynchronously(DiscordAuthPlugin.getInstance(), 10L);
    }
}
