package fr.xen0xys.discordauth.plugin.commands;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth_old.DiscordAuthOld;
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
        if(strings.length == 1){
            Player targetPlayer = Bukkit.getPlayer(strings[0]);
            if(targetPlayer != null){
                if(!DiscordAuth.getUsers().get(targetPlayer.getName()).isLogged()){
                    Commands.forceLogin(targetPlayer);
                    commandSender.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().playerForceLogged);
                }else{
                    commandSender.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().alreadyConnected);
                }

            }else{
                commandSender.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().unknownSpecifiedPlayer);
            }
        }else{
            return false;
        }
        return true;
    }
}
