package fr.xen0xys.discordauth.common.config;

import fr.xen0xys.discordauth.common.database.DatabaseType;
import fr.xen0xys.discordauth.common.database.HibernateConfig;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CoreConfig extends ConfigurationReader{

    public CoreConfig(@NotNull File dataFolder) {
        super(dataFolder, "configs/core.yml");
    }

    public SessionFactory getSessionFactory(){
        DatabaseType databaseType = DatabaseType.from(this.getConfiguration().getString("database.type"));
        String host = this.getConfiguration().getString("database.host");
        String port = this.getConfiguration().getString("database.port");
        String username = this.getConfiguration().getString("database.username");
        String password = this.getConfiguration().getString("database.password");
        String database;
        if(databaseType == DatabaseType.SQLITE){
            database = this.getDataFolder() + "/" + this.getConfiguration().getString("database.database");
        }else{
            database = this.getConfiguration().getString("database.database");
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

    public long getSessionDuration(){
        return this.getConfiguration().getLong("session_duration");
    }

    public String getBotToken() {
        return this.getConfiguration().getString("discord.token");
    }

    public boolean isActivityEnable(){
        return this.getConfiguration().getBoolean("discord.activity.enable");
    }
    public String getActivityType(){
        return this.getConfiguration().getString("discord.activity.type");
    }
    public String getActivityText(){
        return this.getConfiguration().getString("discord.activity.text");
    }
    public String getActivityUrl(){
        return this.getConfiguration().getString("discord.activity.url");
    }
}
