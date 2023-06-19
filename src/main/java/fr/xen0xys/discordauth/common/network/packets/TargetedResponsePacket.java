package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TargetedResponsePacket extends Packet {

    private final UUID target;
    private final boolean success;

    public TargetedResponsePacket(@Nullable UUID target, final boolean success) {
        this.target = target;
        this.success = success;
    }

    @Nullable
    public UUID getTarget() {
        return target;
    }
    public boolean isSuccess() {
        return success;
    }
}
