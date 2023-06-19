package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ConnectionAskPacket extends Packet {

    private final UUID uuid;
    private final String password;

    public ConnectionAskPacket(@NotNull final UUID uuid, @NotNull final String password) {
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
