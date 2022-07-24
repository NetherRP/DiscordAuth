package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth_old.models.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        User user = DiscordAuthOld.getUsers().get(player.getName());
        // Disable move for no-login player
        if(user != null && !user.isLogged()){
            if(e.getTo() != null){
                if(user.getLoginLocation().getWorld() == e.getTo().getWorld()){
                    if(user.getLoginLocation().distance(e.getTo()) >= 3){
                        e.setCancelled(true);
                    }
                }else{
                    e.setCancelled(true);
                }
            }else{
                e.setCancelled(true);
            }
        }
    }
}
