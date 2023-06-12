package fr.xen0xys.discordauth.common.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ServerConfig extends ConfigurationReader{

    public ServerConfig(@NotNull File dataFolder) {
        super(dataFolder, "configs/server.yml");
    }
}
