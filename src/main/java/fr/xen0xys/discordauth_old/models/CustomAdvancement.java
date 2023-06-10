package fr.xen0xys.discordauth_old.models;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CustomAdvancement {

    private final Player player;
    private String advancementName;

    public CustomAdvancement(String advancementLine){
        String[] bracketSplitArray = advancementLine.split("\\[");
        advancementName = bracketSplitArray[bracketSplitArray.length - 1].replace("]", "");
        advancementName = "[" + advancementName + "]";
        String[] array = bracketSplitArray[bracketSplitArray.length - 2].split(" ");
        player = Bukkit.getPlayer(array[array.length - 5]);
    }

    public Player getPlayer(){
        return this.player;
    }

    public String getAdvancementName(){
        return this.advancementName;
    }
}
