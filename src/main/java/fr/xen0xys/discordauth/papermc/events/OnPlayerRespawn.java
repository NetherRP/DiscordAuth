package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OnPlayerRespawn implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(@NotNull final PlayerRespawnEvent e){
        Player player = e.getPlayer();
        if(DiscordAuthPlugin.getServerConfig().isRespawnEnable())
            if(DiscordAuthPlugin.getServerConfig().isRespawnEvenWithBed())
                e.setRespawnLocation(DiscordAuthPlugin.getServerConfig().getSpawnPoint());
            else
                if(Objects.isNull(player.getBedSpawnLocation()))
                    e.setRespawnLocation(DiscordAuthPlugin.getServerConfig().getSpawnPoint());
    }
}
