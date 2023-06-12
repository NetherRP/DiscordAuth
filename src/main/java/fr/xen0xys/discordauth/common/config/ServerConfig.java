package fr.xen0xys.discordauth.common.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ServerConfig extends ConfigurationReader{

    public ServerConfig(@NotNull File dataFolder) {
        super(dataFolder, "configs/server.yml");
    }

    public boolean isRespawnEnable() {
        return this.getConfiguration().getBoolean("spawnpoint.respawn.enable");
    }

    public boolean isRespawnEvenWithBed() {
        return this.getConfiguration().getBoolean("spawnpoint.respawn.even_with_bed");
    }

    public Location getSpawnPoint(){
        String worldName = this.getConfiguration().getString("spawnpoint.infos.world_name");
        if(worldName != null){
            double x = this.getConfiguration().getDouble("spawnpoint.infos.x");
            double y = this.getConfiguration().getDouble("spawnpoint.infos.y");
            double z = this.getConfiguration().getDouble("spawnpoint.infos.z");
            float pitch = (float) this.getConfiguration().getDouble("spawnpoint.infos.facing.pitch");
            float yaw = (float) this.getConfiguration().getDouble("spawnpoint.infos.facing.yaw");
            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }
        return null;
    }
}
