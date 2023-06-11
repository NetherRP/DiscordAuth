package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

public class SessionResponsePacket extends Packet {

    private final boolean hasSession;

    public SessionResponsePacket(boolean hasSession) {
        this.hasSession = hasSession;
    }

    public boolean hasSession() {
        return hasSession;
    }
}
