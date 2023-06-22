package fr.xen0xys.discordauth.waterfall.events;

import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OnLogin implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(@NotNull final LoginEvent e){
        String username = e.getConnection().getName();
        UUID uuid = e.getConnection().getUniqueId();
        DiscordAuthProxy.getInstance().getLogger().info("OnLogin: " + username + " " + uuid);
        if(DiscordAuthProxy.getDatabaseHandler().isAccountExists(uuid)){
            Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(uuid);
            if(account.hasSession(e.getConnection().getSocketAddress().toString(), new Encryption(), DiscordAuthProxy.getCoreConfig().getSessionDuration()))
                DiscordAuthProxy.getSessions().add(uuid);
            DiscordAuthProxy.getInstance().getLogger().info(DiscordAuthProxy.getSessions().toString());
            return;
        }
        if(!DiscordAuthProxy.getDatabaseHandler().isAccountExists(username)){
            //noinspection deprecation
            e.setCancelReason("§cYou are not registered on DiscordAuth");
            e.setCancelled(true);
            return;
        }
        Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(username);
        account.setUuid(uuid);
        account.setLastConnection(System.currentTimeMillis());
        account.setLastIp(e.getConnection().getSocketAddress().toString());
        DiscordAuthProxy.getDatabaseHandler().updateAccount(account);
    }
}
