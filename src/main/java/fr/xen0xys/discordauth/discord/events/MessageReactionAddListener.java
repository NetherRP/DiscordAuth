package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactionAddListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(event.getUserIdLong() != DiscordAuth.getInstance().getBot().getSelfUser().getIdLong()) {
            if(event.getMessageIdLong() == DiscordAuth.getConfiguration().getMessageId()){
                if(DiscordAuth.getAccountTable().isDiscordUserExist(event.getUserIdLong()) == Status.NotExist){
                    event.getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(DiscordAuth.getLanguage().acceptMessage).queue();
                    });
                }
            }
        }
    }
}
