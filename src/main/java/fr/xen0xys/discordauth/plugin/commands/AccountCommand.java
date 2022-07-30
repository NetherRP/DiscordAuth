package fr.xen0xys.discordauth.plugin.commands;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.xen0lib.utils.Status;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AccountCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length >= 1){
            switch (args[0]){
                case "create":
                    if(args.length >= 4 && commandSender.isOp()){
                        String minecraftName = args[1];
                        long discordId = Long.parseLong(args[2]);
                        String password = args[3];
                        Status status = Commands.createAccount(minecraftName, discordId, password, true);
                        if(status == Status.Success){
                            commandSender.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().accountCreatedSuccessful);
                        }else if(status == Status.Exist){
                            commandSender.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().alreadyHasAccount);
                        }else if(status == Status.Invalid){
                            commandSender.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().passwordDefineError);
                        }else{
                            commandSender.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().errorOccurred);
                        }
                    }else{
                        return false;
                    }
                    return true;
                case "delete":
                    break;
                case "manage":
                    if(args.length >= 3){
                        // Password changing
                        if ("-password".equals(args[1])) {
                            if (commandSender instanceof Player player) {
                                switch (Commands.changePassword(player.getName(), args[2])) {
                                    case Success ->
                                            commandSender.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().updatedPassword);
                                    case Invalid ->
                                            commandSender.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().invalidPassword);
                                    case SQLError ->
                                            commandSender.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().errorOccurred);
                                }
                            }
                        }
                    }
                    return true;
            }
        }
        return false;
    }
}
