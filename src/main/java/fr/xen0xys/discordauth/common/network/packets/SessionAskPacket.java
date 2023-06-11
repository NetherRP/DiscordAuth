package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

import java.util.UUID;

public class SessionAskPacket extends Packet {
    private final boolean onlineMode;
    private final String username;
    private final UUID uuid;

    public SessionAskPacket(boolean onlineMode, String username, UUID uuid) {
        this.onlineMode = onlineMode;
        this.username = username;
        this.uuid = uuid;
    }

    public SessionAskPacket(String username) {
        this(true, username, null);
    }

    public SessionAskPacket(UUID uuid) {
        this(true, null, uuid);
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
}
