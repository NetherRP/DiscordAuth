package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnEntityDamagedByEntity implements Listener {
    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
        // No-logged user can't hit entities
        if(e.getDamager() instanceof Player){
            User user = DiscordAuth.getUsers().get(e.getDamager().getName());
            if(user != null && !user.isLogged()){
                e.setCancelled(true);
            }
        }
        // No-logged user can't take damages
        if(e.getEntityType() == EntityType.PLAYER){
            User user = DiscordAuth.getUsers().get(e.getEntity().getName());
            if(user != null && !user.isLogged()){
                e.setCancelled(true);
            }
        }
    }
}
