package fr.xen0xys.discordauth.waterfall;

import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.config.CommonConfig;
import fr.xen0xys.discordauth.common.config.CoreConfig;
import fr.xen0xys.discordauth.common.config.language.LangConfig;
import fr.xen0xys.discordauth.common.database.DatabaseHandler;
import fr.xen0xys.discordauth.common.discord.Bot;
import fr.xen0xys.discordauth.common.logging.ConsoleFilter;
import fr.xen0xys.discordauth.waterfall.events.OnLogin;
import fr.xen0xys.discordauth.waterfall.events.OnPlayerDisconnect;
import fr.xen0xys.discordauth.waterfall.events.OnPluginMessage;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordAuthProxy extends Plugin {

    private static DiscordAuthProxy instance;
    private static CommonConfig commonConfig;
    private static CoreConfig coreConfig;
    private static DatabaseHandler databaseHandler;

    private static final Set<UUID> sessions = new HashSet<>();

    @Override
    public void onLoad() {
        instance = this;
        Logger logger = this.getLogger();
        logger.info("Loading configs...");
        commonConfig = new CommonConfig(this.getDataFolder());
        coreConfig = new CoreConfig(this.getDataFolder());
        new LangConfig(this.getDataFolder(), commonConfig.getLanguage());
        logger.info("Loading database...");
        databaseHandler = new DatabaseHandler(coreConfig.getSessionFactory());
        logger.info("Registering channels...");
        this.registerBungeeCordChannels();
        logger.info("Registering events...");
        this.registerEvents();
        logger.info("Loading Discord bot...");
        new Bot(coreConfig, logger);
        logger.info("Creating logging filter...");
        org.apache.logging.log4j.core.Logger filteredLogger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        filteredLogger.addFilter(new ConsoleFilter());
        logger.info("DiscordAuth is loaded !");
    }

    private void registerBungeeCordChannels(){
        this.getProxy().registerChannel(PluginInfos.CHANNEL);
    }

    private void registerEvents(){
        getProxy().getPluginManager().registerListener(this, new OnPluginMessage());
        getProxy().getPluginManager().registerListener(this, new OnLogin());
        getProxy().getPluginManager().registerListener(this, new OnPlayerDisconnect());
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
