package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth_old.models.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class OnPlayerDropItem implements Listener {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        User user = DiscordAuthOld.getUsers().get(e.getPlayer().getName());
        if(user != null && !user.isLogged()){
            e.setCancelled(true);
        }
    }
}
