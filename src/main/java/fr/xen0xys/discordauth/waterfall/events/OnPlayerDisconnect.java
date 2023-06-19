package fr.xen0xys.discordauth.waterfall.events;

import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class OnPlayerDisconnect implements Listener {
    @EventHandler
    public void onPlayerDisconnect(@NotNull final PlayerDisconnectEvent e){
        DiscordAuthProxy.getSessions().remove(e.getPlayer().getUniqueId());
    }
}
