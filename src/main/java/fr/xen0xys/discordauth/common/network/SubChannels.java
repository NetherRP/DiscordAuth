package fr.xen0xys.discordauth.common.network;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum SubChannels {
    SESSION_ASK("SessionAsk"),
    SESSION_RESPONSE("SessionResponse"),
    CONNECTION_ASK("ConnectionAsk"),
    CONNECTION_RESPONSE("ConnectionResponse"),
    SESSION_INVALIDATION_ASK("SessionInvalidationAsk"),
    SESSION_INVALIDATION_RESPONSE("SessionInvalidationResponse"),
    ACCOUNT_CREATION_ASK("AccountCreationAsk"),
    ACCOUNT_CREATION_RESPONSE("AccountCreationResponse"),
    CHANGE_PASSWORD_ASK("ChangePasswordAsk"),
    CHANGE_PASSWORD_RESPONSE("ChangePasswordResponse");

    private final String name;

    SubChannels(@NotNull final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SubChannels from(@NotNull final String value){
        return Arrays.stream(SubChannels.values()).filter(subChannel -> subChannel.getName().equals(value)).findFirst().orElse(null);
    }
}
