package fr.xen0xys.discordauth.bot.botevents;

import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.bot.commands.CreateAccountBotCommand;
import fr.xen0xys.discordauth.bot.commands.LoginBotCommand;
import fr.xen0xys.discordauth.bot.embeds.HelpEmbed;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnPrivateMessageReceived extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        super.onPrivateMessageReceived(event);
        Message message = event.getMessage();
        User user = event.getAuthor();
        if(message.getContentRaw().startsWith("/createaccount")) {
            new CreateAccountBotCommand(user, message);
        }else if(message.getContentRaw().startsWith("/login")){
            new LoginBotCommand(user, message);
        }else if(message.getContentRaw().startsWith("/")){
            BotUtils.sendDM(user, new HelpEmbed());
        }
    }
}
