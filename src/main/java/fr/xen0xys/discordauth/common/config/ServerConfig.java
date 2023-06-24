package fr.xen0xys.discordauth.common.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ServerConfig extends ConfigurationReader{

    public ServerConfig(@NotNull File dataFolder) {
        super(dataFolder, "configs/server.yml");
    }

    public boolean isFirstTimeTp() {
        return this.getValue(Boolean.class, "spawnpoint.first_time_tp", false);
    }
    public boolean isTpOnLogin() {
        return this.getValue(Boolean.class, "spawnpoint.tp_on_login", false);
    }

    public boolean isRespawnEnable() {
        return this.getValue(Boolean.class, "spawnpoint.respawn.enable", false);
    }
    public boolean isRespawnEvenWithBed() {
        return this.getValue(Boolean.class, "spawnpoint.respawn.even_with_bed", false);
    }
    @Nullable
    public Location getSpawnPoint(){
        String worldName = this.getValue(String.class, "spawnpoint.infos.world_name", "world");
        if(worldName != null){
            double x = this.getValue(Double.class, "spawnpoint.infos.x", 0.0);
            double y = this.getValue(Double.class, "spawnpoint.infos.y", 0.0);
            double z = this.getValue(Double.class, "spawnpoint.infos.z", 0.0);
            float pitch = this.getValue(Float.class, "spawnpoint.infos.facing.pitch", 0.0f);
            float yaw = this.getValue(Float.class, "spawnpoint.infos.facing.yaw", 0.0f);
            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }
        return null;
    }



}
