package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

public class AccountCreationResponsePacket extends Packet {

    private final boolean success;

    public AccountCreationResponsePacket(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
