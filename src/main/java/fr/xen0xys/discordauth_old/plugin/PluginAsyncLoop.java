package fr.xen0xys.discordauth_old.plugin;

import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth_old.DiscordAuthOld;
import fr.xen0xys.discordauth_old.models.CustomAdvancement;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PluginAsyncLoop extends BukkitRunnable {

    private boolean isRunning = true;
    private final List<User> discordUsers = new ArrayList<>();
    private final List<CustomAdvancement> advancements = new ArrayList<>();
    private int lastLineNumber = 0;

    private void sendAccountMessageToUnsendUsers(){
        List<User> newDiscordUsers = BotUtils.getUserWhoReact();
        AccountTable accountsDatabase = DiscordAuthOld.getAccountTable();
        if(newDiscordUsers != null){
            for(User user: newDiscordUsers){
                if(!this.discordUsers.contains(user)){
                    if(accountsDatabase.isDiscordUserExist(user.getIdLong()) == Status.NotExist){
                        BotUtils.sendDM(user, DiscordAuthOld.getLanguage().acceptMessage);
                    }
                    this.discordUsers.add(user);
                }
            }
        }
    }

    @Override
    public void run() {
        int round = 0;
        while(isRunning){
            if(DiscordAuthOld.getConfiguration().getSendMessageToUnregisterUsers())
                sendAccountMessageToUnsendUsers();
            this.custom_wait();
            if(round < Integer.MAX_VALUE){
                round++;
            }else{
                round = 0;
            }
        }
    }

    private void custom_wait(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.isRunning = false;
        this.cancel();
    }
}
