package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SessionAskPacket extends Packet {

    private final UUID target;

    public SessionAskPacket(@NotNull final UUID target) {
        this.target = target;
    }

    public UUID getTarget() {
        return target;
    }
}
