package fr.xen0xys.discordauth.common.discord;

import fr.xen0xys.discordauth.common.config.CoreConfig;
import fr.xen0xys.discordauth.common.discord.components.buttons.RegisterButton;
import fr.xen0xys.discordauth.common.discord.components.commands.DisplayRegisterCommand;
import fr.xen0xys.discordjava.DJApp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Objects;
import java.util.logging.Logger;

public class Bot {

    public Bot(CoreConfig coreConfig, Logger logger){
        try {
            DJApp app = new DJApp(coreConfig.getBotToken(), logger);
//            new DisplayRegisterCommand().register(this.app.getCommandsManager(), true);
            Guild guild = app.getJDA().getGuildById(1016358670600245369L);
            if(Objects.isNull(guild)) return;
            new DisplayRegisterCommand().registerLocal(app.getCommandsManager(), guild, true);
            new RegisterButton().register(app.getComponentsManager());
            this.setupActivity(app.getJDA(), coreConfig);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupActivity(JDA bot, CoreConfig config){
        Activity activity;
        String text = config.getActivityText();
        activity = switch (config.getActivityType()) {
            case "STREAMING" -> Activity.streaming(text, config.getActivityUrl());
            case "LISTENING" -> Activity.listening(text);
            case "WATCHING" -> Activity.watching(text);
            default -> Activity.playing(text);
        };
        bot.getPresence().setActivity(activity);
    }
}
