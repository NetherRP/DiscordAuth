package fr.xen0xys.discordauth.databases;

import fr.xen0xys.discordauth.utils.PluginUtils;
import fr.xen0xys.discordauth.DiscordAuth;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SecurityDatabase {

    private final String accountTableName = "discordauth_accounts_security";
    private final DatabaseProvider databaseProvider;
    private final int sessionLength = 60 * 60 * 1000;

    public SecurityDatabase(){
        databaseProvider = DiscordAuth.getDatabaseProvider();
    }

    public SecurityDatabase(DatabaseProvider databaseProvider) {
        this.databaseProvider = databaseProvider;
    }


    public void initializeTableIfNotInitialized(){
        if(!this.databaseProvider.isTableInitialised(this.accountTableName)){
            this.databaseProvider.initializeTable(this.accountTableName, "id INT PRIMARY KEY AUTO_INCREMENT, discordauth_account_id INT, password VARCHAR(100), ip VARCHAR(100), last_login BIGINT, has_session INT, FOREIGN KEY (discordauth_account_id) REFERENCES discordauth_accounts(id) ON DELETE CASCADE ON UPDATE NO ACTION");
        }
    }

    public boolean setPassword(String minecraftName, String newPassword){
        this.initializeTableIfNotInitialized();
        String encryptedPassword = PluginUtils.encryptString(newPassword);
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraftName);
        if(id != -1){
            try{
                String query = String.format("SELECT id FROM %s WHERE discordauth_account_id='%s'", this.accountTableName, id);
                ResultSet rs = this.databaseProvider.executeQuery(query);
                if(rs.next()){
                    // Encrypt and update password
                    query = String.format("UPDATE %s SET password='%s' WHERE id='%d'", this.accountTableName, encryptedPassword, id);
                }else{
                    // Create security account with provided password
                    query = String.format("INSERT INTO %s VALUES (NULL, %d, '%s', NULL, NULL, 0);", this.accountTableName, id, encryptedPassword);
                }
                return this.databaseProvider.executeUpdateQuery(query);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean checkPassword(String minecraftName, String password){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraftName);
        if(id != -1){
            try {
                String encryptedPassword = PluginUtils.encryptString(password);
                String query = String.format("SELECT password FROM %s WHERE discordauth_account_id='%d'", this.accountTableName, id);
                ResultSet rs = this.databaseProvider.executeQuery(query);
                if(rs.next()){
                    String remotePassword = rs.getString("password");
                    if(remotePassword.equals(encryptedPassword)){
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean loginUser(String minecraftName, String ip){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraftName);
        if(id != -1){
            String query = String.format("UPDATE %s SET ip='%s', last_login='%s', has_session=1 WHERE discordauth_account_id='%d'", this.accountTableName, ip, PluginUtils.getCurrentTimestamp(), id);
            return this.databaseProvider.executeUpdateQuery(query);
        }
        return false;
    }

    public boolean logoutUser(String minecraftName){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraftName);
        if(id != -1){
            String query = String.format("UPDATE %s SET has_session=0 WHERE discordauth_account_id='%d'", this.accountTableName, id);
            return this.databaseProvider.executeUpdateQuery(query);
        }
        return false;
    }

    public boolean hasSession(String minecraftName, String ip){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraftName);
        if(id != -1){
            try {
                String query = String.format("SELECT last_login, ip, has_session FROM %s WHERE discordauth_account_id='%d'", this.accountTableName, id);
                ResultSet rs = this.databaseProvider.executeQuery(query);
                if(rs.next()){
                    String _ip = rs.getString("ip");
                    long last_login = rs.getLong("last_login");
                    if(ip.equals(_ip) && last_login + this.sessionLength >= PluginUtils.getCurrentTimestamp()){
                        if(rs.getInt("has_session") == 1){
                            return true;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
