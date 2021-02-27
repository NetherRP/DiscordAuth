package fr.xen0xys.discordauth.databases;

import fr.xen0xys.discordauth.DiscordAuth;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsDatabase {

    private final String account_table_name = "netherrpv2_accounts";
    private final DatabaseProvider database_provider;

    public AccountsDatabase() {
        database_provider = DiscordAuth.getDatabaseProvider();
    }

    public AccountsDatabase(DatabaseProvider database_provider) {
        this.database_provider = database_provider;
    }

    public void initializeTableIfNotInitialized() {
        if (!this.database_provider.isTableInitialised(this.account_table_name)) {
            this.database_provider.initializeTable(this.account_table_name, "id INT PRIMARY KEY AUTO_INCREMENT UNIQUE, discord_id BIGINT NOT NULL, minecraft_name VARCHAR(30) UNIQUE, is_spectator SMALLINT");
        }
    }

    public int getIdFromMinecraftName(String minecraft_name){
        return this.database_provider.getId(this.account_table_name, "minecraft_name", minecraft_name);
    }

    public String getMinecraftNameFromDiscordId(long discord_id){
        try{
            String query = String.format("SELECT minecraft_name FROM %s WHERE discord_id=%d", this.account_table_name, discord_id);
            ResultSet rs = this.database_provider.executeQuery(query);
            if(rs.next()){
                return rs.getString("minecraft_name");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Long getDiscordIdFromMinecraftName(String minecraft_name) {
        try{
            String query = String.format("SELECT discord_id FROM %s WHERE minecraft_name='%s'", this.account_table_name, minecraft_name);
            ResultSet rs = this.database_provider.executeQuery(query);
            if(rs.next()){
                return rs.getLong("discord_id");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean changeUsername(String minecraft_name, String new_minecraft_name){
        this.initializeTableIfNotInitialized();
        if(this.isMinecraftNameExist(minecraft_name) && !this.isMinecraftNameExist(new_minecraft_name)){
            String query = String.format("UPDATE %s SET minecraft_name='%s' WHERE minecraft_name='%s'", this.account_table_name, new_minecraft_name, minecraft_name);
            return this.database_provider.executeUpdateQuery(query);
        }
        return false;
    }

    public boolean createPlayerAccount(long discord_id, String minecraft_name, String password) {
        return this.createAccount(discord_id, minecraft_name, password, false);
    }

    private boolean createAccount(long discord_id, String minecraft_name, String password, boolean is_spectator){
        this.initializeTableIfNotInitialized();
        if((is_spectator && this.isDiscordUserHasSpectatorAccount(discord_id)) || (is_spectator && !this.isDiscordUserHasPlayerAccount(discord_id))){
            System.out.println("Spec account: " + this.isDiscordUserHasSpectatorAccount(discord_id));
            System.out.println("Player account: " + this.isDiscordUserHasPlayerAccount(discord_id));
            return false;
        }
        if(!is_spectator && this.isDiscordUserHasPlayerAccount(discord_id)){
            return false;
        }
        if (!this.isMinecraftNameExist(minecraft_name)) {
            String query = String.format("INSERT INTO %s VALUES (NULL, ?, '%s', ?);", this.account_table_name, minecraft_name);
            try {
                PreparedStatement ps = this.database_provider.getPreparedStatement(query);
                ps.setLong(1, discord_id);
                if(is_spectator){
                    ps.setInt(2, 1);
                }else{
                    ps.setInt(2, 0);
                }
                ps.executeUpdate();
                // Set password
                new SecurityDatabase().setPassword(minecraft_name, password);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Check if the given discord id already figure in the database
    public boolean isDiscordUserExist(long discord_id) {
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT discord_id FROM %s;", this.account_table_name);
        ResultSet rs = this.database_provider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    long _discord_id = rs.getLong("discord_id");
                    if (_discord_id == discord_id) {
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
    public boolean isUsernameSpectatorAccount(String minecraft_name){
        try{
            String query = String.format("SELECT is_spectator FROM %s WHERE minecraft_name='%s'", this.account_table_name, minecraft_name);
            ResultSet rs = this.database_provider.executeQuery(query);
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
    public boolean isMinecraftNameExist(String minecraft_name) {
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT minecraft_name FROM %s;", this.account_table_name);
        ResultSet rs = this.database_provider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    String _minecraft_name = rs.getString("minecraft_name");
                    if (_minecraft_name.equals(minecraft_name)) {
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
    public boolean isDiscordUserHasSpectatorAccount(long discord_id){
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT is_spectator FROM %s WHERE discord_id=%d;", this.account_table_name, discord_id);
        ResultSet rs = this.database_provider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    int is_spectator = rs.getInt("is_spectator");
                    if(is_spectator == 1){
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
    public boolean isDiscordUserHasPlayerAccount(long discord_id){
        this.initializeTableIfNotInitialized();
        String query = String.format("SELECT is_spectator FROM %s WHERE discord_id=%d;", this.account_table_name, discord_id);
        ResultSet rs = this.database_provider.executeQuery(query);
        try {
            if (rs != null) {
                while (rs.next()) {
                    int is_spectator = rs.getInt("is_spectator");
                    if(is_spectator == 0){
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
