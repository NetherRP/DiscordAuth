package fr.xen0xys.discordauth.plugin.commands.accounts;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceLoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            if(commandSender.isOp()){
                if(strings.length >= 1){
                    if(executeForceLogin(strings[0])){
                        commandSender.sendMessage(ChatColor.GREEN + "The player has been force logged");
                        return true;
                    }
                    commandSender.sendMessage(ChatColor.RED + "The giver player is not found");
                    return false;
                }
            }
        }else{
            if(strings.length >= 1){
                if(executeForceLogin(strings[0])){
                    commandSender.sendMessage(ChatColor.GREEN + "The player has been force logged");
                    return true;
                }
                commandSender.sendMessage(ChatColor.RED + "The giver player is not found");
                return false;
            }
        }
        return false;
    }

    public static boolean executeForceLogin(String minecraftName){
        Player player = Bukkit.getPlayer(minecraftName);
        if(player != null){
            User user = DiscordAuth.getUsers().get(minecraftName);
            user.setIsLogged(true);
            player.sendMessage(ChatColor.DARK_AQUA + "You has been force disconnected");
            return true;
        }
        return false;
    }
}