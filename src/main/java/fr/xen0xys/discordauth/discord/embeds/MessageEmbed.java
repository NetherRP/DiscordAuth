package fr.xen0xys.discordauth.discord.embeds;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.utils.StatusColor;
import net.dv8tion.jda.api.EmbedBuilder;

public class MessageEmbed extends EmbedBuilder implements CustomEmbed{

    public MessageEmbed(StatusColor statusColor, String message){
        this.setColor(statusColor.getColor());
        if(DiscordAuth.getConfiguration().isLogoEnabled()){
            this.setAuthor(message, null, DiscordAuth.getConfiguration().getLogo());
        }else{
            this.setAuthor(message);
        }
    }

}
