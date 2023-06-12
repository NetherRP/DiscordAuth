package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(!DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId()))
            return;
        Location loginLocation = DiscordAuthPlugin.getUnauthenticatedPlayers().get(player.getUniqueId());
        if (loginLocation.getWorld() == e.getTo().getWorld()) {
            if (loginLocation.distance(e.getTo()) >= 3)
                e.setCancelled(true);
        }else{
            e.setCancelled(true);
        }
    }
}
