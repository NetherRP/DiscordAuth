package fr.xen0xys.discordauth.utils;

import com.google.common.hash.Hashing;
import fr.xen0xys.discordauth.DiscordAuth;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Scanner;

public abstract class PluginUtils {
    public static String getPlayerIP(Player player){
        String ip = player.getAddress().getAddress().toString().replace("/", "");
        if(DiscordAuth.getConfigurationManager().getEnableIpEncryption()){
            return encryptString(ip);
        }
        return ip;
    }

    public static long getCurrentTimestamp(){
        return new Timestamp(System.currentTimeMillis()).getTime();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static String encryptString(String string){
        if(DiscordAuth.getConfigurationManager().getEnableAdditionalEncryptionString()){
            string = string + DiscordAuth.getConfigurationManager().getAdditionalEncryptionString();
        }
        return Hashing.sha256().hashString(string, StandardCharsets.UTF_8).toString();
    }

    public static String getLatestLogContent(){
        try {
            File myObj = new File("logs/latest.log");
            Scanner myReader = new Scanner(myObj);
            StringBuilder content = new StringBuilder();
            while (myReader.hasNextLine()) {
                content.append(myReader.nextLine());
                content.append("\n");
            }
            myReader.close();
            return content.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "";
    }
}
