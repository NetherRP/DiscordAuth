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
    private static long message_id = 0L;
    private static long guild_id = 0L;
    private static long channel_id = 0L;
    private static JDA bot;
    private static SkinManager skin_manager;
    private static final String log_prefix = "[DiscordAuth]: ";
    private static DatabaseProvider database_provider;
    private static final HashMap<String, User> USERS = new HashMap<>();
    private static PluginAsyncLoop plugin_async_loop;
    private static ConfigurationManager configuration_manager;
    private static Location spawn_location;

    @Override
    public void onLoad() {
        System.out.println(ChatColor.GREEN + log_prefix + "Loading plugin");
        configuration_manager = new ConfigurationManager(this);
        try {
            buildBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        database_provider = new DatabaseProvider();
        super.onLoad();
        System.out.println(ChatColor.GREEN + log_prefix + "Plugin loaded");
    }

    @Override
    public void onDisable() {
        System.out.println(ChatColor.GREEN + log_prefix + "Disabling plugin");
        plugin_async_loop.stop();
        stopBot();
        super.onDisable();
        System.out.println(ChatColor.GREEN + log_prefix + "Plugin disabled");
    }

    @Override
    public void onEnable() {
        System.out.println(ChatColor.GREEN + log_prefix + "Enabling plugin");
        registerEvents();
        registerCommands();
        registerFilters();
        skin_manager = new SkinManager();
        plugin_async_loop = new PluginAsyncLoop();
        plugin_async_loop.runTaskAsynchronously(this);
        spawn_location = configuration_manager.getSpawnPoint();
        this.initializeWorlds();
        super.onEnable();
        // Enable bstats
        int pluginId = 10497;
        new Metrics(this, pluginId);
        System.out.println(ChatColor.GREEN + log_prefix + "Plugin enabled");
    }

    private void buildBot() throws LoginException {
        message_id = configuration_manager.getMessageId();
        guild_id = configuration_manager.getGuildId();
        channel_id = configuration_manager.getChannelId();
        bot = JDABuilder.createDefault(configuration_manager.getBotToken()).build();
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
            world.setSpawnLocation(spawn_location);
        }
    }

    private void registerFilters(){
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter());
    }

    public static long getMessageId(){
        return message_id;
    }
    public static long getGuildId(){
        return guild_id;
    }
    public static long getChannelId(){
        return channel_id;
    }
    public static HashMap<String, User> getUsers(){
        return USERS;
    }

    public static JDA getBot(){
        return bot;
    }
    public static SkinManager getSkinManager(){
        return skin_manager;
    }
    public static DatabaseProvider getDatabaseProvider(){
        return database_provider;
    }
    public static PluginAsyncLoop getPluginAsyncLoop(){
        return plugin_async_loop;
    }
    public static ConfigurationManager getConfigurationManager(){
        return configuration_manager;
    }
    public static Location getSpawnLocation(){
        return spawn_location;
    }
}
