package fr.xen0xys.discordauth.papermc.commands.executors;

import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.packets.AccountCreationAskPacket;
import fr.xen0xys.discordauth.common.network.packets.ChangePasswordAskPacket;
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
                    return createAccount(player, strings);
                }
                case "delete" -> {
                    return deleteAccount(player, strings);
                }
                case "manage" -> {
                    return manageAccount(player, strings);
                }
                default -> {
                    commandSender.sendMessage(Component.text("Usage: /account <create|delete|manage>"));
                    return false;
                }
            }
        }
        return false;
    }

    private boolean createAccount(Player player, String[] args){
        if(args.length < 4){
            player.sendMessage(Component.text("Usage: /account create <discordId> <minecraftName> <password>"));
            return false;
        }
        if(!player.hasPermission("discordauth.account.create")){
            player.sendMessage(Component.text("You don't have the permission to do that! (discordauth.account.create)"));
            return false;
        }
        long discordId = Long.parseLong(args[1]);
        String minecraftName = args[2];
        Encryption encryption = new Encryption(DiscordAuthPlugin.getInstance().getLogger());
        String encryptedPassword = encryption.hash(args[3]);
        AccountCreationAskPacket packet = new AccountCreationAskPacket(discordId, minecraftName, encryptedPassword);
        ServerPacket.sendServer(player, SubChannels.ACCOUNT_CREATION_ASK, packet);
        return true;
    }
    private boolean deleteAccount(Player player, String[] args){
        // TODO
        return false;
    }

    private boolean manageAccount(Player player, String[] args){
        if(args.length < 2){
            player.sendMessage(Component.text("Usage: /account manage <password> <newPassword> [player]"));
            return false;
        }
        if(!args[1].equals("password")){
            player.sendMessage(Component.text("Usage: /account manage <password> <newPassword> [player]"));
            return false;
        }
        if (args.length == 3) {
            // Change self password
            if (!player.hasPermission("discordauth.account.manage.self")) {
                player.sendMessage(Component.text("You don't have the permission to do that! (discordauth.account.manage.self)"));
                return false;
            }
            String encryptedPassword = new Encryption(DiscordAuthPlugin.getInstance().getLogger()).hash(args[2]);
            ChangePasswordAskPacket packet = new ChangePasswordAskPacket(player.getUniqueId(), encryptedPassword);
            ServerPacket.sendServer(player, SubChannels.CHANGE_PASSWORD_ASK, packet);
        } else if (args.length == 4) {
            // Change other password
            if (!player.hasPermission("discordauth.account.manage.other")) {
                player.sendMessage(Component.text("You don't have the permission to do that! (discordauth.account.manage.other)"));
                return false;
            }
            Player target = DiscordAuthPlugin.getInstance().getServer().getPlayer(args[3]);
            if (target == null) {
                player.sendMessage(Component.text("Player not found!"));
                return false;
            }
            String encryptedPassword = new Encryption(DiscordAuthPlugin.getInstance().getLogger()).hash(args[2]);
            ChangePasswordAskPacket packet = new ChangePasswordAskPacket(target.getUniqueId(), encryptedPassword);
            ServerPacket.sendServer(player, SubChannels.CHANGE_PASSWORD_ASK, packet);
        }
        return true;
    }
}
