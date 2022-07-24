package fr.xen0xys.discordauth.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class AdminAccountSlashCommand {

    private final CommandData commandData;

    public AdminAccountSlashCommand(JDA api){
        // Create
        SubcommandData createCommandData = new SubcommandData("create", "Create user's account");
        createCommandData.addOption(OptionType.STRING, "id", "User's discord id", true);
        createCommandData.addOption(OptionType.STRING, "username", "User's Minecraft username", true);
        createCommandData.addOption(OptionType.STRING, "password", "User's account password", true);
        // Manage
        SubcommandData manageCommandData = new SubcommandData("manage", "Manage user's account");
        manageCommandData.addOption(OptionType.STRING, "id", "User's discord id", true);
        manageCommandData.addOption(OptionType.STRING, "password", "User's new account password", false);
        // Delete
        SubcommandData deleteCommandData = new SubcommandData("delete", "Delete user's account");
        deleteCommandData.addOption(OptionType.STRING, "id", "User's discord id", true);
        // Create command data
        this.commandData = Commands.slash("adminaccount", "Create your account").addSubcommands(
                createCommandData, manageCommandData, deleteCommandData);
    }

    public CommandData getCommandData(){
        return this.commandData;
    }

}
