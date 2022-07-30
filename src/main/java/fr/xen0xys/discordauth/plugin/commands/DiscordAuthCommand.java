package fr.xen0xys.discordauth.plugin.commands;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth_old.DiscordAuthOld;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordAuthCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 0){
            if(args[0].equals("reload")){
                commandSender.sendMessage(ChatColor.GREEN + "Reloading DiscordAuth...");
                DiscordAuth.getInstance().onDisable();
                DiscordAuth.getInstance().onLoad();
                DiscordAuth.getInstance().onEnable();
                commandSender.sendMessage(ChatColor.GREEN + "DiscordAuth reloaded!");
                return true;
            }
        }
        return false;
    }
}
