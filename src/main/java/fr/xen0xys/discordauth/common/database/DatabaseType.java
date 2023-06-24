package fr.xen0xys.discordauth.common.database;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum DatabaseType {
    SQLITE,
    MYSQL,
    POSTGRESQL;

    @Nullable
    public static DatabaseType from(String value){
        return Arrays.stream(DatabaseType.values()).filter(type -> type.name().equalsIgnoreCase(value)).findFirst().orElse(null);
    }
}
