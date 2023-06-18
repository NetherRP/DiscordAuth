package fr.xen0xys.discordauth.papermc.commands.executors;

import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.SessionInvalidationAskPacket;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LogoutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            SessionInvalidationAskPacket packet = new SessionInvalidationAskPacket(player.getUniqueId());
            ServerPacket.sendServer(player, SubChannels.SESSION_INVALIDATION_ASK, packet);
            return true;
        }
        return false;
    }
}
