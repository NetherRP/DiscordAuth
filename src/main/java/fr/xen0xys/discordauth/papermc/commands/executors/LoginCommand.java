package fr.xen0xys.discordauth.papermc.commands.executors;

import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.ConnectionAskPacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            if(strings.length == 0){
                displayPasswordAsk(player);
                return true;
            }
            ConnectionAskPacket packet = new ConnectionAskPacket(player.getUniqueId(), strings[0]);
            ServerPacket.sendServer(player, SubChannels.CONNECTION_ASK, packet);
            return true;
        }
        return false;
    }

    public static void displayPasswordAsk(Player player){
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder.plugin(DiscordAuthPlugin.getInstance());
        builder.title("Password :");
        builder.text("");
        builder.onClick((slot, stateSnapshot) -> {
            ConnectionAskPacket packet = new ConnectionAskPacket(player.getUniqueId(), stateSnapshot.getText());
            ServerPacket.sendServer(player, SubChannels.CONNECTION_ASK, packet);
            return List.of(AnvilGUI.ResponseAction.close());
        });
        builder.open(player);
    }
}
