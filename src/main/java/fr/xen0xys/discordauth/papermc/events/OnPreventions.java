package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class OnPreventions implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getWhoClicked().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getEntity().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player player && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId()))
            e.setCancelled(true);
        if(e.getEntity() instanceof Player player && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler
    public void onEntityDamaged(EntityDamageEvent e) {
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getEntity().getUniqueId())){
            e.getEntity().sendMessage(Component.text("Please login to take damage !"));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(Component.text("Please login to break blocks !"));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onAsyncChat(AsyncChatEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(Component.text("Please login to use chat !"));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(!DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId()))
            return;
        Location loginLocation = DiscordAuthPlugin.getUnauthenticatedPlayers().get(player.getUniqueId());
        if (loginLocation.getWorld() == e.getTo().getWorld()) {
            if (loginLocation.distance(e.getTo()) >= 3)
                e.setCancelled(true);
        }else{
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            if(!e.getMessage().startsWith("/login") && !e.getMessage().startsWith("/l"))
                e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerKick(PlayerKickEvent e){
        if(e.reason().toString().equals("Flying is not enabled on this server") && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
}
