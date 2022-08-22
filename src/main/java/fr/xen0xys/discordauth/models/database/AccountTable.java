package fr.xen0xys.discordauth.models.database;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.utils.PluginUtils;
import fr.xen0xys.xen0lib.database.Database;
import fr.xen0xys.xen0lib.database.Table;
import fr.xen0xys.xen0lib.utils.Status;
import fr.xen0xys.xen0lib.utils.Utils;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountTable extends Table {

    public AccountTable(String tableName, Database database) {
        super(tableName, database);
    }

    /**
     * Check if given discordId is a registered user
     * @param discordId DiscordId
     * @return Xen0Lib Status: Exist, NotExist, SQLError
     */
    public Status isDiscordUserExist(long discordId) {
        String query = String.format("SELECT minecraftName FROM %s WHERE discordId='%s'", this.getTableName(), discordId);
        Status status = this.getDatabase().isDataExist(this.getDatabase().executeQuery(query));
        if(status == Status.Exist){
            return Status.Exist;
        }else if(status == Status.NotExist){
            return Status.NotExist;
        }else{
            return Status.SQLError;
        }
    }

    /**
     * Check if given uuid or minecraftName is a registered user
     * @param uuid Player uuid
     * @param minecraftName Player name
     * @return Xen0Lib Status: Exist, NotExist, SQLError
     */
    public Status isUserHasAccount(UUID uuid, String minecraftName){
        boolean useUUID = DiscordAuth.getConfiguration().getPremium();
        String query;
        query = String.format("SELECT discordId FROM %s WHERE UUID='%s' OR minecraftName='%s'", this.getTableName(), uuid, minecraftName);
        return this.getDatabase().isDataExist(this.getDatabase().executeQuery(query));
    }


    public String getMinecraftNameFromUUID(UUID uuid){
        String query = String.format("SELECT minecraftName FROM %s WHERE UUID='%s'", this.getTableName(), uuid);
        ResultSet rs = this.getDatabase().executeQuery(query);
        try {
            if(rs.next()){
                return rs.getString("minecraftName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUUIDFromMinecraftName(String minecraftName){
        String query = String.format("SELECT UUID FROM %s WHERE minecraftName='%s'", this.getTableName(), minecraftName);
        ResultSet rs = this.getDatabase().executeQuery(query);
        try {
            if(rs.next()){
                return rs.getString("UUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Change user UUID
     * @param uuid Player UUID
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status setUUID(UUID uuid, String minecraftName){
        String query = String.format("UPDATE %s SET UUID='%s' WHERE minecraftName='%s'", this.getTableName(), uuid, minecraftName);
        return this.getDatabase().executeUpdateQuery(query);
    }

    public String getMinecraftNameFromDiscordId(long discordId) {
        String query = String.format("SELECT minecraftName FROM %s WHERE discordId='%s'", this.getTableName(), discordId);
        ResultSet rs = this.getDatabase().executeQuery(query);
        try {
            if(rs.next()){
                return rs.getString("minecraftName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getDiscordIdFromMinecraftName(String minecraftName) {
        String query = String.format("SELECT discordId FROM %s WHERE minecraftName='%s'", this.getTableName(), minecraftName);
        ResultSet rs = this.getDatabase().executeQuery(query);
        try {
            if(rs.next()){
                return rs.getLong("discordId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Check if player has a session
     * @param player Player
     * @return Xen0Lib Status: HasSession, NoSession, SQLError
     */
    public Status isPlayerHasSession(Player player){
        String query;
        if(DiscordAuth.getConfiguration().getPremium()){
            query = String.format("SELECT ip, lastLogin, hasSession FROM %s WHERE UUID='%s'", this.getTableName(), player.getUniqueId());
        }else{
            query = String.format("SELECT ip, lastLogin, hasSession FROM %s WHERE minecraftName='%s'", this.getTableName(), player.getName());
        }
        ResultSet rs = this.getDatabase().executeQuery(query);
        try {
            if (rs.next()) {
                boolean hasSession = rs.getBoolean("hasSession");
                if(hasSession){
                    String ip = PluginUtils.encryptIpIfNeeded(rs.getString("ip"));
                    if(ip.equals(PluginUtils.encryptIpIfNeeded(Utils.getPlayerIP(player)))){
                        long lastLogin = rs.getLong("lastLogin");
                        if(lastLogin + DiscordAuth.getConfiguration().getSessionDuration() >= Utils.getCurrentTimestamp()){
                            return Status.HasSession;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Status.SQLError;
        }
        return Status.NoSession;
    }

    /**
     * Set player last login
     * @param player Player
     * @param ip Player ip
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status setLastLogin(Player player, String ip){
        String query;
        if(DiscordAuth.getConfiguration().getPremium()){
            query = String.format("UPDATE %s SET lastLogin='%s', ip='%s' WHERE UUID='%s'", this.getTableName(), Utils.getCurrentTimestamp(), PluginUtils.encryptIpIfNeeded(ip), player.getUniqueId());
        }else{
            query = String.format("UPDATE %s SET lastLogin='%s', ip='%s' WHERE minecraftName='%s'", this.getTableName(), Utils.getCurrentTimestamp(), PluginUtils.encryptIpIfNeeded(ip), player.getName());
        }
        return this.getDatabase().executeUpdateQuery(query);
    }

    /**
     * Set player session
     * @param sessionState state of the session (enable or disabled)
     * @param player Player
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status setSession(boolean sessionState, Player player){
        String query;
        if(DiscordAuth.getConfiguration().getPremium()){
            query = String.format("UPDATE %s SET hasSession='%d' WHERE UUID='%s'", this.getTableName(), sessionState ? 1 : 0, player.getUniqueId());
        }else{
            query = String.format("UPDATE %s SET hasSession='%d' WHERE minecraftName='%s'", this.getTableName(), sessionState ? 1 : 0, player.getName());
        }
        return this.getDatabase().executeUpdateQuery(query);
    }

    /**
     * Check player password
     * @param player Player
     * @param encryptedPassword Player encrypted password
     * @return Xen0Lib Status: Valid, Invalid, NotExist, SQLError
     */
    public Status checkPassword(Player player, String encryptedPassword){
        String query;
        if(DiscordAuth.getConfiguration().getPremium()){
            query = String.format("SELECT password FROM %s WHERE UUID='%s'", this.getTableName(), player.getUniqueId());
        }else{
            query = String.format("SELECT password FROM %s WHERE minecraftName='%s'", this.getTableName(), player.getName());
        }
        ResultSet rs = this.getDatabase().executeQuery(query);
        try {
            if(rs.next()){
                if(rs.getString("password").equals(encryptedPassword)){
                    return Status.Valid;
                }else{
                    return Status.Invalid;
                }
            }else{
                return Status.NotExist;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Status.SQLError;
    }

    /**
     * Add account
     * @param uuid Player uuid
     * @param minecraftName Player name
     * @param discordId Player discord id
     * @param encryptedPassword Player encrypted password
     * @return Xen0Lib Status: Success, SQLError, Exist
     */
    public Status addAccount(UUID uuid, String minecraftName, long discordId, String encryptedPassword){
        if(uuid == null){
            if(discordId != 0){
                if(this.isDiscordUserExist(discordId) == Status.NotExist){
                    String query = String.format("INSERT INTO %s VALUES (NULL, '', '%s', %d, '%s', '', NULL, NULL)", this.getTableName(), minecraftName, discordId, encryptedPassword);
                    return this.getDatabase().executeUpdateQuery(query);
                }else{
                    return Status.Exist;
                }
            }else{
                String query = String.format("INSERT INTO %s VALUES (NULL, '', '%s', %d, '%s', '', NULL, NULL)", this.getTableName(), minecraftName, discordId, encryptedPassword);
                return this.getDatabase().executeUpdateQuery(query);
            }
        }
        if(discordId != 0){
            if(this.isDiscordUserExist(discordId) == Status.NotExist){
                String query = String.format("INSERT INTO %s VALUES (NULL, '%s', '%s', %d, '%s', '', NULL, NULL)", this.getTableName(), uuid, minecraftName, discordId, encryptedPassword);
                return this.getDatabase().executeUpdateQuery(query);
            }else{
                return Status.Exist;
            }
        }else{
            String query = String.format("INSERT INTO %s VALUES (NULL, '%s', '%s', %d, '%s', '', NULL, NULL)", this.getTableName(), uuid, minecraftName, discordId, encryptedPassword);
            return this.getDatabase().executeUpdateQuery(query);
        }
    }

    /**
     * Set player session
     * @param discordId player's discord id
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status deleteAccount(long discordId){
        String query = String.format("DELETE FROM %s WHERE discordId='%d'", this.getTableName(), discordId);
        return this.getDatabase().executeUpdateQuery(query);
    }

    /**
     * Set player session
     * @param discordId Account's discord ID
     * @param newPassword New Player password
     * @return Xen0Lib Status: Success, SQLError
     */
    public Status setPassword(long discordId, String newPassword){
        String query;
        query = String.format("UPDATE %s SET password='%s' WHERE discordId='%s'", this.getTableName(), newPassword, discordId);
        return this.getDatabase().executeUpdateQuery(query);
    }

    public List<Long> getDbIds(){
        List<Long> ids = new ArrayList<>();
        String query = String.format("SELECT discordId FROM %s", this.getTableName());
        ResultSet rs = this.getDatabase().executeQuery(query);
        try{
            while (rs.next()){
                ids.add(rs.getLong("discordId"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ids;
    }

}
