package fr.xen0xys.discordauth.papermc;

import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.config.CommonConfig;
import fr.xen0xys.discordauth.common.config.ServerConfig;
import fr.xen0xys.discordauth.papermc.commands.completers.AccountCompleter;
import fr.xen0xys.discordauth.papermc.commands.completers.DiscordAuthCompleter;
import fr.xen0xys.discordauth.papermc.commands.executors.AccountCommand;
import fr.xen0xys.discordauth.papermc.commands.executors.DiscordAuthCommand;
import fr.xen0xys.discordauth.papermc.commands.executors.LoginCommand;
import fr.xen0xys.discordauth.papermc.commands.executors.LogoutCommand;
import fr.xen0xys.discordauth.papermc.events.*;
import fr.xen0xys.discordauth.papermc.models.ConsoleFilter;
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
        commonConfig = new CommonConfig(this.getDataFolder());
        serverConfig = new ServerConfig(this.getDataFolder());
        // Add logging filter
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter());
    }

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginInfos.CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PluginInfos.CHANNEL, new OnPluginMessage());
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        this.getServer().getPluginManager().registerEvents(new OnPreventions(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
        this.getCommand("login").setExecutor(new LoginCommand());
        this.getCommand("logout").setExecutor(new LogoutCommand());
        this.getCommand("discordauth").setExecutor(new DiscordAuthCommand());
        this.getCommand("discordauth").setTabCompleter(new DiscordAuthCompleter());
        this.getCommand("account").setExecutor(new AccountCommand());
        this.getCommand("account").setTabCompleter(new AccountCompleter());
        logger.info("DiscordAuth is started !");
    }

    @Override
    public void onDisable() {
        super.onDisable();
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
