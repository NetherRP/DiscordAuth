package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth_old.models.User;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnEntityDamaged implements Listener {
    @EventHandler
    public void onEntityDamaged(EntityDamageEvent e){
        if(e.getEntityType() == EntityType.PLAYER){
            User user = DiscordAuthOld.getUsers().get(e.getEntity().getName());
            if(user != null && !user.isLogged()){
                e.setCancelled(true);
            }
        }
    }
}
