package fr.xen0xys.discordauth.bot.embeds;

import fr.xen0xys.discordauth.DiscordAuth;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;

public class PlayerQuitEmbed extends EmbedBuilder implements CustomEmbed{
    public PlayerQuitEmbed(Player player){
        this.setColor(Color.RED);
        this.setAuthor(String.format("%s just disconnected!", player.getName()), null, DiscordAuth.getSkinManager().getPlayerSkinURL(player));
    }
}
