package fr.xen0xys.discordauth.bot.embeds;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class HelpEmbed extends EmbedBuilder implements CustomEmbed{
    public HelpEmbed(){
        this.setColor(Color.BLUE);
        this.setAuthor("Commandes:");
        this.addField("/createaccount", "Crée votre compte; Arguments: <pseudo MC> <mot de passe> <confirmation mot de passe>", false);
        this.addField("/createspectator", "Crée votre compte spectateur; Arguments: <pseudo MC> <mot de passe> <confirmation mot de passe>", false);
        this.addField("/login", "Permet de vous connecter; Arguments: <mot de passe>", false);
        this.addField("/Help", "Affiche ce message; Arguments: <mot de passe>", false);
    }
}
