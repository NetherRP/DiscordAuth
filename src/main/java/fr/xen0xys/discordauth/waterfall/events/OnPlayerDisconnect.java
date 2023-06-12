package fr.xen0xys.discordauth.waterfall.events;

import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class OnPlayerDisconnect implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        DiscordAuthProxy.getSessions().remove(uuid);
    }
}
