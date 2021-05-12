package fr.xen0xys.discordauth.databases;

import fr.xen0xys.discordauth.models.ConfigurationManager;
import fr.xen0xys.discordauth.DiscordAuth;

import java.sql.*;

public class DatabaseProvider {

    private Connection connection;

    public DatabaseProvider(){
        initializeConnection();
        new AccountsDatabase(this).initializeTableIfNotInitialized();
        new SecurityDatabase(this).initializeTableIfNotInitialized();
    }

    private void initializeConnection(){
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
            ConfigurationManager config = DiscordAuth.getConfigurationManager();
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", config.getHost(), config.getPort(), config.getDatabase()), config.getUser(), config.getPassword());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void reconnect(){
        initializeConnection();
    }

    public boolean isTableInitialised(String tableName) {
        try{
            DatabaseMetaData dbm = this.connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        } catch(SQLException e){
            e.printStackTrace();
            this.reconnect();
        }
        return false;
    }

    public boolean initializeTable(String tableName, String tableValues){
        String query = String.format("CREATE TABLE %s (%s);", tableName, tableValues);
        return executeUpdateQuery(query);
    }

    public boolean executeUpdateQuery(String query){
        try {
            Statement statement = getStatement();
            if(statement != null) {
                statement.executeUpdate(query);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.reconnect();
        }
        return false;
    }

    public ResultSet executeQuery(String query){
        try{
            Statement statement = this.getStatement();
            if(statement != null){
                return statement.executeQuery(query);
            }
        } catch(SQLException e){
            e.printStackTrace();
            this.reconnect();
        }
        return null;
    }

    public PreparedStatement getPreparedStatement(String query){
        try {
            return this.connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean executeUpdatePreparedStatement(PreparedStatement ps){
        try {
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Statement getStatement(){
        try {
            return this.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            this.reconnect();
        }
        return null;
    }

    public int getId(String tableName, String columnName, String targetValue){
        if(!isTableInitialised(tableName)){
            return -1;
        }
        String query = String.format("SELECT id FROM %s WHERE %s='%s';", tableName, columnName, targetValue);
        ResultSet rs = this.executeQuery(query);
        try{
            if(rs != null){
                if(rs.next()){
                    return rs.getInt("id");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
}
