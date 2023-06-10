package fr.xen0xys.discordauth.discord.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class GetIdSlashCommand {

    private final CommandData commandData;

    public GetIdSlashCommand(){
        this.commandData = Commands.slash("getid", "Create your account")
                .addOption(OptionType.STRING, "username", "User's Minecraft username", true);
    }

    public CommandData getCommandData(){
        return this.commandData;
    }

}
