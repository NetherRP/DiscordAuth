package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.bot.embeds.PlayerJoinEmbed;
import fr.xen0xys.discordauth.databases.SecurityDatabase;
import fr.xen0xys.discordauth.models.User;
import fr.xen0xys.discordauth.utils.PluginUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(DiscordAuth.getConfigurationManager().getEnableConnectionMessage())
            BotUtils.sendEmbed(new PlayerJoinEmbed(player));
        DiscordAuth.getUsers().put(player.getName(), new User(player, e.getPlayer().getLocation()));
        User user = DiscordAuth.getUsers().get(player.getName());
        if(user != null){
            if(new SecurityDatabase().hasSession(player.getName(), PluginUtils.getPlayerIP(player))){
                user.setIsLogged(true);
                player.sendMessage(ChatColor.GREEN + "You has been auto-disconnected (session)");
            }else{
                player.sendMessage(String.format("%s%sPlease login yourself using: /login <password>", ChatColor.UNDERLINE, ChatColor.DARK_AQUA));
            }
            if(!player.hasPlayedBefore()){
                initializePlayer(user);
            }
        }
    }

    private void initializePlayer(User user){
        Location spawn = DiscordAuth.getSpawnLocation();
        user.setLoginLocation(spawn);
        user.getPlayer().teleport(spawn);
    }
}
