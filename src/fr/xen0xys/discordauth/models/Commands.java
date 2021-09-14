package fr.xen0xys.discordauth.models;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
import fr.xen0xys.xen0lib.utils.Status;
import fr.xen0xys.xen0lib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class Commands {

    /**
     * Create player account
     * @param minecraftName Player name
     * @param discordId Player discord id
     * @param password player password
     * @return Xen0Lib Status: Success, SQLError, Exist
     */
    public static Status createAccount(String minecraftName, long discordId, String password){
        // Check if account already exist
        AccountTable accountTable = DiscordAuth.getAccountTable();
        if(accountTable.isDiscordUserExist(discordId) == Status.NotExist){
            // Encrypt password
            String encryptedPassword = PluginUtils.encryptPassword(password);
            // UUID uuid = Utils.getUUIDFromUsername(minecraftName);
            // Set uuid to blank here
            return accountTable.addAccount(null, minecraftName, discordId, encryptedPassword);
        }else{
            return Status.Exist;
        }
    }

    /**
     * Login player
     * @param player Player
     * @param password Player password
     * @return Xen0Lib Status: Valid, Invalid, NotExist, SQLError
     */
    public static Status login(Player player, String password){
        AccountTable accountTable = DiscordAuth.getAccountTable();
        Status status = accountTable.checkPassword(player, PluginUtils.encryptPassword(password));;
        if(status == Status.Valid){
            accountTable.setLastLogin(player, Utils.getPlayerIP(player));
            accountTable.setSession(true, player);
            DiscordAuth.getUsers().get(player.getName()).setIsLogged(true);
            player.closeInventory();
        }
        return status;
    }

    /**
     * Login player
     * @param discordId Player discord id
     * @return Xen0Lib Status: Success, NotExist, Error
     */
    public static Status login(long discordId){

        AccountTable accountTable = DiscordAuth.getAccountTable();

        String minecraftName = accountTable.getMinecraftNameFromDiscordId(discordId);
        if(minecraftName != null){
            Player player = Bukkit.getPlayer(minecraftName);
            if(player != null){
                accountTable.setLastLogin(player, Utils.getPlayerIP(player));
                accountTable.setSession(true, player);
                DiscordAuth.getUsers().get(player.getName()).setIsLogged(true);
                Bukkit.getServer().getScheduler().runTask(DiscordAuth.getInstance(), new Runnable(){
                    @Override
                    public void run(){
                        player.closeInventory();
                    }
                });
                player.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().discordLogin);
                return Status.Success;
            }else{
                return Status.Error;
            }
        }
        return Status.NotExist;
    }

    /**
     * Logout player
     * @param player Player
     */
    public static void logout(Player player){
        User user = DiscordAuth.getUsers().get(player.getName());
        user.setLoginLocation(player.getLocation());
        user.setIsLogged(false);

        AccountTable accountTable = DiscordAuth.getAccountTable();
        accountTable.setSession(false, player);

        player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().loginRequest);
        if(DiscordAuth.getConfiguration().getAggressiveLogin()) {
            PluginUtils.displayLoginScreen(player);
        }
    }

    /**
     * Change player password
     * @param player Player
     * @param newPassword New Player password
     * @return Xen0Lib Status: Success, SQLError
     */
    public static Status changePassword(Player player, String newPassword){
        String encryptPassword = PluginUtils.encryptPassword(newPassword);

        AccountTable accountTable = DiscordAuth.getAccountTable();
        return accountTable.setPassword(player, encryptPassword);
    }

    /**
     * Forcelogin a player
     * @param player Player
     */
    public static void forceLogin(Player player){
        AccountTable accountTable = DiscordAuth.getAccountTable();
        accountTable.setLastLogin(player, Utils.getPlayerIP(player));
        accountTable.setSession(true, player);
        player.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().forcedLogin);
        player.closeInventory();
        DiscordAuth.getUsers().get(player.getName()).setIsLogged(true);
    }

    public static Status ipCommand(UUID uuid, String minecraftName, String[] args){
        if(args.length > 0){
            switch (args[0]){
                case "allow":
                    if(args.length >= 2){
                        return IpCommands.addIps(uuid, minecraftName, true, args);
                    }
                    break;
                case "disallow":
                    if(args.length >= 2){
                        return IpCommands.removeIps(uuid, minecraftName, true, args);
                    }
                    break;
                case "block":
                    if(args.length >= 2){
                        return IpCommands.addIps(uuid, minecraftName, false, args);
                    }
                    break;
                case "unblock":
                    if(args.length >= 2){
                        return IpCommands.removeIps(uuid, minecraftName, false, args);
                    }
                    break;
                case "list":
                    if(IpCommands.listIps(uuid, minecraftName, args)){
                        return Status.Allowed;
                    }else{
                        return Status.NotExist;
                    }
                default:
                    return Status.Invalid;
            }
        }
        return Status.Error;
    }

}
