package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SessionInvalidationAskPacket extends Packet {

    private final UUID uuid;

    public SessionInvalidationAskPacket(@NotNull final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
