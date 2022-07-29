package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        User user = DiscordAuth.getUsers().get(player.getName());
        // Disable move for no-login player
        if(user != null && !user.isLogged()){
            e.getTo();
            if(user.getLoginLocation().getWorld() == e.getTo().getWorld()){
                if(user.getLoginLocation().distance(e.getTo()) >= 3){
                    e.setCancelled(true);
                }
            }else{
                e.setCancelled(true);
            }
        }
    }
}
