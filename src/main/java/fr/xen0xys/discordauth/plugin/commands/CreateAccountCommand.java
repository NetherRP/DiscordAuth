package fr.xen0xys.discordauth.plugin.commands;

import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.xen0lib.utils.Status;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CreateAccountCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 3){
            String minecraftName = strings[0];
            long discordId = Long.parseLong(strings[1]);
            String password = strings[2];

            Status status = Commands.createAccount(minecraftName, discordId, password);
            if(status == Status.Success){
                commandSender.sendMessage(ChatColor.GREEN + DiscordAuthOld.getLanguage().accountCreatedSuccessful);
            }else if(status == Status.Exist){
                commandSender.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().alreadyHasAccount);
            }else if(status == Status.Invalid){
                commandSender.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().passwordDefineError);
            }else{
                commandSender.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().errorOccurred);
            }
        }else{
            return false;
        }
        return true;
    }
}
