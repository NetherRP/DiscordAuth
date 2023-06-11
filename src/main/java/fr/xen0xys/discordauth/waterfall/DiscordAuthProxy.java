package fr.xen0xys.discordauth.waterfall;

import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.waterfall.events.OnPluginMessage;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class DiscordAuthProxy extends Plugin {

    private static DiscordAuthProxy instance;
    private static Logger logger;

    @Override
    public void onLoad() {
        instance = this;
        logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        this.getProxy().registerChannel(PluginInfos.CHANNEL);
        getProxy().getPluginManager().registerListener(this, new OnPluginMessage());
        logger.info("DiscordAuth is started !");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static DiscordAuthProxy getInstance() {
        return instance;
    }
}
