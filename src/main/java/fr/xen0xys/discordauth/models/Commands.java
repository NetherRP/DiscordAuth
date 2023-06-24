package fr.xen0xys.discordauth.models;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.utils.PluginUtils;
import fr.xen0xys.xen0lib.utils.Status;
import fr.xen0xys.xen0lib.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Commands {

    /**
     * Create player account
     * @param minecraftName Player name
     * @param discordId Player discord id
     * @param password player password
     * @return Xen0Lib Status: Success, SQLError, Exist, Invalid
     */
    public static Status createAccount(String minecraftName, long discordId, String password, boolean bypass){
        if(PluginUtils.isUserCanCreateAccount(discordId) || bypass){
            // Check if account already exist
            AccountTable accountTable = DiscordAuth.getAccountTable();
            if (discordId == 0) {
                do{
                    discordId = PluginUtils.getRandomLong(0L, Long.MAX_VALUE);
                }while (accountTable.isDiscordUserExist(discordId) == Status.Exist);
            }
            if(accountTable.isDiscordUserExist(discordId) == Status.NotExist || discordId == 0){
                // Check password integrity
                if(!PluginUtils.checkPasswordRegex(minecraftName) || !PluginUtils.checkPasswordRegex(password)){
                    return Status.Invalid;
                }
                // Encrypt password
                String encryptedPassword = PluginUtils.encryptPassword(password);
                // UUID uuid = Utils.getUUIDFromUsername(minecraftName);
                // Set uuid to blank here
                Status result = accountTable.addAccount(null, minecraftName, discordId, encryptedPassword);
                if(result == Status.Success && DiscordAuth.getConfiguration().getUsernameChange()){
                    Guild guild = BotUtils.getGuild();
                    if(guild != null){
                        Member member = guild.retrieveMemberById(discordId).complete();
                        if(member != null){
                            try{
                                member.modifyNickname(minecraftName).queue();
                            } catch (HierarchyException e){
                                DiscordAuth.getInstance().getLogger().warning(String.format("Can't change username for %s", minecraftName));
                            }
                        }
                    }
                }
                System.out.println(result);
                System.out.println(DiscordAuth.getConfiguration().getUsernameChange());
                return result;
            }else{
                return Status.Exist;
            }
        }else{
            return Status.Denied;
        }
    }

    public static Status createAccount(String minecraftName, long discordId, String password){
        return createAccount(minecraftName, discordId, password, false);
    }

    /**
     * Login player
     * @param player Player
     * @param password Player password
     * @return Xen0Lib Status: Valid, Invalid, NotExist, SQLError
     */
    public static Status login(Player player, String password){
        if(!PluginUtils.checkPasswordRegex(password)){
            return Status.Invalid;
        }
        AccountTable accountTable = DiscordAuth.getAccountTable();
        Status status = accountTable.checkPassword(player, PluginUtils.encryptPassword(password));
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
     * @param discordId Account's discord ID
     * @param newPassword New Player password
     * @return Xen0Lib Status: Invalid, Success, SQLError
     */
    public static Status changePassword(long discordId, String newPassword){
        if(!PluginUtils.checkPasswordRegex(newPassword)){
            return Status.Invalid;
        }
        String encryptPassword = PluginUtils.encryptPassword(newPassword);

        AccountTable accountTable = DiscordAuth.getAccountTable();
        return accountTable.setPassword(discordId, encryptPassword);
    }

    public static Status changePassword(String minecraftName, String newPassword){
        return changePassword(DiscordAuth.getAccountTable().getDiscordIdFromMinecraftName(minecraftName), newPassword);
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

    /**
     * Delete player account
     * @param discordId player's discord id
     * @return Xen0Lib Status: Success, SQLError, NotExist
     */
    public static Status deleteAccount(long discordId){
        AccountTable accountTable = DiscordAuth.getAccountTable();
        if(discordId != -1){
            if(accountTable.isDiscordUserExist(discordId) == Status.Exist)
                return accountTable.deleteAccount(discordId);
        }
        return Status.NotExist;
    }

    public static Status deleteAccount(String minecraftName){
        return deleteAccount(DiscordAuth.getAccountTable().getDiscordIdFromMinecraftName(minecraftName));
    }

}