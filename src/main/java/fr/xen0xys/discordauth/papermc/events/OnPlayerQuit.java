package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.getPlayer().teleport(DiscordAuthPlugin.getUnauthenticatedPlayers().get(e.getPlayer().getUniqueId()));
        DiscordAuthPlugin.getUnauthenticatedPlayers().remove(e.getPlayer().getUniqueId());
    }
}
