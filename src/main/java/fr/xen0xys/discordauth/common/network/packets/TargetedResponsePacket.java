package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

import java.util.UUID;

public class TargetedResponsePacket extends Packet {

    private final UUID target;
    private final boolean success;

    public TargetedResponsePacket(UUID target, boolean success) {
        this.target = target;
        this.success = success;
    }

    public UUID getTarget() {
        return target;
    }
    public boolean isSuccess() {
        return success;
    }
}
