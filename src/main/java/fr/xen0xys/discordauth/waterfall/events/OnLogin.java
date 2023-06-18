package fr.xen0xys.discordauth.waterfall.events;

import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class OnLogin implements Listener {
    @EventHandler
    public void onLogin(LoginEvent e){
        String username = e.getConnection().getName();
        UUID uuid = e.getConnection().getUniqueId();
        Encryption encryption = new Encryption(DiscordAuthProxy.getInstance().getLogger());
        DiscordAuthProxy.getInstance().getLogger().info("OnLogin: " + username + " " + uuid);
        if(DiscordAuthProxy.getDatabaseHandler().isAccountExists(uuid)){
            Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(uuid);
            if(account.hasSession(e.getConnection().getSocketAddress().toString(), encryption, DiscordAuthProxy.getCoreConfig().getSessionDuration()))
                DiscordAuthProxy.getSessions().add(uuid);
            DiscordAuthProxy.getInstance().getLogger().info(DiscordAuthProxy.getSessions().toString());
            return;
        }
        if(!DiscordAuthProxy.getDatabaseHandler().isAccountExists(username)){
            e.setCancelReason("§cYou are not registered on DiscordAuth");
            e.setCancelled(true);
            return;
        }
        Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(username);
        account.setUuid(uuid);
        account.setLastConnection(System.currentTimeMillis());
        account.setLastIp(encryption.hash(Account.clearIP(e.getConnection().getSocketAddress().toString())));
        DiscordAuthProxy.getDatabaseHandler().updateAccount(account);
    }
}