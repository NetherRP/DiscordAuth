package fr.xen0xys.discordauth.bot.embeds;

import fr.xen0xys.discordauth.DiscordAuth;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;


public class AdvancementEmbed extends EmbedBuilder implements CustomEmbed{

    public AdvancementEmbed(Player player, String advancement_name){
        this.setColor(Color.ORANGE);
        this.setAuthor(String.format(String.format("%s à complété le succès: %s", player.getName(), advancement_name), player.getName()), null, DiscordAuth.getSkinManager().getPlayerSkinURL(player));
    }

    public AdvancementEmbed(Player player, String[] advancements_name){
        this.setColor(Color.ORANGE);
        StringBuilder final_advancement_string = new StringBuilder();
        for(String advancement_name: advancements_name){
            final_advancement_string.append(advancement_name).append(",");
        }
        this.setAuthor(String.format(String.format("%s à complété les succès: %s", player.getName(), final_advancement_string), player.getName()), null, DiscordAuth.getSkinManager().getPlayerSkinURL(player));
    }

    public AdvancementEmbed(Player player, List<String> advancements_name){
        this.setColor(Color.ORANGE);
        StringBuilder final_advancement_string = new StringBuilder();
        for(String advancement_name: advancements_name){
            if(advancements_name.indexOf(advancement_name) != advancements_name.size() - 1){
                final_advancement_string.append(advancement_name).append(", ");
            }else{
                final_advancement_string.append(advancement_name);
            }
        }
        this.setAuthor(String.format(String.format("%s à complété les succès: %s", player.getName(), final_advancement_string), player.getName()), null, DiscordAuth.getSkinManager().getPlayerSkinURL(player));
    }
}