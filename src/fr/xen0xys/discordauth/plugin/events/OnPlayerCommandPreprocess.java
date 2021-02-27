package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreprocess implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
        String command_name = e.getMessage();
        Player player = e.getPlayer();
        if(!command_name.startsWith("/login") && !command_name.startsWith("/l")){
            if(!DiscordAuth.getUsers().get(player.getName()).isLogged()){
                player.sendMessage(ChatColor.RED + "Vous devez vous identifier pour faire cela!");
                e.setCancelled(true);
                // return;
            }
        }
    }
}
