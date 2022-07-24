package fr.xen0xys.discordauth_old.discord.embeds;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class HelpEmbed extends EmbedBuilder implements CustomEmbed{
    public HelpEmbed(){
        this.setColor(Color.BLUE);
        this.setAuthor("Commands:");
        this.addField("/createaccount", "Create your account; Arguments: <minecraft username> <password> <password confirmation>", false);
        this.addField("/login", "Connect yourself; Arguments: <password>", false);
        this.addField("/ip", "Connect yourself; Arguments: <allow | disallow | block | unblock | list> <x.x.x.x>", false);
        this.addField("/help", "Display this message; Arguments: <password>", false);
    }
}
