package fr.xen0xys.discordauth.common.database.models;

import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import jakarta.persistence.*;

import java.util.UUID;
import java.util.logging.Logger;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "discordauth_accounts")
public class Account {

    @Id
    @Column(name = "discord_id")
    private long discordId;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "last_ip")
    private String lastIp;

    @Column(name = "last_connection")
    private long lastConnection;

    public Account() {

    }

    public Account(long discordId, UUID uuid, String username, String password, String lastIp, long lastConnection) {
        this.discordId = discordId;
    	this.uuid = uuid;
        this.username = username;
    	this.password = password;
    	this.lastIp = lastIp;
    	this.lastConnection = lastConnection;
    }

    public boolean hasSession(String newIp, Encryption encryption, long sessionDuration){
        if(!encryption.compareHash(clearIP(newIp), this.lastIp))
            DiscordAuthProxy.getInstance().getLogger().warning("IPs are not the same : %s != %s".formatted(clearIP(newIp), this.lastIp));
        if(!(System.currentTimeMillis() - lastConnection < 1000 * sessionDuration))
            DiscordAuthProxy.getInstance().getLogger().warning("Session is expired : %s < %s".formatted(System.currentTimeMillis() - lastConnection, 1000 * sessionDuration));
        return encryption.compareHash(clearIP(newIp), this.lastIp) && System.currentTimeMillis() - lastConnection < 1000 * sessionDuration;
    }

    public static String clearIP(String ip){
        return ip.replace("/", "").split(":")[0];
    }

    public long getDiscordId() {
        return discordId;
    }
    public UUID getUuid() {
        return uuid;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getLastIp() {
        return lastIp;
    }
    public long getLastConnection() {
        return lastConnection;
    }

    public void setDiscordId(int discordId) {
        this.discordId = discordId;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setLastIp(String lastIp, Logger logger) {
        logger.info("Setting last IP to %s".formatted(lastIp));
        Encryption encryption = new Encryption(logger);
        this.lastIp = encryption.hash(clearIP(lastIp));
    }
    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }
}
