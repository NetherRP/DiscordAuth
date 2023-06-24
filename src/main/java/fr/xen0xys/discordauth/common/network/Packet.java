package fr.xen0xys.discordauth.common.network;

import fr.xen0xys.discordauth.common.GsonUtils;

public abstract class Packet {
    public String serialize(){
        return GsonUtils.getGson().toJson(this);
    }
}
