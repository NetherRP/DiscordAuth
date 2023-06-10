package fr.xen0xys.discordauth.papermc;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DiscordAuthPlugin extends JavaPlugin {

    private static Logger logger;

    @Override
    public void onLoad() {
        logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        logger.info("DiscordAuth is started !");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
