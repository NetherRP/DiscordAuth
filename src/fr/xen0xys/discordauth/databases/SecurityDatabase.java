package fr.xen0xys.discordauth.databases;

import fr.xen0xys.discordauth.utils.PluginUtils;
import fr.xen0xys.discordauth.DiscordAuth;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SecurityDatabase {

    private final String account_table_name = "netherrpv2_accounts_security";
    private final DatabaseProvider database_provider;
    private final int session_length = 60 * 60 * 1000;

    public SecurityDatabase(){
        database_provider = DiscordAuth.getDatabaseProvider();
    }

    public SecurityDatabase(DatabaseProvider database_provider) {
        this.database_provider = database_provider;
    }


    public void initializeTableIfNotInitialized(){
        if(!this.database_provider.isTableInitialised(this.account_table_name)){
            this.database_provider.initializeTable(this.account_table_name, "id INT PRIMARY KEY AUTO_INCREMENT, netherrpv2_account_id INT, password VARCHAR(100), ip VARCHAR(100), last_login BIGINT, has_session INT, FOREIGN KEY (netherrpv2_account_id) REFERENCES netherrpv2_accounts(id) ON DELETE CASCADE ON UPDATE NO ACTION");
        }
    }

    public boolean setPassword(String minecraft_name, String new_password){
        this.initializeTableIfNotInitialized();
        String encrypted_password = PluginUtils.encryptString(new_password);
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraft_name);
        if(id != -1){
            try{
                String query = String.format("SELECT id FROM %s WHERE netherrpv2_account_id='%s'", this.account_table_name, id);
                ResultSet rs = this.database_provider.executeQuery(query);
                if(rs.next()){
                    int user_security_id = rs.getInt("id");
                    // Encrypt and update password
                    query = String.format("UPDATE %s SET password='%s' WHERE id='%d'", this.account_table_name, encrypted_password, id);
                }else{
                    // Create security account with provided password
                    query = String.format("INSERT INTO %s VALUES (NULL, %d, '%s', NULL, NULL, 0);", this.account_table_name, id, encrypted_password);
                }
                return this.database_provider.executeUpdateQuery(query);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean checkPassword(String minecraft_name, String password){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraft_name);
        if(id != -1){
            try {
                String encrypted_password = PluginUtils.encryptString(password);
                String query = String.format("SELECT password FROM %s WHERE netherrpv2_account_id='%d'", this.account_table_name, id);
                ResultSet rs = this.database_provider.executeQuery(query);
                if(rs.next()){
                    String db_password = rs.getString("password");
                    if(db_password.equals(encrypted_password)){
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean loginUser(String minecraft_name, String ip){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraft_name);
        if(id != -1){
            String query = String.format("UPDATE %s SET ip='%s', last_login='%s', has_session=1 WHERE netherrpv2_account_id='%d'", this.account_table_name, ip, PluginUtils.getCurrentTimestamp(), id);
            return this.database_provider.executeUpdateQuery(query);
        }
        return false;
    }

    public boolean logoutUser(String minecraft_name){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraft_name);
        if(id != -1){
            String query = String.format("UPDATE %s SET has_session=0 WHERE netherrpv2_account_id='%d'", this.account_table_name, id);
            return this.database_provider.executeUpdateQuery(query);
        }
        return false;
    }

    public boolean hasSession(String minecraft_name, String ip){
        int id = new AccountsDatabase().getIdFromMinecraftName(minecraft_name);
        if(id != -1){
            try {
                String query = String.format("SELECT last_login, ip, has_session FROM %s WHERE netherrpv2_account_id='%d'", this.account_table_name, id);
                ResultSet rs = this.database_provider.executeQuery(query);
                if(rs.next()){
                    String _ip = rs.getString("ip");
                    long last_login = rs.getLong("last_login");
                    if(ip.equals(_ip) && last_login + this.session_length >= PluginUtils.getCurrentTimestamp()){
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
