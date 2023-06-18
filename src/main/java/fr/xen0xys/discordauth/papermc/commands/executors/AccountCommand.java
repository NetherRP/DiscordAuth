package fr.xen0xys.discordauth.papermc.commands.executors;

import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.AccountCreationAskPacket;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import fr.xen0xys.discordauth.papermc.network.ServerPacket;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AccountCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            if(strings.length < 1)
                return false;
            switch (strings[0]){
                case "create" -> {
                    if(strings.length < 4){
                        commandSender.sendMessage(Component.text("Usage: /account create <discordId> <minecraftName> <password>"));
                        return false;
                    }
                    if(!commandSender.isOp()){
                        commandSender.sendMessage(Component.text("You don't have the permission to do that!"));
                        return false;
                    }
                    long discordId = Long.parseLong(strings[1]);
                    String minecraftName = strings[2];
                    Encryption encryption = new Encryption(DiscordAuthPlugin.getInstance().getLogger());
                    String encryptedPassword = encryption.hash(strings[3]);
                    AccountCreationAskPacket packet = new AccountCreationAskPacket(discordId, minecraftName, encryptedPassword);
                    ServerPacket.sendServer(player, SubChannels.ACCOUNT_CREATION_ASK, packet);
                    return true;
                }
                case "delete" -> {
                    return false;
                }
                case "manage" -> {
                    return false;
                }
                default -> {
                    commandSender.sendMessage(Component.text("Usage: /account <create|delete|manage>"));
                    return false;
                }
            }
        }
        return false;
    }
}
