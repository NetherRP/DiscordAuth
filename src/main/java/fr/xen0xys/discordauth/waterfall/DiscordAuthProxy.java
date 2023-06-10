package fr.xen0xys.discordauth.waterfall;

import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class DiscordAuthProxy extends Plugin {

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
