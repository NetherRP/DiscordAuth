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
        String commandName = e.getMessage();
        Player player = e.getPlayer();
        if(!commandName.startsWith("/login") && !commandName.startsWith("/l")){
            if(!DiscordAuth.getUsers().get(player.getName()).isLogged()){
                player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().needLogin);
                e.setCancelled(true);
            }
        }
    }
}
