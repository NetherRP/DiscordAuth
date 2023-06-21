package fr.xen0xys.discordauth.papermc.commands.executors;

import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.ConnectionAskPacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player player)) return false;
        if(args.length == 0){
            displayPasswordAsk(player);
            return true;
        }
        ConnectionAskPacket packet = new ConnectionAskPacket(player.getUniqueId(), args[0]);
        ServerPacket.sendServer(player, SubChannels.CONNECTION_ASK, packet);
        return true;
    }

    public static void displayPasswordAsk(@NotNull final Player player){
        AnvilGUI.Builder builder = new AnvilGUI.Builder()
                .plugin(DiscordAuthPlugin.getInstance())
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Password :")
                .text("➞");
        builder.onClick((slot, stateSnapshot) -> {
            String password = stateSnapshot.getText();
            if(password.contains("➞"))
                password = password.replace("➞", "");
            final String finalPassword = password;
            new BukkitRunnable() {
                @Override
                public void run() {
                    ConnectionAskPacket packet = new ConnectionAskPacket(player.getUniqueId(), finalPassword);
                    ServerPacket.sendServer(player, SubChannels.CONNECTION_ASK, packet);
                }
            }.runTaskAsynchronously(DiscordAuthPlugin.getInstance());
            return List.of(AnvilGUI.ResponseAction.close());
        });
        builder.open(player);
    }
}
