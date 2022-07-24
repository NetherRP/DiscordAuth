package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class OnPlayerKick implements Listener {
    @EventHandler
    public void onPlayerKick(PlayerKickEvent e){
        Player player = e.getPlayer();
        if(e.getReason().equals("Flying is not enabled on this server") && !DiscordAuthOld.getUsers().get(player.getName()).isLogged()){
            e.setCancelled(true);
        }
    }
}
