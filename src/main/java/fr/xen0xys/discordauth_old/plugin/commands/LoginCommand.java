package fr.xen0xys.discordauth_old.plugin.commands;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth.utils.PluginUtils;
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
                if(!DiscordAuthOld.getUsers().get(player.getName()).isLogged()){
                    PluginUtils.displayLoginScreen(player);
                }else{
                    player.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().alreadyConnected);
                }
            }else{
                Status status = Commands.login(player, strings[0]);
                if(status == Status.Valid){
                    player.sendMessage(ChatColor.GREEN + DiscordAuthOld.getLanguage().loginSuccess);
                }else if(status == Status.Invalid){
                    player.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().invalidPassword);
                }else{
                    player.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().errorOccurred);
                }
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + DiscordAuthOld.getLanguage().cannotBeExecutedFromCLI);
        }
        return false;
    }
}
