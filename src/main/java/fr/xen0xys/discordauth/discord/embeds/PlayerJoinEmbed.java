package fr.xen0xys.discordauth.discord.embeds;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;

public class PlayerJoinEmbed extends EmbedBuilder implements CustomEmbed{
    public PlayerJoinEmbed(Player player){
        this.setColor(Color.GREEN);
        this.setAuthor(String.format(DiscordAuth.getLanguage().playerConnect, player.getName()), null, PluginUtils.getPlayerSkinURL(player));
    }
}
