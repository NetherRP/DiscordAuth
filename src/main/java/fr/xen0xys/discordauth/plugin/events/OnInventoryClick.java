package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        User user = DiscordAuth.getUsers().get(e.getWhoClicked().getName());
        if(user != null && !user.isLogged()){
            e.setCancelled(true);
        }
    }
}
