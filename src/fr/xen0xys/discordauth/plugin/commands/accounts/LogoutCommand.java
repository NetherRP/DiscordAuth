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

public class LogoutCommand implements CommandExecutor{
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            executeLogout(player);
        }else{
            System.out.println(ChatColor.RED + "You need to execute this command as player");
        }
        return false;
    }

    public static void executeLogout(Player player){
        String minecraftName = player.getName();
        if(new SecurityDatabase().logoutUser(minecraftName)){
            User user = DiscordAuth.getUsers().get(minecraftName);
            user.setIsLogged(false);
            user.setLoginLocation(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "You are logout successful");
        }else{
            player.sendMessage(ChatColor.RED + "An error occurred");
        }
    }
}