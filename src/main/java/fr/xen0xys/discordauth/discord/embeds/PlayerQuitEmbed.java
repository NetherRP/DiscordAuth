package fr.xen0xys.discordauth.discord.embeds;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.plugin.utils.PluginUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;

public class PlayerQuitEmbed extends EmbedBuilder implements CustomEmbed{
    public PlayerQuitEmbed(Player player){
        this.setColor(Color.RED);
        this.setAuthor(String.format(DiscordAuth.getLanguage().playerDisconnect, player.getName()), null, PluginUtils.getPlayerSkinURL(player));
    }
}
