package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
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

        if(table.isUserHasAccount(uuid, minecraftName) != Status.Exist){
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
            e.setKickMessage(ChatColor.RED + DiscordAuth.getLanguage().kickMessage);
        }else{
            String[] allowedPlayerIps = table.getIps(uuid, minecraftName, true);
            String[] blockerPlayerIps = table.getIps(uuid, minecraftName, false);
            // Case if no ip are allowed
            if(allowedPlayerIps[0].equals("")){
                // Case if all ip are blocked
                if(blockerPlayerIps[0].equals("*")){
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                    e.setKickMessage(ChatColor.RED + DiscordAuth.getLanguage().unauthorizedIp);
                    // No owner prevention if ip are intentionally locked
                // Case if there are blocked ip
                }else if(!blockerPlayerIps[0].equals("")){
                    for(String ip: blockerPlayerIps){
                        if(ip.equals(playerIp)){
                            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                            e.setKickMessage(ChatColor.RED + DiscordAuth.getLanguage().unauthorizedIp);
                            // No owner prevention if ip are intentionally locked
                        }
                    }
                }
            // Case if no all ip are allowed
            }else if(!allowedPlayerIps[0].equals("*")){
                boolean contain = false;
                for(String ip: allowedPlayerIps){
                    if(ip.equals(playerIp)){
                        contain = true;
                        break;
                    }
                }
                // Case if player ip is not intentionally allowed
                if(!contain){
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                    e.setKickMessage(ChatColor.RED + DiscordAuth.getLanguage().unauthorizedIp);
                    // Send DM message to prevent discord account owner
                    boolean blockedContain = false;
                    for(String ip: blockerPlayerIps){
                        if(ip.equals(playerIp) || ip.equals("*")){
                            blockedContain = true;
                            break;
                        }
                    }
                    // Case if player ip is not intentionally blocked
                    if(!blockedContain){
                        PluginUtils.sendConnectionWarning(minecraftName, playerIp);
                    }
                }
            }
        }
    }
}
