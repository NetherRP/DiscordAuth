package fr.xen0xys.discordauth.waterfall;

import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.config.CommonConfig;
import fr.xen0xys.discordauth.common.config.CoreConfig;
import fr.xen0xys.discordauth.common.database.DatabaseHandler;
import fr.xen0xys.discordauth.common.discord.Bot;
import fr.xen0xys.discordauth.waterfall.events.OnLogin;
import fr.xen0xys.discordauth.waterfall.events.OnPlayerDisconnect;
import fr.xen0xys.discordauth.waterfall.events.OnPluginMessage;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class DiscordAuthProxy extends Plugin {

    private static DiscordAuthProxy instance;
    private static Logger logger;
    private static CommonConfig commonConfig;
    private static CoreConfig coreConfig;
    private static DatabaseHandler databaseHandler;

    private static final Set<UUID> sessions = new HashSet<>();

    @Override
    public void onLoad() {
        instance = this;
        logger = this.getLogger();
        commonConfig = new CommonConfig(this.getDataFolder());
        coreConfig = new CoreConfig(this.getDataFolder());
        databaseHandler = new DatabaseHandler(coreConfig.getSessionFactory());
        new Bot(coreConfig, logger);
    }

    @Override
    public void onEnable() {
        this.getProxy().registerChannel(PluginInfos.CHANNEL);
        getProxy().getPluginManager().registerListener(this, new OnPluginMessage());
        getProxy().getPluginManager().registerListener(this, new OnLogin());
        getProxy().getPluginManager().registerListener(this, new OnPlayerDisconnect());
        logger.info("DiscordAuth is started !");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static DiscordAuthProxy getInstance() {
        return instance;
    }

    public static CommonConfig getCommonConfig() {
        return commonConfig;
    }
    public static CoreConfig getCoreConfig() {
        return coreConfig;
    }
    public static DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public static Set<UUID> getSessions() {
        return sessions;
    }
}
