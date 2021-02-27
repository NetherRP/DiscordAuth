package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnPlayerRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        if(player.getBedSpawnLocation() == null){
            e.setRespawnLocation(DiscordAuth.getSpawnLocation());
        }else{
            System.out.println(player.getBedSpawnLocation());
        }
    }
}
