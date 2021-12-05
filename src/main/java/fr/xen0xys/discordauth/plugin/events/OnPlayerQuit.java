package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.discord.embeds.PlayerQuitEmbed;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        User user = DiscordAuth.getUsers().get(player.getName());

        if(!DiscordAuth.getConfiguration().isOnlySafety() && DiscordAuth.getConfiguration().getEnableConnectionMessage())
            BotUtils.sendEmbed(new PlayerQuitEmbed(e.getPlayer()));

        if (!user.isLogged())
            player.teleport(user.getLoginLocation());

        DiscordAuth.getUsers().remove(player.getName());
    }
}
