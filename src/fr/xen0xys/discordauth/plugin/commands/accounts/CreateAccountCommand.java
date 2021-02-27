package fr.xen0xys.discordauth.plugin.commands.accounts;

import fr.xen0xys.discordauth.databases.AccountsDatabase;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateAccountCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            if(strings.length >= 4){
                if(strings[2].equals(strings[3])){
                    if(executeCreateAccount(Long.parseLong(strings[0]), strings[1], strings[2])){
                        commandSender.sendMessage(ChatColor.GREEN + "Account created successful");
                    }else{
                        commandSender.sendMessage(ChatColor.RED + "The account could not be created");
                    }
                }else{
                    commandSender.sendMessage(ChatColor.RED + "The 2 passwords must be identical");
                }
            }else{
                commandSender.sendMessage(ChatColor.RED + "Usage: /createaccount <discord_id> <minecraft_name> <password> <password>");
            }
        }
        return false;
    }

    public static boolean executeCreateAccount(long discord_id, String minecraft_name, String password){
        return new AccountsDatabase().createPlayerAccount(discord_id, minecraft_name, password);
    }
}