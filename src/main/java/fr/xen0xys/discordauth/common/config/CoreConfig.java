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
        DatabaseType databaseType = DatabaseType.from(this.getValue(String.class, "database.type", "SQLITE"));
        String host = this.getValue(String.class, "database.host", "localhost");
        String port = this.getValue(Integer.class, "database.port", 3306).toString();
        String username = this.getValue(String.class, "database.username", "root");
        String password = this.getValue(String.class, "database.password", "password");
        String database;
        if(databaseType == DatabaseType.SQLITE)
            database = this.getDataFolder() + "/" + this.getValue(String.class, "database.database", "database");
        else
            database = this.getValue(String.class, "database.database", "database");
        return new HibernateConfig()
                .setDatabaseType(databaseType)
                .setHost(host)
                .setPort(port)
                .setDatabase(database)
                .setUsername(username)
                .setPassword(password)
                .getSessionFactory();
    }

    public int getSessionDuration(){
        return this.getValue(Integer.class, "session_duration", 3600);
    }

    public String getBotToken() {
        return this.getValue(String.class, "discord.token", "token");
    }

    public long getGuildId(){
        return this.getValue(Long.class, "discord.guild_id", 0L);
    }

    public boolean isUsernameChangingEnable(){
        return this.getValue(Boolean.class, "discord.change_username", true);
    }

    public boolean isActivityEnable(){
        return this.getValue(Boolean.class, "discord.activity.enable", true);
    }
    public String getActivityType(){
        return this.getValue(String.class, "discord.activity.type", "PLAYING");
    }
    public String getActivityText(){
        return this.getValue(String.class, "discord.activity.text", "DiscordAuth v4.0.0-beta");
    }
    public String getActivityUrl(){
        return this.getValue(String.class, "discord.activity.url", "null");
    }
}
