package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.discord.embeds.DeathEmbed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        if(DiscordAuth.getConfiguration().getDeathMessages()){
            BotUtils.sendEmbed(new DeathEmbed(player, e.getDeathMessage()));
        }
    }
}
