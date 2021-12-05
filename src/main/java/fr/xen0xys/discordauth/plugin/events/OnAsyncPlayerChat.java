package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnAsyncPlayerChat implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
        if(!e.isCancelled()){
            String author = e.getPlayer().getName();
            User user = DiscordAuth.getUsers().get(author);
            if(user != null && !user.isLogged()){
                e.getPlayer().sendMessage(ChatColor.RED + DiscordAuth.getLanguage().needLogin);
                e.setCancelled(true);
                return;
            }
            if(DiscordAuth.getConfiguration().getEnableSharedChat()){
                String message = String.format("<%s> %s", author, e.getMessage());
                BotUtils.sendMessage(message);
            }
        }
    }
}
