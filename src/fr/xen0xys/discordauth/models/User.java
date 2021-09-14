package fr.xen0xys.discordauth.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class User {

    private final Player player;
    private boolean isLogged = false;
    private Location loginLocation = null;

    public User(Player player, Location loginLocation){
        this.player = player;
        this.loginLocation = loginLocation;
    }

    public User(Player player, boolean isLogged){
        this.player = player;
        this.isLogged = isLogged;
    }

    // Getters
    public Player getPlayer() {
        return player;
    }
    public boolean isLogged() {
        return isLogged;
    }
    public Location getLoginLocation() {
        return loginLocation;
    }

    // Setters
    public void setIsLogged(boolean value){
        this.isLogged = value;
    }
    public void setLoginLocation(Location loginLocation) {
        this.loginLocation = loginLocation;
    }

    // Other
    @Deprecated
    public boolean checkPassword(){
        return false;
    }

    @Deprecated
    public void setPassword(){

    }


}
