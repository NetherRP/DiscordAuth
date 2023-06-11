package fr.xen0xys.discordauth.common.discord.components.commands;

import fr.xen0xys.discordauth.common.discord.components.buttons.RegisterButton;
import fr.xen0xys.discordjava.DJApp;
import fr.xen0xys.discordjava.components.commands.AbstractSlashCommand;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class DisplayRegisterCommand extends AbstractSlashCommand {

    public DisplayRegisterCommand() {
        super("register", "Display register button");
    }

    @Override
    public void callback(@NotNull DJApp djApp, @NotNull SlashCommandInteraction slashCommandInteraction) {
        slashCommandInteraction.getChannel().sendMessage("Register").addActionRow(new RegisterButton().getButton()).queue();
        slashCommandInteraction.deferReply(true).addContent("Message added").queue();
    }
}
