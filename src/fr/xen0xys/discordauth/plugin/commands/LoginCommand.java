package fr.xen0xys.discordauth.plugin.commands;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
import fr.xen0xys.xen0lib.utils.Status;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            if(strings.length == 0){
                PluginUtils.displayLoginScreen(player);
            }else{
                Status status = Commands.login(player, strings[0]);
                if(status == Status.Valid){
                    player.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().loginSuccess);
                }else if(status == Status.Invalid){
                    player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().invalidPassword);
                }else{
                    player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().errorOccurred);
                }
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().cannotBeExecutedFromCLI);
        }
        return false;
    }
}
