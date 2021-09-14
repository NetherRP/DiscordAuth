package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.database.AccountTable;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class OnMessageReceived extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(!event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())){
            // Check if shared chat is enabled
            if(DiscordAuth.getConfiguration().getEnableSharedChat()){
                if(event.getChannelType() == ChannelType.TEXT){
                    TextChannel channel = (TextChannel) event.getChannel();
                    if(channel.getIdLong() == DiscordAuth.getConfiguration().getChannelId()){
                        AccountTable accountTable = DiscordAuth.getAccountTable();
                        String minecraftName = accountTable.getMinecraftNameFromDiscordId(event.getAuthor().getIdLong());
                        // If user exist
                        if(minecraftName != null){
                            // Bukkit.broadcastMessage(String.format("%s<%s>%s %s", ChatColor.BLUE, minecraftName, ChatColor.RESET, event.getMessage().getContentRaw()));
                            Bukkit.broadcastMessage(String.format(DiscordAuth.getLanguage().registeredChat, minecraftName, event.getMessage().getContentRaw()));
                            // Bukkit.broadcastMessage(ChatColor.BLUE + "<" + minecraftName + "> " + ChatColor.RESET + event.getMessage().getContentRaw());
                        }else{
                            //Bukkit.broadcastMessage(String.format("%s[%s] %s<%s>%s %s", ChatColor.GOLD, DiscordAuth.getLanguage().unregistered, ChatColor.BLUE, event.getAuthor().getName(), ChatColor.RESET, event.getMessage().getContentRaw()));
                            Bukkit.broadcastMessage(String.format(DiscordAuth.getLanguage().unregisteredChat, event.getAuthor().getName(), event.getMessage().getContentRaw()));
                        }
                    }
                }
            }
        }
    }
}
