package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth_old.models.User;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class OnFoodLevelChange implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        if(e.getEntityType() == EntityType.PLAYER){
            User user = DiscordAuthOld.getUsers().get(e.getEntity().getName());
            if(user != null && !user.isLogged()){
                e.setCancelled(true);
            }
        }
    }
}
