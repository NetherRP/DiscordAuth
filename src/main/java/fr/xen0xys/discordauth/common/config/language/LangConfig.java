package fr.xen0xys.discordauth.common.config.language;

import fr.xen0xys.discordauth.common.config.ConfigurationReader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

public class LangConfig extends ConfigurationReader {

    public LangConfig(@NotNull File dataFolder, String language) {
        super(dataFolder, "lang/%s.yml".formatted(language));
        Arrays.stream(LangField.values()).forEach(field -> {
            String value = this.getConfiguration().getString(field.getKey());
            field.setValue(value);
        });
    }
}
