package fr.xen0xys.discordauth.utils;

import com.google.common.hash.Hashing;
import fr.xen0xys.discordauth.DiscordAuth;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

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
}
