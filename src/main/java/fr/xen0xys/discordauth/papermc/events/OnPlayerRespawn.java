package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnPlayerRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        // If respawn function is enable
        if(DiscordAuthPlugin.getServerConfig().isRespawnEnable()){
            // Override bed respawn if it is enabled in configuration
            if(DiscordAuthPlugin.getServerConfig().isRespawnEvenWithBed()){
                e.setRespawnLocation(DiscordAuthPlugin.getServerConfig().getSpawnPoint());
                // Only change respawn point if player don't have bed location!
            }else{
                if(player.getBedSpawnLocation() == null){
                    e.setRespawnLocation(DiscordAuthPlugin.getServerConfig().getSpawnPoint());
                }
            }
        }
    }
}
