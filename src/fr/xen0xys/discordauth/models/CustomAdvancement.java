package fr.xen0xys.discordauth.models;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CustomAdvancement {

    private final Player player;
    private String advancement_name;

    public CustomAdvancement(String advancement_line){
        String[] bracket_split_array = advancement_line.split("\\[");
        advancement_name = bracket_split_array[bracket_split_array.length - 1].replace("]", "");
        advancement_name = "[" + advancement_name + "]";
        String[] array = bracket_split_array[bracket_split_array.length - 2].split(" ");
        player = Bukkit.getPlayer(array[array.length - 5]);
    }

    public Player getPlayer(){
        return this.player;
    }

    public String getAdvancementName(){
        return this.advancement_name;
    }
}
