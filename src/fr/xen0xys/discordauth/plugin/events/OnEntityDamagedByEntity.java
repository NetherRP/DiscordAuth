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

        // Check if ban needed
        /*
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player damager = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();
            if(victim.getHealth() - e.getFinalDamage() <= 0){
                new TempBanDatabase().banPlayer(damager.getName(), victim.getName());
                damager.kickPlayer(ChatColor.DARK_AQUA + "Vous avez tué un joueur, vous etes banni 3min sauf si le joueur vous pardonne ou vous rallonge votre peine de 2min");
            }
        }
        */
    }
}
