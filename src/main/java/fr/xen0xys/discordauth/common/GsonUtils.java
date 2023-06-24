package fr.xen0xys.discordauth.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
    public static Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
