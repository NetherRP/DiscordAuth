package fr.xen0xys.discordauth.common.network.packets;

public class SessionResponsePacket extends Packet {

    private final boolean hasSession;

    public SessionResponsePacket(boolean hasSession) {
        this.hasSession = hasSession;
    }

    public boolean hasSession() {
        return hasSession;
    }
}
