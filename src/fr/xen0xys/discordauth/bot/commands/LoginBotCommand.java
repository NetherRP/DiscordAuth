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
            String minecraft_name = new AccountsDatabase().getMinecraftNameFromDiscordId(user.getIdLong());
            if(minecraft_name != null){
                Player player = Bukkit.getPlayer(minecraft_name);
                if(player != null){
                    if(new SecurityDatabase().checkPassword(minecraft_name, args[0])){
                        DiscordAuth.getUsers().get(minecraft_name).setIsLogged(true);
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
