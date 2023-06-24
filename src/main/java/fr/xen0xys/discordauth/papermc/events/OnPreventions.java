package fr.xen0xys.discordauth.papermc.events;

import fr.xen0xys.discordauth.common.config.language.LangField;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
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
        if(e.getDamager() instanceof Player player && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId())){
            player.sendMessage(LangField.PREVENTION_DEAL_DAMAGES.asComponent());
            e.setCancelled(true);
        }
        if(e.getEntity() instanceof Player player && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId())){
            player.sendMessage(LangField.PREVENTION_TAKE_DAMAGES.asComponent());
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamaged(@NotNull final EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player player)) return;
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId())){
            player.sendMessage(LangField.PREVENTION_TAKE_DAMAGES.asComponent());
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(@NotNull final BlockBreakEvent e) {
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(LangField.PREVENTION_BREAK_BLOCKS.asComponent());
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(@NotNull final AsyncChatEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(LangField.PREVENTION_USE_CHAT.asComponent());
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
            if (loginLocation.distance(e.getTo()) >= 3){
                player.sendMessage(LangField.PREVENTION_MOVE.asComponent());
                e.setCancelled(true);
            }
        }else{
            player.sendMessage(LangField.PREVENTION_MOVE.asComponent());
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(@NotNull final PlayerCommandPreprocessEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            if(!e.getMessage().split(" ")[0].equalsIgnoreCase("/login") && !e.getMessage().split(" ")[0].equalsIgnoreCase("/l")){
                e.getPlayer().sendMessage(LangField.PREVENTION_COMMANDS.asComponent());
                e.setCancelled(true);
            }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(@NotNull final PlayerDropItemEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(LangField.PREVENTION_DROP_ITEMS.asComponent());
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(@NotNull final PlayerInteractEvent e){
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(LangField.PREVENTION_INTERACT.asComponent());
            e.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityPickupItem(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player player)) return;
        if(DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(player.getUniqueId()))
            e.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerKick(@NotNull final PlayerKickEvent e){
        if(e.reason().toString().equals("Flying is not enabled on this server") && DiscordAuthPlugin.getUnauthenticatedPlayers().containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
}
