package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.xen0lib.utils.Status;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class OnAsyncPlayerPreLogin implements Listener {
    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e){
        UUID uuid = e.getUniqueId();
        String minecraftName = e.getName();
        AccountTable table = DiscordAuth.getAccountTable();

        // Register last player connect ip
        String playerIp = e.getAddress().getHostAddress();

        // Fix here because null pointer exception
        if(table.getUUIDFromMinecraftName(e.getName()).equals("")){
            table.setUUID(uuid, minecraftName);
        }

        if(table.isUserHasAccount(uuid, minecraftName) != Status.Exist){
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
            e.setKickMessage(ChatColor.RED + DiscordAuth.getLanguage().kickMessage);
        }
    }
}
