package fr.xen0xys.discordauth.common.database.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "discordauth_accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public Account(UUID uuid, String username, String password, String lastIp, long lastConnection) {
    	this.uuid = uuid;
        this.username = username;
    	this.password = password;
    	this.lastIp = lastIp;
    	this.lastConnection = lastConnection;
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

    public void setPassword(String password) {
        this.password = password;
    }
    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }
    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }
}
