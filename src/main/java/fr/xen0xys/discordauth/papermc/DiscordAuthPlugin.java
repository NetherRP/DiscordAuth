package fr.xen0xys.discordauth.papermc;

import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.config.CommonConfig;
import fr.xen0xys.discordauth.common.config.ServerConfig;
import fr.xen0xys.discordauth.common.config.language.LangConfig;
import fr.xen0xys.discordauth.papermc.commands.completers.AccountCompleter;
import fr.xen0xys.discordauth.papermc.commands.completers.DiscordAuthCompleter;
import fr.xen0xys.discordauth.papermc.commands.executors.*;
import fr.xen0xys.discordauth.papermc.events.*;
import fr.xen0xys.discordauth.common.logging.ConsoleFilter;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class DiscordAuthPlugin extends JavaPlugin {

    private static DiscordAuthPlugin instance;
    private static Logger logger;
    private static CommonConfig commonConfig;
    private static ServerConfig serverConfig;
    private static final Map<UUID, Location> unauthenticatedPlayers = new HashMap<>();

    @Override
    public void onLoad() {
        instance = this;
        logger = this.getLogger();
        logger.info("Loading configs...");
        commonConfig = new CommonConfig(this.getDataFolder());
        serverConfig = new ServerConfig(this.getDataFolder());
        new LangConfig(this.getDataFolder(), commonConfig.getLanguage());
        logger.info("Registering channels...");
        this.registerBungeeCordChannels();
        logger.info("Creating logging filter...");
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter());
        logger.info("DiscordAuth is loaded !");
    }

    @Override
    public void onEnable() {
        logger.info("Registering events...");
        this.registerEvents();
        logger.info("Registering commands...");
        this.registerCommands();
        logger.info("DiscordAuth is enabled !");
    }

    private void registerBungeeCordChannels(){
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginInfos.CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PluginInfos.CHANNEL, new OnPluginMessage());
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        this.getServer().getPluginManager().registerEvents(new OnPreventions(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
    }

    private void registerCommands(){
        this.getCommand("login").setExecutor(new LoginCommand());
        this.getCommand("logout").setExecutor(new LogoutCommand());
        this.getCommand("forcelogin").setExecutor(new ForceLoginCommand());
        this.getCommand("discordauth").setExecutor(new DiscordAuthCommand());
        this.getCommand("discordauth").setTabCompleter(new DiscordAuthCompleter());
        this.getCommand("account").setExecutor(new AccountCommand());
        this.getCommand("account").setTabCompleter(new AccountCompleter());
    }

    public static DiscordAuthPlugin getInstance() {
        return instance;
    }
    public static CommonConfig getCommonConfig() {
        return commonConfig;
    }
    public static ServerConfig getServerConfig() {
        return serverConfig;
    }
    public static Map<UUID, Location> getUnauthenticatedPlayers() {
        return unauthenticatedPlayers;
    }
}
