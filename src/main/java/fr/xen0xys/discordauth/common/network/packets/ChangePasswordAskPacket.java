package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

import java.util.UUID;

public class ChangePasswordAskPacket extends Packet {

    private final UUID uuid;
    private final String newPassword;

    public ChangePasswordAskPacket(UUID uuid, String newPassword) {
        this.uuid = uuid;
        this.newPassword = newPassword;
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getNewPassword() {
        return newPassword;
    }
}
