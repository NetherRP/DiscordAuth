package fr.xen0xys.discordauth.papermc.commands.executors;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceLoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length != 1)
            return false;
        Player targetPlayer = commandSender.getServer().getPlayer(strings[0]);
        if(targetPlayer == null){
            commandSender.sendMessage(Component.text("This player is not online!"));
            return false;
        }
        if(!DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(targetPlayer.getUniqueId())){
            commandSender.sendMessage(Component.text("This player is already authenticated!"));
            return false;
        }
        DiscordAuthPlugin.getUnauthenticatedPlayers().remove(targetPlayer.getUniqueId());
        return true;
    }
}
