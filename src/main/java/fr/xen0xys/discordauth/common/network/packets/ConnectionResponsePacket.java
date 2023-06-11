package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

public class ConnectionResponsePacket extends Packet {

    private final boolean isConnected;

    public ConnectionResponsePacket(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
