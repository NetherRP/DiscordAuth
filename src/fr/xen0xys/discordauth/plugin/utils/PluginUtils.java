package fr.xen0xys.discordauth.plugin.utils;

import com.google.common.primitives.Chars;
import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.xen0lib.gui.anvilgui.AnvilGUI;
import fr.xen0xys.xen0lib.utils.Status;
import fr.xen0xys.xen0lib.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public abstract class PluginUtils {

    public static List<Character> ipsChars = Chars.asList("0123456789.".toCharArray());

    public static void displayLoginScreen(Player player){
        AnvilGUI.Builder builder = new AnvilGUI.Builder()
                .plugin(DiscordAuth.getInstance())
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Mot de passe:")
                .text("➞");

        if(DiscordAuth.getConfiguration().getAggressiveLogin()){
            // builder.preventClose();
        }

        builder.onComplete((_player, text) -> {
            Status status = Commands.login(_player, text);
            if(status == Status.Valid){
                _player.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().loginSuccess);
                return AnvilGUI.Response.close();
            }else if(status == Status.Invalid){
                status = Commands.login(_player, text.substring(1));
                if(status == Status.Valid) {
                    _player.sendMessage(ChatColor.GREEN + DiscordAuth.getLanguage().loginSuccess);
                    return AnvilGUI.Response.close();
                }else if(status == Status.Invalid){
                    return AnvilGUI.Response.text(DiscordAuth.getLanguage().invalidPassword);
                }else{
                    return AnvilGUI.Response.text(DiscordAuth.getLanguage().errorOccurred);
                }
            }else{
                return AnvilGUI.Response.text(DiscordAuth.getLanguage().errorOccurred);
            }
        });

        builder.open(player);
    }

    public static String encryptPassword(String password){
        String additionalString = "";
        if(DiscordAuth.getConfiguration().getIsAdditionalString()){
            additionalString = DiscordAuth.getConfiguration().getAdditionalString();
        }
        return Utils.encryptString(password, additionalString);
    }

    public static String encryptIpIfNeeded(String ip){
        if(DiscordAuth.getConfiguration().getEncryptIp()){
            String additionalString = "";
            if(DiscordAuth.getConfiguration().getIsAdditionalString()){
                additionalString = DiscordAuth.getConfiguration().getAdditionalString();
            }

            return Utils.encryptString(ip, additionalString);
        }
        return ip;
    }

    public static String[] getCommandArgs(String commandLine){
        List<String> args = new java.util.ArrayList<>(List.of(commandLine.split(" ")));
        args.remove(0);
        return args.toArray(new String[0]);
    }

    public static String getPlayerSkinURL(Player player){
        String skinName = player.getName();
        return String.format("https://mc-heads.net/avatar/%s/100", skinName);
    }

    public static String serializeIps(String[] ips){
        StringBuilder finalString = new StringBuilder();
        for(String ip: ips){
            if(!ip.equals(""))
                finalString.append(ip).append(";");
        }
        if(!finalString.toString().equals("")){
            finalString.deleteCharAt(finalString.length() - 1);
        }
        return finalString.toString();
    }

    public static String[] concatenateTabs(String[] tab1, String[] tab2){
        List<String> array1 = new java.util.ArrayList<>(List.of(tab1));
        for(String string: tab2){
            if(!string.equals("")){
                if(!array1.contains(string)){
                    array1.add(string);
                }
            }
        }
        String[] finalIps = array1.toArray(new String[0]);
        Arrays.sort(finalIps);
        return finalIps;
    }

    public static boolean checkIpFormat(String ip){
        if(ip.length() > 15 || ip.length() < 7){
            return false;
        }
        for(char localIpChar: ip.toCharArray()){
            if(!ipsChars.contains(localIpChar)){
                return false;
            }
        }
        for(String strNumber: ip.split("\\.")){
            int number = Integer.parseInt(strNumber);
            if(number > 255){
                return false;
            }
        }
        return true;
    }

    public static boolean checkIpsFormat(String[] ips){
        for(String ip: ips){
            if(!checkIpFormat(ip))
                return false;
        }
        return true;
    }

}
