package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

import java.util.UUID;

public class ConnectionAskPacket extends Packet {

    private final UUID uuid;
    private final String password;

    public ConnectionAskPacket(UUID uuid, String password) {
        this.uuid = uuid;
        this.password = password;
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getPassword() {
        return password;
    }
}
