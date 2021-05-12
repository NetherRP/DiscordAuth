package fr.xen0xys.discordauth.models;

import org.bukkit.entity.Player;

public class SkinManager {


    public SkinManager(){
    }

    public String getPlayerSkinURL(Player player){
        String skinName = player.getName();
        return String.format("https://mc-heads.net/avatar/%s/100", skinName);
    }
}
