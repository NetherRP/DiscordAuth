package fr.xen0xys.discordauth.common.discord.components.commands;

import fr.xen0xys.discordauth.common.config.language.LangField;
import fr.xen0xys.discordauth.common.discord.components.buttons.RegisterButton;
import fr.xen0xys.discordjava.DJApp;
import fr.xen0xys.discordjava.components.commands.AbstractSlashCommand;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class DisplayRegisterCommand extends AbstractSlashCommand {

    public DisplayRegisterCommand() {
        super("register", LangField.REGISTER_COMMAND_DESCRIPTION.asText());
    }

    @Override
    public void callback(@NotNull DJApp djApp, @NotNull SlashCommandInteraction slashCommandInteraction) {
        slashCommandInteraction.getChannel().sendMessage(LangField.REGISTER_MESSAGE_CONTENT.asText()).addActionRow(new RegisterButton().getButton()).queue();
        slashCommandInteraction.deferReply(true).addContent(LangField.REGISTER_MESSAGE_ADDED.asText()).queue();
    }
}
