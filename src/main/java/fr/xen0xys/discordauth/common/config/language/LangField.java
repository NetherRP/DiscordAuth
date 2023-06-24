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
    PASSWORD_CHANGING_ERROR("password_changing_error"),
    MISSING_PERMISSION("missing_permission"),
    PLAYER_NOT_FOUND("player_not_found"),
    CONFIG_NULL_LOCATION("config_null_location"),
    PREVENTION_BREAK_BLOCKS("prevention_break_blocks"),
    PREVENTION_USE_CHAT("prevention_use_chat"),
    PREVENTION_TAKE_DAMAGES("prevention_take_damages"),
    PREVENTION_DEAL_DAMAGES("prevention_deal_damages"),
    PREVENTION_MOVE("prevention_move"),
    PREVENTION_COMMANDS("prevention_commands"),
    PREVENTION_DROP_ITEMS("prevention_drop_items"),
    PREVENTION_INTERACT("prevention_interact"),
    ALREADY_CONNECTED("already_connected");


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
    public TextComponent asComponent(Object... args) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(this.asText(args));
    }
    public TextComponent asRawComponent(Object... args) {
        return Component.text(this.asText(args));
    }
    public String asText(Object... args) {
        return value.formatted(args);
    }
}
