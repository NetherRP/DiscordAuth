package fr.xen0xys.discordauth_old.plugin.events;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth_old.discord.embeds.PlayerQuitEmbed;
import fr.xen0xys.discordauth_old.models.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        User user = DiscordAuthOld.getUsers().get(player.getName());

        if(!DiscordAuthOld.getConfiguration().isOnlySafety() && DiscordAuthOld.getConfiguration().getEnableConnectionMessage())
            BotUtils.sendEmbed(new PlayerQuitEmbed(e.getPlayer()));

        if (!user.isLogged())
            player.teleport(user.getLoginLocation());

        DiscordAuthOld.getUsers().remove(player.getName());
    }
}
