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
                    AccountsDatabase bot_database = new AccountsDatabase();
                    if(bot_database.createPlayerAccount(user.getIdLong(), args[0], args[1])){
                        BotUtils.sendDM(user, "Votre compte à été crée");
                    }else{
                        BotUtils.sendDM(user, "Votre compte n'a pas pu être crée");
                    }
                }else{
                    BotUtils.sendDM(user, "La confirmation du mot de passe n'est pas identique au mot de passe");
                }
            }else{
                BotUtils.sendDM(user, "Pas assez d'arguments");
            }
        }else{
            BotUtils.sendDM(user, "Vous n'avez pas réagi au message requis");
        }

    }
}
