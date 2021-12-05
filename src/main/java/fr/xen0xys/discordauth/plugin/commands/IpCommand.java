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

public class IpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player){
            Status status = Commands.ipCommand(player.getUniqueId(), player.getName(), args);
            if(status == Status.NotExist){
                player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().playerNotConnected);
            }else if(status == Status.Invalid){
                player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().invalidIpFormat);
            }else if(status == Status.Success){
                player.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().actionSuccess);
            }else if(status == Status.Allowed){
                return true;
            }else{
                player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().errorOccurred);
            }
        }
        return true;
    }
}
