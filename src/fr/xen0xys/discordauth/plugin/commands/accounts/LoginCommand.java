package fr.xen0xys.discordauth.plugin.commands.accounts;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.databases.SecurityDatabase;
import fr.xen0xys.discordauth.models.User;
import fr.xen0xys.discordauth.utils.PluginUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            if(strings.length >= 1){
                executeLogin((Player) commandSender, strings[0]);
                return true;
            }else{
                commandSender.sendMessage(ChatColor.RED + "Not enough arguments: /login <password>");
            }
        }else{
            System.out.println(ChatColor.RED + "You need to execute this command as player");
        }
        return false;
    }

    public static void executeLogin(Player player, String password){
        String minecraftName = player.getName();
        User user = DiscordAuth.getUsers().get(minecraftName);
        String ip = PluginUtils.getPlayerIP(player);
        SecurityDatabase securityDatabase = new SecurityDatabase();
        if(!user.isLogged()){
            if(securityDatabase.checkPassword(minecraftName, password)){
                if(securityDatabase.loginUser(minecraftName, ip)){
                    player.sendMessage(ChatColor.GREEN + "You are login successful");
                }else{
                    player.sendMessage(ChatColor.GOLD + "You are login, but an error occurred");
                }
                user.setIsLogged(true);
            }else{
                player.kickPlayer(ChatColor.RED + "Incorrect password");
            }
        }else{
            player.sendMessage(ChatColor.RED + "You are already connected");
        }
    }


}