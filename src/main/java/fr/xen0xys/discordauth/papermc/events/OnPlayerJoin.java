package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.common.config.language.LangField;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.SessionAskPacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OnPlayerJoin implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(@NotNull final PlayerJoinEvent e) {
        if(DiscordAuthPlugin.getServerConfig().isTpOnLogin() || (e.getPlayer().hasPlayedBefore() && DiscordAuthPlugin.getServerConfig().isFirstTimeTp())){
            Location spawn = DiscordAuthPlugin.getServerConfig().getSpawnPoint();
            if(Objects.nonNull(spawn))
                e.getPlayer().teleport(spawn);
            else
                e.getPlayer().sendMessage(LangField.CONFIG_NULL_LOCATION.asComponent());
        }
        DiscordAuthPlugin.getUnauthenticatedPlayers().put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                ServerPacket.sendServer(e.getPlayer(), SubChannels.SESSION_ASK, new SessionAskPacket(e.getPlayer().getUniqueId()));
                DiscordAuthPlugin.getInstance().getLogger().info("Session ask sent for %s".formatted(e.getPlayer().getName()));
            }
        }.runTaskLaterAsynchronously(DiscordAuthPlugin.getInstance(), 10L);
    }
}
