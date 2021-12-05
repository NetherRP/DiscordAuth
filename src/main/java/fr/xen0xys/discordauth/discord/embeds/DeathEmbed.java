package fr.xen0xys.discordauth.discord.embeds;

import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;

public class DeathEmbed extends EmbedBuilder implements CustomEmbed{

    public DeathEmbed(Player player, String death_message){
        this.setColor(Color.GRAY);
        this.setAuthor(String.format(death_message, player.getName()), null, PluginUtils.getPlayerSkinURL(player));
    }
}
