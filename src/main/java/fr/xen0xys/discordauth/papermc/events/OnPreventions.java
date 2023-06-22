package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public class OnPreventions implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(@NotNull final InventoryClickEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getWhoClicked().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange(@NotNull final FoodLevelChangeEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getEntity().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamagedByEntity(@NotNull final EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player player && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId()))
            e.setCancelled(true);
        if(e.getEntity() instanceof Player player && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamaged(@NotNull final EntityDamageEvent e) {
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getEntity().getUniqueId())){
            e.getEntity().sendMessage(Component.text("Please login to take damage !"));
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(@NotNull final BlockBreakEvent e) {
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(Component.text("Please login to break blocks !"));
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(@NotNull final AsyncChatEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(Component.text("Please login to use chat !"));
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(@NotNull final PlayerMoveEvent e){
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
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(@NotNull final PlayerCommandPreprocessEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            if(!e.getMessage().startsWith("/login") && !e.getMessage().startsWith("/l"))
                e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(@NotNull final PlayerDropItemEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(@NotNull final PlayerInteractEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerKick(@NotNull final PlayerKickEvent e){
        if(e.reason().toString().equals("Flying is not enabled on this server") && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
}
