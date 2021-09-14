package fr.xen0xys.discordauth.discord.embeds;

import fr.xen0xys.discordauth.DiscordAuth;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.UUID;

public class IpListEmbed extends EmbedBuilder implements CustomEmbed {

    public IpListEmbed(UUID uuid, String minecraftName, boolean allowed){
        this.setColor(Color.CYAN);
        if(allowed){
            this.setTitle(DiscordAuth.getLanguage().ipAllowed);
        }else{
            this.setTitle(DiscordAuth.getLanguage().ipBlocked);
        }

        String[] ips = DiscordAuth.getAccountTable().getIps(uuid, minecraftName, allowed);

        if(ips[0].equals("")){
            if(allowed){
                this.addField(DiscordAuth.getLanguage().noIpAllowed, "", false);
            }else{
                this.addField(DiscordAuth.getLanguage().noIpBlocked, "", false);
            }
        }else if(ips[0].equals("*")){
            if(allowed){
                this.addField(DiscordAuth.getLanguage().allIpAllowed, "", false);
            }else{
                this.addField(DiscordAuth.getLanguage().allIpBlocked, "", false);
            }
        }else{
            for(String ip: ips){
                this.addField(ip, "", false);
            }
        }
    }

}
