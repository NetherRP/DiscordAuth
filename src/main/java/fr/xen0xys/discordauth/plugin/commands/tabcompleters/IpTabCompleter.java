package fr.xen0xys.discordauth.plugin.commands.tabcompleters;

import fr.xen0xys.discordauth.DiscordAuth;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IpTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player){
            if(args.length == 1){
                return List.of(new String[]{"allow", "disallow", "block", "unblock", "list"});
            }else if(args.length == 2){
                switch (args[0]) {
                    case "disallow":
                        return List.of(DiscordAuth.getAccountTable().getIps(player.getUniqueId(), player.getName(), true));
                    case "unblock":
                        return List.of(DiscordAuth.getAccountTable().getIps(player.getUniqueId(), player.getName(), false));
                    case "allow":
                    case "block":
                        return List.of(new String[]{"current"});
                }
            }
        }
        return null;
    }
}
