package fr.xen0xys.discordauth.bot.commands;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.databases.AccountsDatabase;
import fr.xen0xys.discordauth.databases.SecurityDatabase;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LoginBotCommand {
    public LoginBotCommand(User user, Message message){
        String[] args = BotUtils.getCommandArgs(message.getContentRaw());
        if(args.length >= 1){
            String minecraftName = new AccountsDatabase().getMinecraftNameFromDiscordId(user.getIdLong());
            if(minecraftName != null){
                Player player = Bukkit.getPlayer(minecraftName);
                if(player != null){
                    if(new SecurityDatabase().checkPassword(minecraftName, args[0])){
                        DiscordAuth.getUsers().get(minecraftName).setIsLogged(true);
                        player.sendMessage(ChatColor.GREEN + "You has been connected (Discord)");
                        BotUtils.sendDM(user, "Connected successful");
                        return;
                    }
                }
            }
            BotUtils.sendDM(user, "An error occurred");
        }else{
            BotUtils.sendDM(user, "Not enough arguments");
        }
    }
}
