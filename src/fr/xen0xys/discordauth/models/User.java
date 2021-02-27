package fr.xen0xys.discordauth.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class User {

    private final Player player;
    private boolean is_logged = false;
    private Location login_location;

    public User(Player player, Location login_location){
        this.player = player;
        this.login_location = login_location;
    }

    public Player getPlayer(){
        return this.player;
    }
    public boolean isLogged(){
        return this.is_logged;
    }
    public Location getLoginLocation(){
        return this.login_location;
    }

    public void setIsLogged(boolean value){
        is_logged = value;
    }
    public void setLoginLocation(Location location){
        login_location = location;
    }

}
