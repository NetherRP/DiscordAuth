package fr.xen0xys.discordauth_old.discord.events;

import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.discord.embeds.HelpEmbed;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.utils.PluginUtils;
import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnMessageReceived extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(event.getChannel().getType() == ChannelType.PRIVATE){
            Message message = event.getMessage();
            User user = event.getAuthor();
            PrivateChannel channel = (PrivateChannel) event.getChannel();
            if(message.getContentRaw().startsWith("/createaccount")) {
                // Create account command
                //if(BotUtils.isUserHasReact(user)){
                if(true){
                    String[] args = PluginUtils.getCommandArgs(message.getContentRaw());
                    if(args.length == 2){
                        Status status = Commands.createAccount(args[0], user.getIdLong(), args[1]);
                        switch (status){
                            // case Success -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.OK, DiscordAuthOld.getLanguage().accountCreatedSuccessful), channel);
                            // case Exist -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuthOld.getLanguage().alreadyHasAccount), channel);
                            // case SQLError -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuthOld.getLanguage().errorOccurred), channel);
                            // case Invalid -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuthOld.getLanguage().passwordDefineError), channel);
                        }
                    }else{
                        BotUtils.sendEmbed(new HelpEmbed(), channel);
                    }
                }else{
                    // BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuthOld.getLanguage().noReaction), channel);
                }
            }else if(message.getContentRaw().startsWith("/login")){
                // Login command
                Status status = Commands.login(user.getIdLong());
                switch (status){
                    // case Success -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.OK, DiscordAuthOld.getLanguage().loginSuccess), channel);
                    // case Error -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuthOld.getLanguage().playerNotConnected), channel);
                    // case NotExist -> BotUtils.sendEmbed(new MessageEmbed(StatusColor.Error, DiscordAuthOld.getLanguage().accountNotExist), channel);
                }
            }else if(message.getContentRaw().startsWith("/")){
                BotUtils.sendEmbed(new HelpEmbed(), channel);
            }
        }else{
            if(!event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())){
                // Check if shared chat is enabled
                if(DiscordAuthOld.getConfiguration().getEnableSharedChat() && !event.getAuthor().isBot()){
                    if(event.getChannelType() == ChannelType.TEXT){
                        TextChannel channel = (TextChannel) event.getChannel();
                        if(channel.getIdLong() == DiscordAuthOld.getConfiguration().getChannelId()){
                            AccountTable accountTable = DiscordAuthOld.getAccountTable();
                            String minecraftName = accountTable.getMinecraftNameFromDiscordId(event.getAuthor().getIdLong());
                            // If user exist
                            if(minecraftName != null){
                                // Bukkit.broadcastMessage(String.format("%s<%s>%s %s", ChatColor.BLUE, minecraftName, ChatColor.RESET, event.getMessage().getContentRaw()));
                                // Bukkit.broadcastMessage(String.format(DiscordAuthOld.getLanguage().registeredChat, minecraftName, event.getMessage().getContentRaw()));
                                // Bukkit.broadcastMessage(ChatColor.BLUE + "<" + minecraftName + "> " + ChatColor.RESET + event.getMessage().getContentRaw());
                            }else{
                                //Bukkit.broadcastMessage(String.format("%s[%s] %s<%s>%s %s", ChatColor.GOLD, DiscordAuth.getLanguage().unregistered, ChatColor.BLUE, event.getAuthor().getName(), ChatColor.RESET, event.getMessage().getContentRaw()));
                                // Bukkit.broadcastMessage(String.format(DiscordAuthOld.getLanguage().unregisteredChat, event.getAuthor().getName(), event.getMessage().getContentRaw()));
                            }
                        }
                    }
                }
            }
        }
    }
}
