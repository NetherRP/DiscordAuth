package fr.xen0xys.discordauth;

import fr.xen0xys.discordauth.bot.BotUtils;
import fr.xen0xys.discordauth.bot.embeds.AdvancementEmbed;
import fr.xen0xys.discordauth.bot.embeds.HelpEmbed;
import fr.xen0xys.discordauth.databases.AccountsDatabase;
import fr.xen0xys.discordauth.models.CustomAdvancement;
import fr.xen0xys.discordauth.utils.PluginUtils;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginAsyncLoop extends BukkitRunnable {

    private boolean is_running = true;
    private final List<User> discord_users = new ArrayList<>();
    private final List<CustomAdvancement> advancements = new ArrayList<>();
    private int last_line_number = 0;

    private void sendAccountMessageToUnsendUsers(){
        List<User> new_discord_users = BotUtils.getUserWhoReact();
        AccountsDatabase accounts_database = new AccountsDatabase();
        if(new_discord_users != null){
            for(User user: new_discord_users){
                if(!this.discord_users.contains(user)){
                    if(!accounts_database.isDiscordUserExist(user.getIdLong())){
                        BotUtils.sendDM(user, "Vous avez accepté le règlement, vous pouvez maintenant créer votre compte en envoyant la commande **/createaccount** comme indiqué ci-dessous.");
                        BotUtils.sendDM(user, new HelpEmbed());
                    }
                    this.discord_users.add(user);
                }
            }
        }
    }

    private void getAdvancements(){
        // Get String list with lines to check
        String content = PluginUtils.getLatestLogContent();
        String[] lines = content.split("\n");
        if(last_line_number <= lines.length){
            List<String> lines_to_try = new ArrayList<>(Arrays.asList(lines).subList(last_line_number, lines.length));
            last_line_number = lines.length;
            // Fill CustomAdvancement list
            for(String line: lines_to_try){
                if(line.contains("has made the advancement") || line.contains("has reached the goal") || line.contains("has completed the challenge")){
                    advancements.add(new CustomAdvancement(line));
                }
            }
        }
    }

    private void sendAdvancements() {
        // Anti-lag advancement sending
        List<CustomAdvancement> local_advancements = new ArrayList<>(advancements); // Read only list
        List<Player> players = new ArrayList<>();
        for (CustomAdvancement advancement : local_advancements) {
            if (!players.contains(advancement.getPlayer())) {
                players.add(advancement.getPlayer());
            }
        }
        for (Player player : players) {
            List<String> player_advancements_string = new ArrayList<>();
            // Get player advancements
            for (CustomAdvancement advancement : local_advancements) {
                if (advancement.getPlayer() == player) {
                    player_advancements_string.add(advancement.getAdvancementName());
                    advancements.remove(advancement);
                }
            }
            // Actually not send embed if there is too many advancements
            if (player_advancements_string.size() == 1) {
                BotUtils.sendEmbed(new AdvancementEmbed(player, player_advancements_string.get(0)));
            } else if (player_advancements_string.size() <= 15) {
                BotUtils.sendEmbed(new AdvancementEmbed(player, player_advancements_string));
            }else{
                BotUtils.sendEmbed(new AdvancementEmbed(player, "{Too many advancements to display}"));
            }
        }
    }

    @Override
    public void run() {
        int round = 0;
        while(is_running){
            if(DiscordAuth.getConfigurationManager().getSendMessageToUnregisterUsers())
                sendAccountMessageToUnsendUsers();
            if(DiscordAuth.getConfigurationManager().getSendAdvancements()){
                if(round % 5 == 0){
                    getAdvancements();
                }
                if(round % 2 == 0){
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
        this.is_running = false;
        this.cancel();
    }
}
