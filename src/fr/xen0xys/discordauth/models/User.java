package fr.xen0xys.discordauth.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class User {

    private final Player player;
    private boolean isLogged = false;
    private Location loginLocation;

    public User(Player player, Location loginLocation){
        this.player = player;
        this.loginLocation = loginLocation;
    }

    public Player getPlayer(){
        return this.player;
    }
    public boolean isLogged(){
        return this.isLogged;
    }
    public Location getLoginLocation(){
        return this.loginLocation;
    }

    public void setIsLogged(boolean value){
        isLogged = value;
    }
    public void setLoginLocation(Location location){
        loginLocation = location;
    }

}
