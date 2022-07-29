package fr.xen0xys.discordauth_old;

import fr.xen0xys.discordauth.config.CustomConfiguration;
import fr.xen0xys.discordauth.config.Language;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.models.User;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.plugin.commands.LoginCommand;
import fr.xen0xys.discordauth.plugin.commands.LogoutCommand;
import fr.xen0xys.discordauth.plugin.events.*;
import fr.xen0xys.discordauth_old.discord.events.OnMessageReceived;
import fr.xen0xys.discordauth_old.plugin.PluginAsyncLoop;
import fr.xen0xys.discordauth_old.plugin.commands.ChangePasswordCommand;
import fr.xen0xys.discordauth.plugin.commands.CreateAccountCommand;
import fr.xen0xys.discordauth_old.plugin.commands.DiscordAuthCommand;
import fr.xen0xys.discordauth.plugin.commands.ForceLoginCommand;
import fr.xen0xys.discordauth_old.plugin.commands.tabcompleters.DiscordAuthTabCompleter;
import fr.xen0xys.discordauth.utils.ConsoleFilter;
import fr.xen0xys.xen0lib.database.Database;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.logging.Logger;

public class DiscordAuthOld extends JavaPlugin {

    // createaccount Xen0Xys 266646920473214978 test

    private static Plugin instance;
    private static Logger logger;
    private static CustomConfiguration configuration;
    private static Language language;
    private static Database database;
    private static PluginAsyncLoop pluginAsyncLoop;
    private static final HashMap<String, User> users = new HashMap<>();

    //BOT
    private static JDA bot;

    // DATABASE
    private static AccountTable accountTable;

    public static Language getLanguage() {
        return language;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.registerFilters();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        logger.info("Unregistering events...");
        this.unregisterEvents();
        logger.info("Events unregistered!");

        if(pluginAsyncLoop != null){
            logger.info("Stopping async loop...");
            pluginAsyncLoop.stop();
            logger.info("Async loop stopped!");
        }else{
            logger.info("Skip async loop stopping!");
        }

        database.disconnect();

        if(bot != null){
            logger.info("Disabling bot...");
            this.stopBot();
            logger.info("Bot stopped!");
        }else{
            logger.info("Skip bot stopping!");
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;
        logger = this.getLogger();

        logger.info("Loading configuration...");
        configuration = new CustomConfiguration(this, "config.yml");
        language = new Language(this, String.format("resources/%s.yml", configuration.getLanguage()));
        logger.info("Configuration loaded!");

        // DATABASE INIT
        logger.info("Initializing database...");
        if(configuration.isMySQLEnabled()){
            HashMap<String, Object> databaseInfos = configuration.getDatabaseInfos();
            database = new Database(String.valueOf(databaseInfos.get("host")),
                    Integer.parseInt(String.valueOf(databaseInfos.get("port"))),
                    String.valueOf(databaseInfos.get("user")),
                    String.valueOf(databaseInfos.get("password")),
                    String.valueOf(databaseInfos.get("database")), logger);
        }else{
            database = new Database(this.getDataFolder().getPath(), "DiscordAuth", logger);
        }

        if(database.connect() != Status.Success){
            logger.severe("Cannot connect to Database, stopping server...");
            Bukkit.getServer().shutdown();
        }
        accountTable = new AccountTable("DiscordAuth_Accounts", database);
        database.openTableAndCreateINE(accountTable, "id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                "UUID VARCHAR(50)," +
                "minecraftName VARCHAR(30)," +
                "discordId BIGINT," +
                "password VARCHAR(100)," +
                "ip VARCHAR(100)," +
                "lastLogin BIGINT," +
                "hasSession TINYINT," +
                "deniedIps TEXT," +
                "allowedIps TEXT");

        logger.info("Database initialized!");

        // PLUGIN INIT
        logger.info("Registering events and commands...");
        this.registerEvents();
        this.registerCommands();
        logger.info("Events and commands registered!");

        logger.info("Building bot...");
        if(!configuration.isOnlySafety()){
            if(configuration.isBotActivityEnabled()) {
                try {
                    this.buildBot();
                    logger.info("Bot built");
                } catch (LoginException | InterruptedException e) {
                    e.printStackTrace();
                    logger.severe("Cannot build Bot, stopping server...");
                    Bukkit.getServer().shutdown();
                }
            }
        }

        if(!configuration.isOnlySafety()){
            logger.info("Starting async loop...");
            pluginAsyncLoop = new PluginAsyncLoop();
            pluginAsyncLoop.runTaskAsynchronously(this);
            logger.info("Async loop started!");
        }else{
            logger.info("Skip async loop loading!");
        }
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new OnPlayerJoin(), this);
        pm.registerEvents(new OnAsyncPlayerPreLogin(), this);
        pm.registerEvents(new OnPlayerQuit(), this);
        pm.registerEvents(new OnPlayerMove(), this);
        pm.registerEvents(new OnPlayerKick(), this);
        pm.registerEvents(new OnAsyncChat(), this);
        pm.registerEvents(new OnBlockBreak(), this);
        pm.registerEvents(new OnEntityDamaged(), this);
        pm.registerEvents(new OnEntityDamagedByEntity(), this);
        pm.registerEvents(new OnFoodLevelChange(), this);
        pm.registerEvents(new OnPlayerCommandPreprocess(), this);
        pm.registerEvents(new OnPlayerDropItem(), this);
        pm.registerEvents(new OnPlayerInteract(), this);
        pm.registerEvents(new OnPlayerRespawn(), this);
        pm.registerEvents(new OnInventoryClick(), this);
    }

    private void unregisterEvents(){
        HandlerList.unregisterAll(this);
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands(){
        this.getCommand("createaccount").setExecutor(new CreateAccountCommand());
        this.getCommand("login").setExecutor(new LoginCommand());
        this.getCommand("logout").setExecutor(new LogoutCommand());
        this.getCommand("changepassword").setExecutor(new ChangePasswordCommand());
        this.getCommand("forcelogin").setExecutor(new ForceLoginCommand());
        this.getCommand("discordauth").setExecutor(new DiscordAuthCommand());
        this.getCommand("discordauth").setTabCompleter(new DiscordAuthTabCompleter());
    }

    private void registerFilters(){
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter());
    }

    // BOT
    private void buildBot() throws LoginException, InterruptedException {
        bot = JDABuilder.createDefault(configuration.getBotToken()).build().awaitReady();

        // Set bot status
        Activity activity;
        String text = configuration.getBotActivityText();
        activity = switch (configuration.getBotActivityType()) {
            case "STREAMING" -> Activity.streaming(text, configuration.getBotActivityUrl());
            case "LISTENING" -> Activity.listening(text);
            case "WATCHING" -> Activity.watching(text);
            default -> Activity.playing(text);
        };
        bot.getPresence().setActivity(activity);

        // Sending message for server started
        if(getConfiguration().getStartStopMessages()){
            BotUtils.sendMessage(BotUtils.getServerStartMessage());
        }

        // Add reaction to message
        Message message = BotUtils.retrieveMessageFromId(configuration.getGuildId(), configuration.getMessageId());
        if(message != null){
            message.addReaction(Emoji.fromUnicode(configuration.getReactionName())).complete();
        }
        // Enable or not discord commands
        if(!configuration.isOnlySafety()){
            registerBotEvents();
        }
    }
    private void stopBot(){
        bot.getRegisteredListeners().forEach(bot::removeEventListener);
        if(getConfiguration().getStartStopMessages()){
            BotUtils.sendMessage(BotUtils.getServerStopMessage());
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bot.shutdown();
    }

    private void registerBotEvents(){
        bot.addEventListener(new OnMessageReceived());
    }


    // GETTERS
    public static Plugin getInstance() {
        return instance;
    }
    public static Logger getCustomLogger() {
        return logger;
    }
    public static CustomConfiguration getConfiguration() {
        return configuration;
    }
    public static Database getDatabase() {
        return database;
    }
    public static HashMap<String, User> getUsers() {
        return users;
    }
    public static JDA getBot() {
        return bot;
    }
    public static AccountTable getAccountTable() {
        return accountTable;
    }
}
