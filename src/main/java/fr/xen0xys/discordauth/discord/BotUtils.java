package fr.xen0xys.discordauth.discord;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth_old.discord.embeds.CustomEmbed;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BotUtils {
    public static void sendMessage(String message){
        TextChannel channel = DiscordAuthOld.getBot().getTextChannelById(DiscordAuthOld.getConfiguration().getChannelId());
        if(channel != null){
            channel.sendTyping().complete();
            channel.sendMessage(message).complete();
        }
    }

    public static void sendEmbed(CustomEmbed embed){
        if(!DiscordAuthOld.getConfiguration().isOnlySafety()){
            TextChannel channel = DiscordAuthOld.getBot().getTextChannelById(DiscordAuthOld.getConfiguration().getChannelId());
            if(channel != null){
                channel.sendTyping().complete();
                channel.sendMessageEmbeds(embed.build()).complete();
            }
        }
    }

    public static void sendEmbed(CustomEmbed embed, PrivateChannel channel){
        if(channel != null){
            channel.sendTyping().complete();
            channel.sendMessageEmbeds(embed.build()).complete();
        }
    }

    public static String getServerStartMessage(){
        return DiscordAuth.getLanguage().serverStart;
    }
    public static String getServerStopMessage(){
        return DiscordAuth.getLanguage().serverStop;
    }

    public static List<User> getUserWhoReact(){
        Message message = retrieveMessageFromId(DiscordAuthOld.getConfiguration().getGuildId(), DiscordAuthOld.getConfiguration().getMessageId());
        if(message != null){
            reactToMessage(message);
            for(MessageReaction messageReaction: message.getReactions()){
                if(messageReaction.getEmoji().asUnicode().equals(Emoji.fromUnicode(DiscordAuthOld.getConfiguration().getReactionName()))){
                    List<User> users = messageReaction.retrieveUsers().complete();
                    users.remove(DiscordAuthOld.getBot().getSelfUser());
                    return users;
                }
            }
        }
        return null;
    }

    public static boolean isUserHasReact(User targetUser){
        List<User> reactUserList = getUserWhoReact();
        if (reactUserList != null) {
            for(User user: reactUserList){
                if(user == targetUser){
                    return true;
                }
            }
        }
        return false;
    }

    public static Message retrieveMessageFromId(long guildId, long messageId){
        Guild guild = DiscordAuthOld.getBot().getGuildById(guildId);
        if(guild != null){
            for(Category category: guild.getCategories()) {
                for(TextChannel channel: category.getTextChannels()){
                    try{
                        return channel.retrieveMessageById(messageId).complete();
                    } catch(ErrorResponseException ignored){

                    }
                }
            }
        }
        return null;
    }

    public static void reactToMessage(Message message){
        message.addReaction(Emoji.fromUnicode(DiscordAuthOld.getConfiguration().getReactionName())).complete();
    }

    public static Member getMemberFromUser(User user){
        Guild guild = DiscordAuthOld.getBot().getGuildById(DiscordAuthOld.getConfiguration().getGuildId());
        if(guild != null){
            return guild.getMemberById(user.getIdLong());
        }
        return null;
    }

    public static void sendDM(User user, String message){
        if(user.getIdLong() != DiscordAuthOld.getBot().getSelfUser().getIdLong()){
            PrivateChannel channel = user.openPrivateChannel().complete();
            if(channel != null){
                channel.sendMessage(message).queue();
            }
        }
    }
    public static void sendDM(User user, CustomEmbed embed){
        if(user.getIdLong() != DiscordAuthOld.getBot().getSelfUser().getIdLong()){
            PrivateChannel channel = user.openPrivateChannel().complete();
            if(channel != null){
                channel.sendMessageEmbeds(embed.build()).complete();
            }
        }
    }

    /**
     * Initialize users that are not initialized
     * @param users User list
     */
    public static void initializeNoInitializedUsers(List<User> users){
        AccountTable accountTable = DiscordAuthOld.getAccountTable();
        for(User user: new ArrayList<>(users)){
            if(accountTable.isDiscordUserExist(user.getIdLong()) == Status.NotExist){
                sendDM(user, "You has accepted rules, now you can create your account by using: **/createaccount <minecraft username> <password>**.");
            }
        }
    }

    public static TextChannel getDiscordServerChannel(){
        return DiscordAuthOld.getBot().getTextChannelById(DiscordAuthOld.getConfiguration().getChannelId());
    }

    public static void setDiscordServerChannelTopic(String topic){
        setDiscordServerChannelTopicComplete(topic);
    }

    public static void setDiscordServerChannelTopicComplete(String topic){
        try {
            BotUtils.getDiscordServerChannel().getManager().setTopic(topic).complete(false);
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }
    }

    public static void setDiscordServerChannelTopicQueue(String topic){
        BotUtils.getDiscordServerChannel().getManager().setTopic(topic).queue();
    }

    public static String[] getCommandArgs(String command){
        String[] args = command.split(" ").clone();
        if(args.length >= 1) {
            return Arrays.copyOfRange(args, 1, args.length);
        }
        return new String[]{};
    }

    public static PrivateChannel getUserPrivateChannelFromId(long id){
        return DiscordAuthOld.getBot().getSelfUser().getJDA().openPrivateChannelById(id).complete();
    }

}
