package fr.xen0xys.discordauth.common.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Configuration extends ConfigurationReader{
    public Configuration(@NotNull File dataFolder, @NotNull String configName) {
        super(dataFolder, configName);
    }

    public String getSecret(){
        return this.getConfiguration().getString("both.secret");
    }
}
