package fr.xen0xys.discordauth.waterfall.events;

import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class OnPreLogin implements Listener {
    @EventHandler
    public void onPlayerPreLogin(LoginEvent e){
        String username = e.getConnection().getName();
        UUID uuid = e.getConnection().getUniqueId();
        Encryption encryption = new Encryption(DiscordAuthProxy.getInstance().getLogger());
        if(DiscordAuthProxy.getDatabaseHandler().isAccountExists(uuid)){
            Account account = DiscordAuthProxy.getDatabaseHandler().getAccount(uuid);
            if(account.hasSession(e.getConnection().getSocketAddress().toString(), encryption))
                DiscordAuthProxy.getSessions().add(uuid);
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
