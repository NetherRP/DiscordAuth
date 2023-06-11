package fr.xen0xys.discordauth.common.config;

import fr.xen0xys.discordauth.common.database.DatabaseType;
import fr.xen0xys.discordauth.common.database.HibernateConfig;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Configuration extends ConfigurationReader{
    public Configuration(@NotNull File dataFolder, @NotNull String configName) {
        super(dataFolder, configName);
    }

    public String getSecret(){
        return this.getConfiguration().getString("both.secret");
    }

    public SessionFactory getSessionFactory(){
        DatabaseType databaseType = DatabaseType.from(this.getConfiguration().getString("core.database.type"));
        String host = this.getConfiguration().getString("core.database.host");
        String port = this.getConfiguration().getString("core.database.port");
        String username = this.getConfiguration().getString("core.database.username");
        String password = this.getConfiguration().getString("core.database.password");
        String database;
        if(databaseType == DatabaseType.SQLITE){
            database = this.getDataFolder() + "/" + this.getConfiguration().getString("core.database.database");
        }else{
            database = this.getConfiguration().getString("core.database.database");
        }
        return new HibernateConfig()
                .setDatabaseType(databaseType)
                .setHost(host)
                .setPort(port)
                .setDatabase(database)
                .setUsername(username)
                .setPassword(password)
                .getSessionFactory();
    }

    public String getBotToken() {
        return this.getConfiguration().getString("core.discord.token");
    }
}
