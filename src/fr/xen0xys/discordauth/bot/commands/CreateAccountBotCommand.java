package fr.xen0xys.discordauth.bot.commands;

import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.databases.AccountsDatabase;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

public class CreateAccountBotCommand {
    public CreateAccountBotCommand(User user, Message message){
        if(BotUtils.isUserHasReact(user)){
            String[] args = message.getContentRaw().split(" ").clone();
            if(args.length >= 4) {
                args = Arrays.copyOfRange(args, 1, args.length);
                if(args[1].equals(args[2])) {
                    AccountsDatabase botDatabase = new AccountsDatabase();
                    if(botDatabase.createPlayerAccount(user.getIdLong(), args[0], args[1])){
                        BotUtils.sendDM(user, "Your account has been created");
                    }else{
                        BotUtils.sendDM(user, "Your account hasn't been created");
                    }
                }else{
                    BotUtils.sendDM(user, "Password confirmation and password has not identical");
                }
            }else{
                BotUtils.sendDM(user, "Not enough arguments");
            }
        }else{
            BotUtils.sendDM(user, "You don't have react to requested message");
        }

    }
}
