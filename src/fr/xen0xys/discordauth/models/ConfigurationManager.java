package fr.xen0xys.discordauth.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationManager {

    private final FileConfiguration config;

    public ConfigurationManager(JavaPlugin plugin){
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public String getBotToken(){
        return this.config.getString("discord.token");
    }
    public long getGuildId(){
        return this.config.getLong("discord.guild_id");
    }
    public long getMessageId(){
        return this.config.getLong("discord.message_id");
    }
    public long getChannelId(){
        return this.config.getLong("discord.channel_id");
    }
    public boolean getEnableConnectionMessage(){
        return this.config.getBoolean("discord.enable_connection_message");
    }
    public boolean getEnableSharedChat(){
        return this.config.getBoolean("discord.enable_shared_chat");
    }
    public boolean getSendMessageToUnregisterUsers(){
        return this.config.getBoolean("discord.send_message_to_unregister_users");
    }
    public String getReactionName(){
        return this.config.getString("discord.reaction_name");
    }
    public boolean getSendAdvancements(){
        return this.config.getBoolean("discord.send_advancements");
    }

    public boolean useMysql(){
        return this.config.getBoolean("mysql.enable");
    }
    public String getHost(){
        return this.config.getString("mysql.host");
    }
    public int getPort(){
        return (int) this.config.getInt("mysql.port");
    }
    public String getDatabase(){
        return this.config.getString("mysql.database");
    }
    public String getUser(){
        return this.config.getString("mysql.user");
    }
    public String getPassword(){
        return this.config.getString("mysql.password");
    }

    public boolean getEnableIpEncryption(){
        return this.config.getBoolean("encryption.encrypt_ip");
    }
    public boolean getEnableAdditionalEncryptionString(){
        return this.config.getBoolean("encryption.additional_encryption_string.enable");
    }
    public boolean getAdditionalEncryptionString(){
        return this.config.getBoolean("encryption.additional_encryption_string.string");
    }

    public Location getSpawnPoint(){
        String worldName = this.config.getString("spawnpoint.world_name");
        double x, y, z;
        float yaw, pitch;
        x = this.config.getDouble("spawnpoint.x");
        y = this.config.getDouble("spawnpoint.y");
        z = this.config.getDouble("spawnpoint.z");
        yaw = (float) this.config.getDouble("spawnpoint.facing.yaw");
        pitch = (float) this.config.getDouble("spawnpoint.facing.pitch");
        if(worldName != null){
            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }
        return null;
    }


}
