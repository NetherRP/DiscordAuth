package fr.xen0xys.discordauth.common.discord;

import fr.xen0xys.discordauth.common.config.CoreConfig;
import fr.xen0xys.discordauth.common.discord.components.buttons.RegisterButton;
import fr.xen0xys.discordauth.common.discord.components.commands.DisplayRegisterCommand;
import fr.xen0xys.discordjava.DJApp;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Objects;

public class Bot {

    public Bot(CoreConfig coreConfig){
        this(coreConfig.getBotToken());
    }

    public Bot(String token){
        try {
            DJApp app = new DJApp(token, "DiscordAuthBot");
//            new DisplayRegisterCommand().register(this.app.getCommandsManager(), true);
            Guild guild = app.getJDA().getGuildById(1016358670600245369L);
            if(Objects.isNull(guild)) return;
            new DisplayRegisterCommand().registerLocal(app.getCommandsManager(), guild, true);
            new RegisterButton().register(app.getComponentsManager());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
