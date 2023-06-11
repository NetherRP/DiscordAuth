package fr.xen0xys.discordauth.waterfall;

import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.config.Configuration;
import fr.xen0xys.discordauth.common.database.DatabaseHandler;
import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.waterfall.events.OnPluginMessage;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;
import java.util.logging.Logger;

public class DiscordAuthProxy extends Plugin {

    private static DiscordAuthProxy instance;
    private static Logger logger;
    private static Configuration configuration;
    private static DatabaseHandler databaseHandler;

    @Override
    public void onLoad() {
        instance = this;
        logger = this.getLogger();
        configuration = new Configuration(this.getDataFolder(), "config.yml");
        databaseHandler = new DatabaseHandler(configuration.getSessionFactory());
    }

    @Override
    public void onEnable() {
        this.getProxy().registerChannel(PluginInfos.CHANNEL);
        getProxy().getPluginManager().registerListener(this, new OnPluginMessage());
        logger.info("DiscordAuth is started !");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static DiscordAuthProxy getInstance() {
        return instance;
    }
    public static Configuration getConfiguration() {
        return configuration;
    }
    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }
}
