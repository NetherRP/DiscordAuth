package fr.xen0xys.discordauth.common.config.language;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public enum LangField {
    SESSION_CHECK("session_check"),
    CONNECTED_SESSION("connected_session"),
    CONNECTED_LOGIN("connected_login"),
    LOGIN_ASK("login_ask"),
    CONNECTION_CHECK("connection_check"),
    INVALID_PASSWORD("invalid_password"),
    SESSION_INVALIDATION_CHECK("session_invalidation_check"),
    DISCONNECTED("disconnected"),
    DISCONNECTING_ERROR("disconnecting_error"),
    ACCOUNT_CREATED("account_created"),
    ACCOUNT_CREATION_ERROR("account_creation_error"),
    PASSWORD_CHANGE_CHECK("password_change_check"),
    PASSWORD_CHANGED("password_changed"),
    PASSWORD_CHANGING_ERROR("password_changing_error");

    private final String key;
    private String value;

    LangField(String key) {
        this.key = key;
        this.value = key;
    }

    public String getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public TextComponent asSerializedComponent(Object... args) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(this.asText(args));
    }
    public TextComponent asComponent(Object... args) {
        return Component.text(this.asText(args));
    }
    public String asText(Object... args) {
        return value.formatted(args);
    }
}
