package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.databases.AccountsDatabase;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class OnAsyncPlayerPreLogin implements Listener {
    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e){
        AccountsDatabase accountDatabase = new AccountsDatabase();
        if(!accountDatabase.isMinecraftNameExist(e.getName())){
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, ChatColor.RED + "You do not have an account on the server, check your MP Discord and / or that you have reacted to the required message.");
        }
    }
}
