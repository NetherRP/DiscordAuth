package fr.xen0xys.discordauth.common.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CommonConfig extends ConfigurationReader{

    public CommonConfig(@NotNull File dataFolder) {
        super(dataFolder, "config.yml");
    }

    public String getSecret(){
        return this.getConfiguration().getString("secret");
    }
}
