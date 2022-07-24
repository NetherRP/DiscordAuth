package fr.xen0xys.discordauth_old.discord.embeds;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth.utils.PluginUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;


public class AdvancementEmbed extends EmbedBuilder implements CustomEmbed{

    public AdvancementEmbed(Player player, String advancementName){
        this.setColor(Color.ORANGE);
        this.setAuthor(String.format(String.format(DiscordAuthOld.getLanguage().successComplete, player.getName(), advancementName), player.getName()), null, PluginUtils.getPlayerSkinURL(player));
    }

    public AdvancementEmbed(Player player, String[] advancements_name){
        this.setColor(Color.ORANGE);
        StringBuilder finalAdvancementString = new StringBuilder();
        for(String advancementName: advancements_name){
            finalAdvancementString.append(advancementName).append(",");
        }
        this.setAuthor(String.format(String.format(DiscordAuthOld.getLanguage().successComplete, player.getName(), finalAdvancementString), player.getName()), null, PluginUtils.getPlayerSkinURL(player));
    }

    public AdvancementEmbed(Player player, List<String> advancements_name){
        this.setColor(Color.ORANGE);
        StringBuilder finalAdvancementString = new StringBuilder();
        for(String advancementName: advancements_name){
            if(advancements_name.indexOf(advancementName) != advancements_name.size() - 1){
                finalAdvancementString.append(advancementName).append(", ");
            }else{
                finalAdvancementString.append(advancementName);
            }
        }
        this.setAuthor(String.format(String.format(DiscordAuthOld.getLanguage().successComplete, player.getName(), finalAdvancementString), player.getName()), null, PluginUtils.getPlayerSkinURL(player));
    }
}