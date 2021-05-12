package fr.xen0xys.discordauth.databases;

import fr.xen0xys.discordauth.DiscordAuth;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsDatabase {

    private final String accountTableName = "discordauth_accounts";
    private final DatabaseProvider databaseProvider;

    public AccountsDatabase() {
        databaseProvider = DiscordAuth.getDatabaseProvider();
    }

    public AccountsDatabase(DatabaseProvider databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    public void initializeTableIfNotInitialized() {
        if (!this.databaseProvider.isTableInitialised(this.accountTableName)) {
            this.databaseProvider.initializeTable(this.accountTableName, "id INT PRIMARY KEY AUTO_INCREMENT UNIQUE, discord_id BIGINT NOT NULL, minecraft_name VARCHAR(30) UNIQUE, is_spectator SMALLINT");
        }
    }

    public int getIdFromMinecraftName(String minecraftName){
        return this.databaseProvider.getId(this.accountTableName, "minecraft_name", minecraftName);
    }

    public String getMinecraftNameFromDiscordId(long discordId){
        try{
            String query = String.format("SELECT minecraft_name FROM %s WHERE discord_id=%d", this.accountTableName, discordId);
            ResultSet rs = this.databaseProvider.executeQuery(query);
            if(rs.next()){
                return rs.getString("minecraft_name");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Long getDiscordIdFromMinecraftName(String minecraftName) {
        try{
            String query = String.format("SELECT discord_id FROM %s WHERE minecraft_name='%s'", this.accountTableName, minecraftName);
            ResultSet rs = this.databaseProvider.executeQuery(query);
            if(rs.next()){
                return rs.getLong("discord_id");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean changeUsername(String minecraftName, String newMinecraftName){
        this.initializeTableIfNotInitialized();
        if(this.isMinecraftNameExist(minecraftName) && !this.isMinecraftNameExist(newMinecraftName)){
            String query = String.format("UPDATE %s SET minecraft_name='%s' WHERE minecraft_name='%s'", this.accountTableName, newMinecraftName, minecraftName);
            return this.databaseProvider.executeUpdateQuery(query);
        }
        return false;
    }

    public boolean createPlayerAccount(long discordId, String minecraftName, String password) {
        return this.createAccount(discordId, minecraftName, password, false);
    }

    private boolean createAccount(long discordId, String minecraftName, String password, boolean isSpectator){
        this.initializeTableIfNotInitialized();
        if((isSpectator && this.isDiscordUserHasSpectatorAccount(discordId)) || (isSpectator && !this.isDiscordUserHasPlayerAccount(discordId))){
            System.out.println("Spec account: " + this.isDiscordUserHasSpectatorAccount(discordId));
            System.out.println("Player account: " + this.isDiscordUserHasPlayerAccount(discordId));
            return false;
        }
        if(!isSpectator && this.isDiscordUserHasPlayerAccount(discordId)){
            return false;
        }
        if (!this.isMinecraftNameExist(minecraftName)) {
            String query = String.format("INSERT INTO %s VALUES (NULL, ?, '%s', ?);", this.accountTableName, minecraftName);
            try {
                PreparedStatement ps = this.databaseProvider.getPreparedStatement(query);
                ps.setLong(1, discordId);
                if(isSpectator){
                    ps.setInt(2, 1);
                }else{
                    ps.setInt(2, 0);
                }
                ps.executeUpdate();
                // Set password
                new SecurityDatabase().setPassword(minecraftName, password);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Check if the given discord id already figure in the database
    public boolean isDiscordUserExist(long discordId) {
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT discord_id FROM %s;", this.accountTableName);
        ResultSet rs = this.databaseProvider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    long localDiscordId = rs.getLong("discord_id");
                    if (localDiscordId == discordId) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if given username is a spectator account
    public boolean isUsernameSpectatorAccount(String minecraftName){
        try{
            String query = String.format("SELECT is_spectator FROM %s WHERE minecraft_name='%s'", this.accountTableName, minecraftName);
            ResultSet rs = this.databaseProvider.executeQuery(query);
            if(rs.next()){
                if(rs.getInt("is_spectator") == 1){
                    return true;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Check if the given minecraft username already exist or not
    public boolean isMinecraftNameExist(String minecraftName) {
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT minecraft_name FROM %s;", this.accountTableName);
        ResultSet rs = this.databaseProvider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    String localMinecraftName = rs.getString("minecraft_name");
                    if (localMinecraftName.equals(minecraftName)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if giver discord id has corresponding spectator account
    public boolean isDiscordUserHasSpectatorAccount(long discordId){
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT is_spectator FROM %s WHERE discord_id=%d;", this.accountTableName, discordId);
        ResultSet rs = this.databaseProvider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    int isSpectator = rs.getInt("is_spectator");
                    if(isSpectator == 1){
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if giver discord id has corresponding player account
    public boolean isDiscordUserHasPlayerAccount(long discordId){
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT is_spectator FROM %s WHERE discord_id=%d;", this.accountTableName, discordId);
        ResultSet rs = this.databaseProvider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    int isSpectator = rs.getInt("is_spectator");
                    if(isSpectator == 0){
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
