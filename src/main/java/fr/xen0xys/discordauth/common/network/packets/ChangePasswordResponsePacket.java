package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

public class ChangePasswordResponsePacket extends Packet {

    private final boolean success;

    public ChangePasswordResponsePacket(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
