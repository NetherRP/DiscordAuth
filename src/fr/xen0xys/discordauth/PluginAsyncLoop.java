package fr.xen0xys.discordauth;

import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.bot.embeds.HelpEmbed;
import fr.xen0xys.discordauth.databases.AccountsDatabase;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PluginAsyncLoop extends BukkitRunnable {

    private boolean is_running = true;
    private final List<User> discord_users = new ArrayList<>();

    private void sendAccountMessageToUnsendUsers(){
        List<User> new_discord_users = BotUtils.getUserWhoReact();
        AccountsDatabase accounts_database = new AccountsDatabase();
        if(new_discord_users != null){
            for(User user: new_discord_users){
                if(!this.discord_users.contains(user)){
                    if(!accounts_database.isDiscordUserExist(user.getIdLong())){
                        BotUtils.sendDM(user, "You has accepted rules, now you can create your account by using: **/createaccount <minecraft username> <password> <password confirmation>**.");
                        BotUtils.sendDM(user, new HelpEmbed());
                    }
                    this.discord_users.add(user);
                }
            }
        }
    }

    @Override
    public void run() {
        int round = 0;
        while(is_running){
            // Code here
            if(DiscordAuth.getConfigurationManager().getSendMessageToUnregisterUsers())
                sendAccountMessageToUnsendUsers();
            this.custom_wait();
            if(round < Integer.MAX_VALUE){
                round++;
            }else{
                round = 0;
            }
        }
        round = -1;
    }

    private void custom_wait(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.is_running = false;
        this.cancel();
    }
}
