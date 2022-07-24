package fr.xen0xys.discordauth_old.discord.embeds;

import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth.utils.PluginUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;

public class PlayerJoinEmbed extends EmbedBuilder implements CustomEmbed{
    public PlayerJoinEmbed(Player player){
        this.setColor(Color.GREEN);
        this.setAuthor(String.format(DiscordAuthOld.getLanguage().playerConnect, player.getName()), null, PluginUtils.getPlayerSkinURL(player));
    }
}
