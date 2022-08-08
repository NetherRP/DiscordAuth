package fr.xen0xys.discordauth.plugin.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AccountTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player) {
            switch (args.length) {
                case 1:
                    if(player.isOp()) {
                        return List.of(new String[]{"create", "manage", "delete"});
                    }else{
                        return List.of(new String[]{"manage", "delete"});
                    }
                case 2:
                    return List.of(new String[]{"-password"});
            }
        }
        return null;
    }
}