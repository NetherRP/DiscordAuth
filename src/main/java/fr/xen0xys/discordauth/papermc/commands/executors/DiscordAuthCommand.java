package fr.xen0xys.discordauth.papermc.commands.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public class DiscordAuthCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length != 1)
            return false;
        switch (strings[0]) {
            case "reload" -> reload();
            default -> {
                return false;
            }
        };
        return true;
    }

    private void reload(){

    }
}
