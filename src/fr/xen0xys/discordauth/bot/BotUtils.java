package fr.xen0xys.discordauth.bot;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.bot.embeds.CustomEmbed;
import fr.xen0xys.discordauth.databases.AccountsDatabase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BotUtils {
    public static void sendMessage(String message){
        TextChannel channel = DiscordAuth.getBot().getTextChannelById(DiscordAuth.getChannelId());
        if(channel != null){
            channel.sendTyping().complete();
            channel.sendMessage(message).complete();
        }
    }

    public static void sendEmbed(CustomEmbed embed){
        TextChannel channel = DiscordAuth.getBot().getTextChannelById(DiscordAuth.getChannelId());
        if(channel != null){
            channel.sendTyping().complete();
            channel.sendMessage(embed.build()).complete();
        }
    }

    public static String getServerStartMessage(){
        return ":white_check_mark: **Server just started**";
    }
    public static String getServerStopMessage(){
        return ":octagonal_sign: **Server just stopped**";
    }

    public static List<User> getUserWhoReact(){
        Message message = retrieveMessageFromId(DiscordAuth.getGuildId(), DiscordAuth.getMessageId());
        if(message != null){
            reactToMessage(message);
            for(MessageReaction mr: message.getReactions()){
                if(mr.getReactionEmote().toString().split(":")[1].equals(DiscordAuth.getConfigurationManager().getReactionName())){
                    List<User> users = mr.retrieveUsers().complete();
                    users.remove(DiscordAuth.getBot().getSelfUser());
                    return users;
                }
            }
        }
        return null;
    }

    public static boolean isUserHasReact(User target_user){
        List<User> react_user_list = getUserWhoReact();
        if (react_user_list != null) {
            for(User user: react_user_list){
                if(user == target_user){
                    return true;
                }
            }
        }
        return false;
    }

    public static Message retrieveMessageFromId(long guild_id, long message_id){
        Guild guild = DiscordAuth.getBot().getGuildById(DiscordAuth.getGuildId());
        if(guild != null){
            for(Category category: guild.getCategories()) {
                for(TextChannel channel: category.getTextChannels()){
                    try{
                        return channel.retrieveMessageById(message_id).complete();
                    } catch(ErrorResponseException ignored){

                    }
                }
            }
        }
        return null;
    }

    public static void reactToMessage(Message message){
        message.addReaction(DiscordAuth.getConfigurationManager().getReactionName()).complete();
    }

    public static Member getMemberFromUser(User user){
        Guild guild = DiscordAuth.getBot().getGuildById(DiscordAuth.getGuildId());
        if(guild != null){
            return guild.getMemberById(user.getIdLong());
        }
        return null;
    }

    public static void sendDM(User user, String message){
        if(user.getIdLong() != DiscordAuth.getBot().getSelfUser().getIdLong()){
            PrivateChannel channel = user.openPrivateChannel().complete();
            if(channel != null){
                channel.sendMessage(message).queue();
            }
        }
    }
    public static void sendDM(User user, CustomEmbed embed){
        if(user.getIdLong() != DiscordAuth.getBot().getSelfUser().getIdLong()){
            PrivateChannel channel = user.openPrivateChannel().complete();
            if(channel != null){
                channel.sendMessage(embed.build()).queue();
            }
        }
    }

    public static void initializeNoInitializedUsers(List<User> users){
        AccountsDatabase bot_database = new AccountsDatabase();
        for(User user: new ArrayList<>(users)){
            if(!bot_database.isDiscordUserExist(user.getIdLong())){
                sendDM(user, "You has accepted rules, now you can create your account by using: **/createaccount <minecraft username> <password> <password confirmation>**.");
            }
        }
    }

    public static TextChannel getDiscordServerChannel(){
        return DiscordAuth.getBot().getTextChannelById(DiscordAuth.getChannelId());
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

}
