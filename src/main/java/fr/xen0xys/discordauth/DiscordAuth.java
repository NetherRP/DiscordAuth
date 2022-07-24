package fr.xen0xys.discordauth;

import fr.xen0xys.discordauth.config.CustomConfiguration;
import fr.xen0xys.discordauth.config.Language;
import fr.xen0xys.discordauth.discord.commands.AccountSlashCommand;
import fr.xen0xys.discordauth.discord.commands.AdminAccountSlashCommand;
import fr.xen0xys.discordauth.discord.events.ButtonClickListener;
import fr.xen0xys.discordauth.discord.events.SlashCommandListener;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth_old.models.User;
import fr.xen0xys.xen0lib.database.Database;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
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
        config = new CustomConfiguration(this, "config.yml");
        language = new Language(this, "resources/fr_FR.yml");
        try {
            this.setupBot();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            this.getServer().shutdown();
        }

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
                "discordId BIGINT," +
                "password VARCHAR(100)," +
                "ip VARCHAR(100)," +
                "lastLogin BIGINT," +
                "hasSession TINYINT");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.shutdownBot();
        this.unregisterEvents();
        database.disconnect();
    }

    @Override
    public void onEnable() {
        super.onEnable();
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
        eventGuild.upsertCommand(new AccountSlashCommand(bot).getCommandData()).queue();
        eventGuild.upsertCommand(new AdminAccountSlashCommand(bot).getCommandData()).queue();

        // Add listeners
        bot.addEventListener(new SlashCommandListener());
        bot.addEventListener(new ButtonClickListener());
    }

    private void shutdownBot(){
        bot.getRegisteredListeners().forEach(bot::removeEventListener);
        if(getConfiguration().getStartStopMessages()){
            BotUtils.sendMessage(BotUtils.getServerStopMessage());
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        bot.shutdown();
    }

    private void unregisterEvents(){
        HandlerList.unregisterAll(this);
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