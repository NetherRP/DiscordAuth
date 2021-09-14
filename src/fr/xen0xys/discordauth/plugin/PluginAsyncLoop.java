package fr.xen0xys.discordauth.plugin;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.BotUtils;
import fr.xen0xys.discordauth.discord.embeds.AdvancementEmbed;
import fr.xen0xys.discordauth.models.CustomAdvancement;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.xen0lib.utils.Status;
import fr.xen0xys.xen0lib.utils.Utils;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginAsyncLoop extends BukkitRunnable {

    private boolean isRunning = true;
    private final List<User> discordUsers = new ArrayList<>();
    private final List<CustomAdvancement> advancements = new ArrayList<>();
    private int lastLineNumber = 0;

    private void sendAccountMessageToUnsendUsers(){
        List<User> newDiscordUsers = BotUtils.getUserWhoReact();
        AccountTable accountsDatabase = DiscordAuth.getAccountTable();
        if(newDiscordUsers != null){
            for(User user: newDiscordUsers){
                if(!this.discordUsers.contains(user)){
                    if(accountsDatabase.isDiscordUserExist(user.getIdLong()) == Status.NotExist){
                        BotUtils.sendDM(user, DiscordAuth.getLanguage().acceptMessage);
                    }
                    this.discordUsers.add(user);
                }
            }
        }
    }

    private void getAdvancements(){
        // Get String list with lines to check
        String content = Utils.getLatestLogContent();
        String[] lines = content.split("\n");
        if(lastLineNumber <= lines.length){
            List<String> linesToTry = new ArrayList<>(Arrays.asList(lines).subList(lastLineNumber, lines.length));
            lastLineNumber = lines.length;
            // Fill CustomAdvancement list
            for(String line: linesToTry){
                if(line.contains("has made the advancement") || line.contains("has reached the goal") || line.contains("has completed the challenge")){
                    advancements.add(new CustomAdvancement(line));
                }
            }
        }
    }

    private void sendAdvancements() {
        // Anti-lag advancement sending
        List<CustomAdvancement> localAdvancements = new ArrayList<>(advancements); // Read only list
        List<Player> players = new ArrayList<>();
        for (CustomAdvancement advancement : localAdvancements) {
            if (!players.contains(advancement.getPlayer())) {
                players.add(advancement.getPlayer());
            }
        }
        for (Player player : players) {
            List<String> playerAdvancementsString = new ArrayList<>();
            // Get player advancements
            for (CustomAdvancement advancement : localAdvancements) {
                if (advancement.getPlayer() == player) {
                    playerAdvancementsString.add(advancement.getAdvancementName());
                    advancements.remove(advancement);
                }
            }
            // Actually not send embed if there is too many advancements
            if (playerAdvancementsString.size() == 1) {
                BotUtils.sendEmbed(new AdvancementEmbed(player, playerAdvancementsString.get(0)));
            } else if (playerAdvancementsString.size() <= 15) {
                BotUtils.sendEmbed(new AdvancementEmbed(player, playerAdvancementsString));
            }else{
                BotUtils.sendEmbed(new AdvancementEmbed(player, String.format("{%s}", DiscordAuth.getLanguage().tooManyAdvancements)));
            }
        }
    }

    @Override
    public void run() {
        int round = 0;
        while(isRunning){
            if(DiscordAuth.getConfiguration().getSendMessageToUnregisterUsers())
                sendAccountMessageToUnsendUsers();
            if(DiscordAuth.getConfiguration().getSendAdvancements()){
                if(round % 2 == 0){
                    getAdvancements();
                    sendAdvancements();
                }
            }
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
