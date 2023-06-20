package fr.xen0xys.discordauth.papermc.commands.executors;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ForceLoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length != 1)
            return false;
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if(Objects.isNull(targetPlayer)){
            commandSender.sendMessage(Component.text("This player is not online!"));
            return false;
        }
        if(!DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(targetPlayer.getUniqueId())){
            commandSender.sendMessage(Component.text("This player is already authenticated!"));
            return false;
        }
        DiscordAuthPlugin.getUnauthenticatedPlayers().remove(targetPlayer.getUniqueId());
        targetPlayer.sendMessage(Component.text("You have been authenticated!"));
        return true;
    }
}
