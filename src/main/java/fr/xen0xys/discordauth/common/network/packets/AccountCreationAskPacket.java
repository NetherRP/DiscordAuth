package fr.xen0xys.discordauth.common.network.packets;

import fr.xen0xys.discordauth.common.network.Packet;

public class AccountCreationAskPacket extends Packet {

    private final long discordId;
    private final String username;
    private final String password;

    public AccountCreationAskPacket(long discordId, String username, String password) {
        this.discordId = discordId;
        this.username = username;
        this.password = password;
    }

    public long getDiscordId() {
        return discordId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
