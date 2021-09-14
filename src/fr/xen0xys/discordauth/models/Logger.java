package fr.xen0xys.discordauth.models;

import org.bukkit.ChatColor;

public class Logger {

    private final String prefix;

    public Logger(String prefix){
        this.prefix = prefix;
    }

    private String getFullPrefix(){
        return String.format("[%s] ", this.prefix);
    }

    public void log(String log){
        System.out.println(this.getFullPrefix() + log);
    }

    public void warn(String log){
        System.out.println(ChatColor.GOLD + this.getFullPrefix() + log);
    }

    public void error(String log){
        System.out.println(ChatColor.RED + this.getFullPrefix() + log);
    }

}
