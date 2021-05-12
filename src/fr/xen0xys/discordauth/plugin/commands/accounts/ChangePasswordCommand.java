package fr.xen0xys.discordauth.plugin.commands.accounts;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.databases.SecurityDatabase;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChangePasswordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            if(strings.length >= 2){
                if(strings[0].equals(strings[1])){
                    executeChangePassword((Player) commandSender, strings[0]);
                }else{
                    commandSender.sendMessage(ChatColor.RED + "The 2 passwords must be identical");
                }
            }else{
                commandSender.sendMessage(ChatColor.RED + "Usage: /changepassword <password> <password confirmation>");
            }
        }else{
            System.out.println(ChatColor.RED + "You need to execute this command as player");
        }
        return false;
    }

    public static void executeChangePassword(Player player, String new_password){
        String minecraft_name = player.getName();
        User user = DiscordAuth.getUsers().get(minecraft_name);
        if(user.isLogged()){
            if(new SecurityDatabase().setPassword(minecraft_name, new_password)){
                player.sendMessage(ChatColor.GREEN + "Your password has been successfully changed");
            }else{
                player.sendMessage(ChatColor.RED + "An error occurred while changing your password");
            }
        }else{
            player.kickPlayer(ChatColor.RED + "You do not have the right to execute this command without being logged in");
        }
    }
}