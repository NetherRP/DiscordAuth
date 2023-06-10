package fr.xen0xys.discordauth;

import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.discord.events.MessageReactionAddListener;
import fr.xen0xys.discordauth.models.config.CustomConfiguration;
import fr.xen0xys.discordauth.models.config.Language;
import fr.xen0xys.discordauth.discord.commands.AccountSlashCommand;
import fr.xen0xys.discordauth.discord.commands.AdminAccountSlashCommand;
import fr.xen0xys.discordauth.discord.commands.GetIdSlashCommand;
import fr.xen0xys.discordauth.discord.commands.LoginSlashCommand;
import fr.xen0xys.discordauth.discord.events.ButtonClickListener;
import fr.xen0xys.discordauth.discord.events.SlashCommandListener;
import fr.xen0xys.discordauth.models.User;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.plugin.commands.*;
import fr.xen0xys.discordauth.plugin.commands.tabcompleters.AccountTabCompleter;
import fr.xen0xys.discordauth.plugin.commands.tabcompleters.DiscordAuthTabCompleter;
import fr.xen0xys.discordauth.plugin.events.*;
import fr.xen0xys.discordauth.utils.ConsoleFilter;
import fr.xen0xys.xen0lib.database.Database;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class DiscordAuth extends JavaPlugin {

    // Base values
    private static DiscordAuth instance;
    private static Logger logger;

    // Needed values
    private static CustomConfiguration config;
    private static Language language;
    private static JDA bot;

    // Database
    private static Database database;
    private static AccountTable accountTable;

    // Users
    private static final HashMap<String, User> users = new HashMap<>();

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
        logger = this.getLogger();
        this.registerFilters();
        config = new CustomConfiguration(new File("plugins/DiscordAuth"), "config.yml");
        language = new Language(this, String.format("resources/%s.yml", getConfiguration().getLanguage()));

        if(getConfiguration().isMySQLEnabled()){
            HashMap<String, Object> databaseInfos = getConfiguration().getDatabaseInfos();
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
                "discordId BIGINT UNIQUE," +
                "password VARCHAR(100)," +
                "ip VARCHAR(100)," +
                "lastLogin BIGINT," +
                "hasSession TINYINT");
        try {
            if(!getConfiguration().isOnlySafety())
                this.setupBot();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            this.getServer().shutdown();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.registerEvents();
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if(bot != null){
            this.shutdownBot();
        }
        this.unregisterEvents();
        database.disconnect();
    }

    private void setupBot() throws LoginException, InterruptedException {
        bot = JDABuilder.createDefault(getConfiguration().getBotToken()).build().awaitReady();
        Activity activity;
        String text = getConfiguration().getBotActivityText();
        activity = switch (getConfiguration().getBotActivityType()) {
            case "STREAMING" -> Activity.streaming(text, getConfiguration().getBotActivityUrl());
            case "LISTENING" -> Activity.listening(text);
            case "WATCHING" -> Activity.watching(text);
            default -> Activity.playing(text);
        };
        bot.getPresence().setActivity(activity);

        // Slash commands creation
        Guild eventGuild = bot.getGuildById(getConfiguration().getGuildId());
        eventGuild.upsertCommand(new AccountSlashCommand().getCommandData()).queue();
        eventGuild.upsertCommand(new AdminAccountSlashCommand().getCommandData()).queue();
        eventGuild.upsertCommand(new GetIdSlashCommand().getCommandData()).queue();
        eventGuild.upsertCommand(new LoginSlashCommand().getCommandData()).queue();

        // Add listeners
        bot.addEventListener(new SlashCommandListener());
        bot.addEventListener(new ButtonClickListener());
        bot.addEventListener(new MessageReactionAddListener());

        sendRegisterMessage();
    }

    private void shutdownBot(){
        bot.getRegisteredListeners().forEach(bot::removeEventListener);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        bot.shutdown();
    }

    private void sendRegisterMessage(){
        List<net.dv8tion.jda.api.entities.User> discordUsers = BotUtils.getUserWhoReact();
        List<Long> ids = getAccountTable().getDbIds();
        for(net.dv8tion.jda.api.entities.User discordUser : discordUsers){
            if(!ids.contains(discordUser.getIdLong())){
                discordUser.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(getLanguage().acceptMessage).queue();
                });
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands(){
        this.getCommand("forcelogin").setExecutor(new ForceLoginCommand());
        this.getCommand("login").setExecutor(new LoginCommand());
        this.getCommand("logout").setExecutor(new LogoutCommand());
        this.getCommand("account").setExecutor(new AccountCommand());
        this.getCommand("account").setTabCompleter(new AccountTabCompleter());
        this.getCommand("discordauth").setExecutor(new DiscordAuthCommand());
        this.getCommand("discordauth").setTabCompleter(new DiscordAuthTabCompleter());
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new OnAsyncChat(), this);
        pm.registerEvents(new OnAsyncPlayerPreLogin(), this);
        pm.registerEvents(new OnBlockBreak(), this);
        pm.registerEvents(new OnEntityDamaged(), this);
        pm.registerEvents(new OnEntityDamagedByEntity(), this);
        pm.registerEvents(new OnFoodLevelChange(), this);
        pm.registerEvents(new OnInventoryClick(), this);
        pm.registerEvents(new OnPlayerCommandPreprocess(), this);
        pm.registerEvents(new OnPlayerDropItem(), this);
        pm.registerEvents(new OnPlayerInteract(), this);
        pm.registerEvents(new OnPlayerJoin(), this);
        pm.registerEvents(new OnPlayerKick(), this);
        pm.registerEvents(new OnPlayerMove(), this);
        pm.registerEvents(new OnPlayerQuit(), this);
        pm.registerEvents(new OnPlayerRespawn(), this);
    }

    private void unregisterEvents(){
        HandlerList.unregisterAll(this);
    }

    private void registerFilters(){
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(new ConsoleFilter());
    }

    public static DiscordAuth getInstance() {
        return instance;
    }
    public static Language getLanguage() {
        return language;
    }
    public static CustomConfiguration getConfiguration() {
        return config;
    }
    public static JDA getBot() {
        return bot;
    }

    // Database
    public static Database getDatabase() {
        return database;
    }
    public static AccountTable getAccountTable() {
        return accountTable;
    }

    // Users
    public static HashMap<String, User> getUsers() {
        return users;
    }
}