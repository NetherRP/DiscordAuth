package fr.xen0xys.discordauth.common.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CommonConfig extends ConfigurationReader{

    public CommonConfig(@NotNull File dataFolder) {
        super(dataFolder, "config.yml");
    }

    public boolean isCore(){
        return this.getValue(Boolean.class, "core", false);
    }

    public String getSecret(){
        return this.getValue(String.class, "secret", "unsafe_key");
    }

    public String getLanguage(){
        return this.getValue(String.class, "language", "en_EN");
    }
}
