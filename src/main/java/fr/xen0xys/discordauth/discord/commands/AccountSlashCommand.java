package fr.xen0xys.discordauth.discord.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AccountSlashCommand {

    private final CommandData commandData;

    public AccountSlashCommand(){
        // Create
        SubcommandData createCommandData = new SubcommandData("create", "Create your account");
        createCommandData.addOption(OptionType.STRING, "username", "Your Minecraft username", true);
        createCommandData.addOption(OptionType.STRING, "password", "Your account password", true);
        createCommandData.addOption(OptionType.STRING, "confirm", "Confirm your account password", true);
        // Manage
        SubcommandData manageCommandData = new SubcommandData("manage", "Manage your account");
        manageCommandData.addOption(OptionType.STRING, "password", "Your new account password", false);
        // Delete
        SubcommandData deleteCommandData = new SubcommandData("delete", "Delete your account");
        // Create command data
        this.commandData = Commands.slash("account", "Create your account").addSubcommands(
                createCommandData, manageCommandData, deleteCommandData);
    }

    public CommandData getCommandData(){
        return this.commandData;
    }

}
