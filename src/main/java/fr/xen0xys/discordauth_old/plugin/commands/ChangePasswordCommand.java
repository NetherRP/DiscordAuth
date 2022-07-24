package fr.xen0xys.discordauth_old.plugin.commands;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.xen0lib.utils.Status;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChangePasswordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            if(strings.length == 1){
                String newPassword = strings[0];
                Status status = Commands.changePassword(player, newPassword);
                if(status == Status.Success){
                    player.sendMessage(ChatColor.GREEN + DiscordAuthOld.getLanguage().updatedPassword);
                }else if(status == Status.Invalid){
                    player.sendMessage(ChatColor.GREEN + DiscordAuthOld.getLanguage().passwordDefineError);
                }else{
                    player.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().errorOccurred);
                }
            }else{
                return true;
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().cannotBeExecutedFromCLI);
        }
        return false;
    }
}
