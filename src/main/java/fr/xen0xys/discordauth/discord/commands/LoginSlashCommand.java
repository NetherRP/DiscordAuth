package fr.xen0xys.discordauth.discord.commands;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class LoginSlashCommand {

    private final CommandData commandData;

    public LoginSlashCommand(){
        this.commandData = Commands.slash("login", "Create your account");
    }

    public CommandData getCommandData(){
        return this.commandData;
    }

}
