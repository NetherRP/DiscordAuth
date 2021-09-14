package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.discord.embeds.HelpEmbed;
import fr.xen0xys.discordauth.discord.embeds.IpListEmbed;
import fr.xen0xys.discordauth.discord.embeds.MessageEmbed;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
import fr.xen0xys.discordauth.plugin.utils.StatusColor;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OnPrivateMessageReceived extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        super.onPrivateMessageReceived(event);
        Message message = event.getMessage();
        User user = event.getAuthor();
        PrivateChannel channel = event.getChannel();
        if(message.getContentRaw().startsWith("/createaccount")) {
            // Create account command
            if(BotUtils.isUserHasReact(user)){
                String[] args = PluginUtils.getCommandArgs(message.getContentRaw());
                if(args.length == 2){
                    Status status = Commands.createAccount(args[0], user.getIdLong(), args[1]);
                    switch (status){
                        case Success -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().accountCreatedSuccessful), channel);
                        case Exist -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().alreadyHasAccount), channel);
                        case SQLError -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().errorOccurred), channel);
                    }
                }else{
                    BotUtils.sendEmbed(new HelpEmbed());
                }
            }else{
                BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().noReaction), channel);
            }
        }else if(message.getContentRaw().startsWith("/login")){
            // Login command
            Status status = Commands.login(user.getIdLong());
            switch (status){
                case Success -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().loginSuccess), channel);
                case Error -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().playerNotConnected), channel);
                case NotExist -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().accountNotExist), channel);
            }
        }else if(message.getContentRaw().startsWith("/ip")){
            String[] args = PluginUtils.getCommandArgs(message.getContentRaw());
            if(args.length >= 1){
                String minecraftName = DiscordAuth.getAccountTable().getMinecraftNameFromDiscordId(user.getIdLong());
                UUID uuid = UUID.fromString(DiscordAuth.getAccountTable().getUUIDFromMinecraftName(minecraftName));
                if(args[0].equals("list")){
                    BotUtils.sendEmbed(new IpListEmbed(uuid, minecraftName, true), channel);
                    BotUtils.sendEmbed(new IpListEmbed(uuid, minecraftName, false), channel);
                }else if(args.length >= 2){
                    Status status = Commands.ipCommand(uuid, minecraftName, args);
                    switch (status){
                        case Success -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().actionSuccess), channel);
                        case Error -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().errorOccurred), channel);
                        case NotExist -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().needLogin), channel);
                        case Invalid -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().invalidIpFormat), channel);
                    }
                }else{
                    BotUtils.sendEmbed(new HelpEmbed());
                }
            }
        }else if(message.getContentRaw().startsWith("/")){
            BotUtils.sendEmbed(new HelpEmbed());
        }
    }

}
