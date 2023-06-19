package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

import java.util.UUID;

public class SessionAskPacket extends Packet {

    private final UUID target;

    public SessionAskPacket(UUID target) {
        this.target = target;
    }

    public UUID getTarget() {
        return target;
    }
}
