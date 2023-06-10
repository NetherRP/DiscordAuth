package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.User;
import fr.xen0xys.discordauth_old.DiscordAuthOld;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OnAsyncChat implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncChatEvent e){
        if(!e.isCancelled() && !DiscordAuth.getConfiguration().isOnlySafety()){
            String author = e.getPlayer().getName();
            User user = DiscordAuthOld.getUsers().get(author);
            if(user != null && !user.isLogged()){
                e.getPlayer().sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().needLogin);
                e.setCancelled(true);
            }
        }
    }
}
