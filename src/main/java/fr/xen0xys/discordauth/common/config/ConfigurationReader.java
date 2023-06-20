package fr.xen0xys.discordauth.common.config;

import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ConfigurationReader {

    private final String configName;
    private final YamlConfiguration configuration;
    private final File configFile;
    private final String dataFolder;

    public ConfigurationReader(@NotNull final File dataFolder, @NotNull final String configName){
        this.configName = configName;
        this.dataFolder = dataFolder.getPath();
        this.configFile = new File(dataFolder, configName);
        if(!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            this.saveDefault();
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveDefault(){
        try {
            InputStream configInputStream = getClass().getClassLoader().getResourceAsStream(this.configName);
            try (FileOutputStream outputStream = new FileOutputStream(this.configFile)) {
                if (Objects.nonNull(configInputStream))
                    configInputStream.transferTo(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConfiguration(){
        return this.configuration;
    }
    public String getDataFolder() {
        return dataFolder;
    }
}