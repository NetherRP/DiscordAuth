package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

import java.util.UUID;

public class ConnectionAskPacket extends Packet {

    private final boolean onlineMode;
    private final String username;
    private final UUID uuid;
    private final String password;

    public ConnectionAskPacket(boolean onlineMode, String username, UUID uuid, String password) {
        this.onlineMode = onlineMode;
        this.username = username;
        this.uuid = uuid;
        this.password = password;
    }

    public ConnectionAskPacket(String username, String password) {
        this(true, username, null, password);
    }

    public ConnectionAskPacket(UUID uuid, String password) {
        this(true, null, uuid, password);
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }
    public String getUsername() {
        return username;
    }
    public UUID getUuid() {
        return uuid;
    }
    public String getPassword() {
        return password;
    }
}
