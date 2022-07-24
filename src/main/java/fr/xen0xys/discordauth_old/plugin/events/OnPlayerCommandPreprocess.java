package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
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
            if(!DiscordAuthOld.getUsers().get(player.getName()).isLogged()){
                player.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().needLogin);
                e.setCancelled(true);
            }
        }
    }
}
