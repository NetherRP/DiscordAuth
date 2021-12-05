package fr.xen0xys.discordauth.models;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.xen0lib.utils.Status;
import fr.xen0xys.xen0lib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class IpCommands {

    public static Status addIps(UUID uuid, String minecraftName, boolean allow, String[] args){
        if(args[1].equals("current")){
            Player player = Bukkit.getPlayer(uuid);
            if(player != null){
                return DiscordAuth.getAccountTable().addIps(uuid, minecraftName, allow, new String[]{Utils.getPlayerIP(player)});
            }else{
                return Status.NotExist;
            }
        }else if(args[1].equals("all") || args[1].equals("*")){
            return DiscordAuth.getAccountTable().setIps(uuid, minecraftName, allow, "*");
        }else{
            return DiscordAuth.getAccountTable().addIps(uuid, minecraftName, allow, new String[]{args[1]});
        }
    }

    public static Status removeIps(UUID uuid, String minecraftName, boolean allow, String[] args){
        if(args[1].equals("current")){
            Player player = Bukkit.getPlayer(uuid);
            if(player != null){
                return DiscordAuth.getAccountTable().removeIps(uuid, minecraftName, allow, new String[]{Utils.getPlayerIP(player)});
            }else{
                return Status.NotExist;
            }
        }else if(args[1].equals("all") || args[1].equals("*")){
            return DiscordAuth.getAccountTable().setIps(uuid, minecraftName, allow, "");
        }else{
            return DiscordAuth.getAccountTable().removeIps(uuid, minecraftName, allow, new String[]{args[1]});
        }
    }

    public static boolean listIps(UUID uuid, String minecraftName, String[] args){
        String[] allowedIps = DiscordAuth.getAccountTable().getIps(uuid, minecraftName, true);
        String[] deniedIps = DiscordAuth.getAccountTable().getIps(uuid, minecraftName, false);
        Player player = Bukkit.getPlayer(uuid);
        if(player != null){
            player.sendMessage(ChatColor.GOLD + "Ips:");
            player.sendMessage(ChatColor.GREEN + "  " + DiscordAuth.getLanguage().allowed);
            if(allowedIps.length > 0){
                if(allowedIps[0].equals("")){
                    player.sendMessage(ChatColor.RED + "    " + DiscordAuth.getLanguage().noIpAllowed);
                }else if(allowedIps[0].equals("*")){
                    player.sendMessage(ChatColor.GREEN + "    " + DiscordAuth.getLanguage().allIpAllowed);
                }else{
                    for(String ip: allowedIps){
                        player.sendMessage(ChatColor.AQUA + "    - " + ip);
                    }
                }
            }else{
                player.sendMessage(ChatColor.RED + "    " + DiscordAuth.getLanguage().noIpBlocked);
            }
            player.sendMessage(ChatColor.RED + "  " + DiscordAuth.getLanguage().blocked);
            if(deniedIps.length > 0){
                if(deniedIps[0].equals("")){
                    player.sendMessage(ChatColor.RED + "    " + DiscordAuth.getLanguage().noIpBlocked);
                }else if(deniedIps[0].equals("*")){
                    player.sendMessage(ChatColor.GREEN + "    " + DiscordAuth.getLanguage().allIpBlocked);
                }else{
                    for(String ip: deniedIps){
                        player.sendMessage(ChatColor.AQUA + "    - " + ip);
                    }
                }
            }else{
                player.sendMessage(ChatColor.RED + "    " + DiscordAuth.getLanguage().noIpBlocked);
            }
            return true;
        }
        return false;
    }

}
