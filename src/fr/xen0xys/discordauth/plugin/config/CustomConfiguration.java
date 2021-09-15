package fr.xen0xys.discordauth.plugin.config;

import fr.xen0xys.xen0lib.utils.ConfigurationReader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class CustomConfiguration extends ConfigurationReader {

    public CustomConfiguration(Plugin plugin, String configName) {
        super(plugin, configName);
    }


    // MYSQL
    public boolean isMySQLEnabled(){
        return this.getConfiguration().getBoolean("mysql.enable");
    }
    public HashMap<String, Object> getDatabaseInfos(){
        HashMap<String, Object> infos = new HashMap<>();
        FileConfiguration config = this.getConfiguration();
        infos.put("host", config.getString("mysql.host"));
        infos.put("port", config.getInt("mysql.port"));
        infos.put("user", config.getString("mysql.user"));
        infos.put("password", config.getString("mysql.password"));
        infos.put("database", config.getString("mysql.database"));
        return infos;
    }


    // DISCORD
    public String getBotToken() {
        return this.getConfiguration().getString("discord.token");
    }
    public boolean isOnlySafety() {
        return this.getConfiguration().getBoolean("discord.only_safety");
    }
    public long getGuildId() {
        return this.getConfiguration().getLong("discord.guild_id");
    }
    public long getMessageId(){
        return this.getConfiguration().getLong("discord.message_id");
    }
    public long getChannelId() {
        return this.getConfiguration().getLong("discord.channel_id");
    }
    public String getReactionName() {
        return this.getConfiguration().getString("discord.reaction_name");
    }
    public boolean getEnableConnectionMessage() {
        return this.getConfiguration().getBoolean("discord.enable_connection_message");
    }
    public boolean getEnableSharedChat() {
        return this.getConfiguration().getBoolean("discord.enable_shared_chat");
    }
    public boolean getSendMessageToUnregisterUsers() {
        return this.getConfiguration().getBoolean("discord.send_message_to_unregister_users");
    }
    public boolean getSendAdvancements() {
        return this.getConfiguration().getBoolean("discord.send_advancements");
    }
    public boolean getDeathMessages(){
        return this.getConfiguration().getBoolean("discord.send_death_messages");
    }
    // Logo
    public boolean isLogoEnabled() {
        return this.getConfiguration().getBoolean("discord.logo.enable");
    }
    public String getLogo() {
        return this.getConfiguration().getString("discord.logo.url");
    }
    // Activity
    public boolean isBotActivityEnabled(){
        return this.getConfiguration().getBoolean("discord.activity.enable");
    }
    public String getBotActivityType(){
        return this.getConfiguration().getString("discord.activity.type");
    }
    public String getBotActivityText(){
        return this.getConfiguration().getString("discord.activity.text");
    }
    public String getBotActivityUrl(){
        return this.getConfiguration().getString("discord.activity.url");
    }


    // ENCRYPTION
    public boolean getEncryptIp() {
        return this.getConfiguration().getBoolean("encryption.encrypt_ip");
    }
    public boolean getIsAdditionalString() {
        return this.getConfiguration().getBoolean("encryption.additional_encryption_string.enable");
    }
    public String getAdditionalString() {
        return this.getConfiguration().getString("encryption.additional_encryption_string.string");
    }


    // SPAWN POINT
    public boolean isFirstTimeTp(){
        return this.getConfiguration().getBoolean("spawnpoint.first_time_tp");
    }
    public boolean isTpOnLogin(){
        return this.getConfiguration().getBoolean("spawnpoint.tp_on_login");
    }
    // Respawn
    public boolean isRespawnEnable(){
        return this.getConfiguration().getBoolean("spawnpoint.respawn.enable");
    }
    public boolean isRespawnEvenWithBed(){
        return this.getConfiguration().getBoolean("spawnpoint.respawn.even_with_bed");
    }
    // Infos
    public Location getSpawnPoint(){
        String worldName = this.getConfiguration().getString("spawnpoint.infos.world_name");
        if(worldName != null){
            double x = this.getConfiguration().getDouble("spawnpoint.infos.x");
            double y = this.getConfiguration().getDouble("spawnpoint.infos.y");
            double z = this.getConfiguration().getDouble("spawnpoint.infos.z");
            float pitch = (float) this.getConfiguration().getDouble("spawnpoint.infos.facing.pitch");
            float yaw = (float) this.getConfiguration().getDouble("spawnpoint.infos.facing.yaw");
            return new Location(Bukkit.getWorld(worldName), x, y, z, pitch, yaw);
        }
        return null;
    }


    // OTHER
    public Object getLanguage() {
        return this.getConfiguration().getString("other.language");
    }
    public boolean getPremium(){
        return this.getConfiguration().getBoolean("other.premium");
    }
    public int getSessionDuration(){
        return this.getConfiguration().getInt("other.session_duration");
    }
    public boolean getAggressiveLogin(){
        return this.getConfiguration().getBoolean("other.aggressive_login");
    }











}
