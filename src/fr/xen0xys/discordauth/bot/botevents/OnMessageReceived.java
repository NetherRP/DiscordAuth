package fr.xen0xys.discordauth.bot.botevents;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.databases.AccountsDatabase;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class OnMessageReceived extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(!event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())){
            if(event.getChannelType() == ChannelType.TEXT){
                TextChannel channel = (TextChannel) event.getChannel();
                if(Long.parseLong(channel.getId()) == DiscordAuth.getChannelId()){
                    // BotUtils.sendMessage("You speak!");
                    String minecraft_name = new AccountsDatabase().getMinecraftNameFromDiscordId(event.getAuthor().getIdLong());
                    if(minecraft_name != null && DiscordAuth.getConfigurationManager().getEnableSharedChat()){
                        Bukkit.broadcastMessage(ChatColor.BLUE + "<" + minecraft_name + "> " + ChatColor.RESET + event.getMessage().getContentRaw());
                    }
                }
            }
        }
    }
}
