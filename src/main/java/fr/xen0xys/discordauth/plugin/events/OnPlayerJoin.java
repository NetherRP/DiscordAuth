package fr.xen0xys.discordauth.plugin.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.discord.embeds.PlayerJoinEmbed;
import fr.xen0xys.discordauth.models.User;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
import fr.xen0xys.xen0lib.utils.Status;
import fr.xen0xys.xen0lib.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        AccountTable accountTable = DiscordAuth.getAccountTable();
        Player player = e.getPlayer();
        User user;

        // If tp on login is enabled
        if(DiscordAuth.getConfiguration().isTpOnLogin()){
            player.teleport(DiscordAuth.getConfiguration().getSpawnPoint());
        }

        // Send message if login sending is enabled
        if(DiscordAuth.getConfiguration().getEnableConnectionMessage())
            BotUtils.sendEmbed(new PlayerJoinEmbed(player));


        Location playerLocation = player.getLocation();
        // Only on first time login
        if(!e.getPlayer().hasPlayedBefore() && DiscordAuth.getConfiguration().isFirstTimeTp()) {
            player.teleport(DiscordAuth.getConfiguration().getSpawnPoint());
            playerLocation = DiscordAuth.getConfiguration().getSpawnPoint();
        }

        user = new User(player, playerLocation);
        // Check if payer has session
        if(accountTable.isPlayerHasSession(player) == Status.HasSession){
            user.setIsLogged(true);
            if(accountTable.setLastLogin(player, Utils.getPlayerIP(player)) != Status.Success){
                player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().sessionEnd);
            }
            player.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().sessionLogin);
        }else{
            user.setLoginLocation(player.getLocation());
            player.sendMessage(ChatColor.RED + DiscordAuth.getLanguage().loginRequest);
            PluginUtils.displayLoginScreen(player);
        }

        DiscordAuth.getUsers().put(player.getName(), user);
    }

}
