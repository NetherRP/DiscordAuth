package fr.xen0xys.discordauth;

import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.bot.botevents.*;
import fr.xen0xys.discordauth.bstats.Metrics;
import fr.xen0xys.discordauth.databases.*;
import fr.xen0xys.discordauth.models.*;
import fr.xen0xys.discordauth.plugin.commands.accounts.*;
import fr.xen0xys.discordauth.plugin.events.*;
import fr.xen0xys.discordauth.utils.ConsoleFilter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class DiscordAuth extends JavaPlugin {
    private static long messageId = 0L;
    private static long guildId = 0L;
    private static long channelId = 0L;
    private static JDA bot;
    private static SkinManager skinManager;
    private static final String LOG_PREFIX = "[DiscordAuth]: ";
    private static DatabaseProvider databaseProvider;
    private static final HashMap<String, User> USERS = new HashMap<>();
    private static PluginAsyncLoop pluginAsyncLoop;
    private static ConfigurationManager configurationManager;
    private static Location spawnLocation;

    @Override
    public void onLoad() {
        System.out.println(ChatColor.GREEN + LOG_PREFIX + "Loading plugin");
        configurationManager = new ConfigurationManager(this);
        try {
            buildBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        databaseProvider = new DatabaseProvider();
        super.onLoad();
        System.out.println(ChatColor.GREEN + LOG_PREFIX + "Plugin loaded");
    }

    @Override
    public void onDisable() {
        System.out.println(ChatColor.GREEN + LOG_PREFIX + "Disabling plugin");
        pluginAsyncLoop.stop();
        stopBot();
        super.onDisable();
        System.out.println(ChatColor.GREEN + LOG_PREFIX + "Plugin disabled");
    }

    @Override
    public void onEnable() {
        System.out.println(ChatColor.GREEN + LOG_PREFIX + "Enabling plugin");
        registerEvents();
        registerCommands();
        registerFilters();
        skinManager = new SkinManager();
        pluginAsyncLoop = new PluginAsyncLoop();
        pluginAsyncLoop.runTaskAsynchronously(this);
        spawnLocation = configurationManager.getSpawnPoint();
        this.initializeWorlds();
        super.onEnable();
        // Enable bstats
        int pluginId = 10497;
        new Metrics(this, pluginId);
        System.out.println(ChatColor.GREEN + LOG_PREFIX + "Plugin enabled");
    }

    private void buildBot() throws LoginException {
        messageId = configurationManager.getMessageId();
        guildId = configurationManager.getGuildId();
        channelId = configurationManager.getChannelId();
        bot = JDABuilder.createDefault(configurationManager.getBotToken()).build();
        registerBotEvents();
    }
    private void stopBot(){
        bot.getRegisteredListeners().forEach(bot::removeEventListener);
        BotUtils.sendMessage(BotUtils.getServerStopMessage());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bot.shutdown();
    }

    private void registerBotEvents(){
        bot.addEventListener(new OnReady());
        bot.addEventListener(new OnMessageReceived());
        bot.addEventListener(new OnPrivateMessageReceived());
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new OnAsyncPlayerChat(), this);
        pm.registerEvents(new OnPlayerQuit(), this);
        pm.registerEvents(new OnPlayerJoin(), this);
        pm.registerEvents(new OnPlayerMove(), this);
        pm.registerEvents(new OnPlayerCommandPreprocess(), this);
        pm.registerEvents(new OnFoodLevelChange(), this);
        pm.registerEvents(new OnEntityDamaged(), this);
        pm.registerEvents(new OnEntityDamagedByEntity(), this);
        pm.registerEvents(new OnPlayerDropItem(), this);
        pm.registerEvents(new OnPlayerInteract(), this);
        pm.registerEvents(new OnBlockBreak(), this);
        pm.registerEvents(new OnPlayerRespawn(), this);
        pm.registerEvents(new OnAsyncPlayerPreLogin(), this);
        pm.registerEvents(new OnPlayerKick(), this);
    }

    @SuppressWarnings("all")
    private void registerCommands(){
        this.getCommand("login").setExecutor(new LoginCommand());
        this.getCommand("logout").setExecutor(new LogoutCommand());
        this.getCommand("forcelogin").setExecutor(new ForceLoginCommand());
        this.getCommand("changepassword").setExecutor(new ChangePasswordCommand());
        this.getCommand("createaccount").setExecutor(new CreateAccountCommand());
    }

    @SuppressWarnings("all")
    private void initializeWorlds(){
        World world = Bukkit.getWorld("world");
        if(world.getName().equals("world")){
            world.setSpawnLocation(spawnLocation);
        }
    }

    private void registerFilters(){
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter());
    }

    public static long getMessageId(){
        return messageId;
    }
    public static long getGuildId(){
        return guildId;
    }
    public static long getChannelId(){
        return channelId;
    }
    public static HashMap<String, User> getUsers(){
        return USERS;
    }

    public static JDA getBot(){
        return bot;
    }
    public static SkinManager getSkinManager(){
        return skinManager;
    }
    public static DatabaseProvider getDatabaseProvider(){
        return databaseProvider;
    }
    public static PluginAsyncLoop getPluginAsyncLoop(){
        return pluginAsyncLoop;
    }
    public static ConfigurationManager getConfigurationManager(){
        return configurationManager;
    }
    public static Location getSpawnLocation(){
        return spawnLocation;
    }
}
