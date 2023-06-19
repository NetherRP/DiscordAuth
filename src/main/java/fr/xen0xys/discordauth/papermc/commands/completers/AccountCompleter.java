package fr.xen0xys.discordauth.papermc.commands.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AccountCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player) {
            switch (args.length) {
                case 1 -> {
                    List<String> list = new ArrayList<>();
                    if(player.hasPermission("discordauth.account.create"))
                        list.add("create");
                    if(player.hasPermission("discordauth.account.manage.self") || player.hasPermission("discordauth.account.manage.other"))
                        list.add("manage");
                    if(player.hasPermission("discordauth.account.delete.self") || player.hasPermission("discordauth.account.delete.other"))
                        list.add("delete");
                    return list;
                }
                case 2 -> {
                    if(args[0].equalsIgnoreCase("create") && player.hasPermission("discordauth.account.create"))
                        return List.of(new String[]{"<discordId>"});
                    if(args[0].equalsIgnoreCase("manage") && (player.hasPermission("discordauth.account.manage.self") || player.hasPermission("discordauth.account.manage.other")))
                        return List.of(new String[]{"password"});
                }
                case 3 -> {
                    if(args[0].equalsIgnoreCase("manage") && (player.hasPermission("discordauth.account.manage.self") || player.hasPermission("discordauth.account.manage.other"))){
                        if(args[1].equalsIgnoreCase("password"))
                            return List.of(new String[]{"<password>"});
                    }
                    if(args[0].equalsIgnoreCase("create") && player.hasPermission("discordauth.account.create"))
                        return List.of(new String[]{"<username>"});
                }
                case 4 -> {
                    if(args[0].equalsIgnoreCase("manage") && (player.hasPermission("discordauth.account.manage.self") || player.hasPermission("discordauth.account.manage.other"))){
                        if(args[1].equalsIgnoreCase("password") && player.hasPermission("discordauth.account.manage.other"))
                            return List.of(new String[]{"<target>"});
                    }
                    if(args[0].equalsIgnoreCase("create") && player.hasPermission("discordauth.account.create"))
                        return List.of(new String[]{"<password>"});
                }
            }
        }
        return null;
    }
}
