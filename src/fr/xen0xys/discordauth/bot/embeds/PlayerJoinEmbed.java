package fr.xen0xys.discordauth.bot.embeds;

import fr.xen0xys.discordauth.DiscordAuth;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;

import java.awt.*;

public class PlayerJoinEmbed extends EmbedBuilder implements CustomEmbed{
    public PlayerJoinEmbed(Player player){
        this.setColor(Color.GREEN);
        this.setAuthor(String.format("%s just connected!", player.getName()), null, DiscordAuth.getSkinManager().getPlayerSkinURL(player));
    }
}
