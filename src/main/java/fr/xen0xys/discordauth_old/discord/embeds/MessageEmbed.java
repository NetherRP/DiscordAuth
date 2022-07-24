package fr.xen0xys.discordauth_old.discord.embeds;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth_old.plugin.utils.StatusColor;
import net.dv8tion.jda.api.EmbedBuilder;

public class MessageEmbed extends EmbedBuilder implements CustomEmbed{

    public MessageEmbed(StatusColor statusColor, String message){
        this.setColor(statusColor.getColor());
        if(DiscordAuthOld.getConfiguration().isLogoEnabled()){
            this.setAuthor(message, null, DiscordAuthOld.getConfiguration().getLogo());
        }else{
            this.setAuthor(message);
        }
    }

}
