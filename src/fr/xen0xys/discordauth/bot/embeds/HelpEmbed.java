package fr.xen0xys.discordauth.bot.embeds;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class HelpEmbed extends EmbedBuilder implements CustomEmbed{
    public HelpEmbed(){
        this.setColor(Color.BLUE);
        this.setAuthor("Commands:");
        this.addField("/createaccount", "Crete your account; Arguments: <minecraft username> <password> <password confirmation>", false);
        this.addField("/login", "Connect yourself; Arguments: <password>", false);
        this.addField("/Help", "Display this message; Arguments: <password>", false);
    }
}
