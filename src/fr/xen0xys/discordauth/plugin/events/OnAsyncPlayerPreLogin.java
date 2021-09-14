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
        if(table.isUserHasAccount(uuid, minecraftName) != Status.Exist){
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
            e.setKickMessage(ChatColor.RED + DiscordAuth.getLanguage().kickMessage);
        }else{
            String playerIp = e.getAddress().getHostAddress();
            String[] allowedPlayerIps = table.getIps(uuid, minecraftName, true);
            String[] blockerPlayerIps = table.getIps(uuid, minecraftName, false);
            if(allowedPlayerIps[0].equals("")){
                if(blockerPlayerIps[0].equals("*")){
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                    e.setKickMessage(ChatColor.RED + "Votre ip n'a pas été autorisé par le propriétaire du compte!");
                }else if(!blockerPlayerIps[0].equals("")){
                    for(String ip: blockerPlayerIps){
                        if(ip.equals(playerIp)){
                            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                            e.setKickMessage(ChatColor.RED + "Votre ip n'a pas été autorisé par le propriétaire du compte!");
                        }
                    }
                }
            }else if(!allowedPlayerIps[0].equals("*")){
                boolean contain = false;
                for(String ip: allowedPlayerIps){
                    if(ip.equals(playerIp)){
                        contain = true;
                        break;
                    }
                }
                if(!contain){
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                    e.setKickMessage(ChatColor.RED + "Votre ip n'a pas été autorisé par le propriétaire du compte!");
                }
            }
        }
    }
}
