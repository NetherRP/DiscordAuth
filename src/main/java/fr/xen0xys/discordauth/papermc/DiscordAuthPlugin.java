package fr.xen0xys.discordauth.papermc;

import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.config.Configuration;
import fr.xen0xys.discordauth.papermc.events.OnPlayerJoin;
import fr.xen0xys.discordauth.papermc.events.OnPluginMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiscordAuthPlugin extends JavaPlugin {

    private static DiscordAuthPlugin instance;
    private static Logger logger;
    private static Configuration configuration;
    private static final List<Player> connectedPlayers = new ArrayList<>();

    @Override
    public void onLoad() {
        instance = this;
        logger = this.getLogger();
        configuration = new Configuration(this.getDataFolder(), "config.yml");
    }

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, PluginInfos.CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, PluginInfos.CHANNEL, new OnPluginMessage());
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        logger.info("DiscordAuth is started !");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static DiscordAuthPlugin getInstance() {
        return instance;
    }
    public static Configuration getConfiguration() {
        return configuration;
    }
    public static List<Player> getConnectedPlayers() {
        return connectedPlayers;
    }
}
