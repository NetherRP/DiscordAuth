package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.bot.embeds.PlayerQuitEmbed;
import fr.xen0xys.discordauth.models.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        User user = DiscordAuth.getUsers().get(player.getName());
        if(DiscordAuth.getConfigurationManager().getEnableConnectionMessage())
            BotUtils.sendEmbed(new PlayerQuitEmbed(e.getPlayer()));
        if(user != null)
            if (!user.isLogged())
                player.teleport(user.getLoginLocation());
        DiscordAuth.getUsers().remove(player.getName());
    }
}
