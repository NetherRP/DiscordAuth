package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth_old.models.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnAsyncPlayerChat implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
        if(!e.isCancelled() && !DiscordAuthOld.getConfiguration().isOnlySafety()){
            String author = e.getPlayer().getName();
            User user = DiscordAuthOld.getUsers().get(author);
            if(user != null && !user.isLogged()){
                e.getPlayer().sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().needLogin);
                e.setCancelled(true);
                return;
            }
            if(DiscordAuthOld.getConfiguration().getEnableSharedChat()){
                String message = String.format("<%s> %s", author, e.getMessage());
                BotUtils.sendMessage(message);
            }
        }
    }
}
