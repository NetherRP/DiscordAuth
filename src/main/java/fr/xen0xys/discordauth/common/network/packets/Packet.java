package fr.xen0xys.discordauth.common.network.packets;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.GsonUtils;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.network.SubChannels;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("UnstableApiUsage")
public class Packet {
    private String serialize(){
        return GsonUtils.getGson().toJson(this);
    }

    public void sendProxy(ProxiedPlayer player, SubChannels channel){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel.getName());
        output.writeUTF(serialize());
        player.getServer().getInfo().sendData(PluginInfos.CHANNEL, output.toByteArray());
    }

    public void sendServer(Plugin plugin, Player player, SubChannels channel){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel.getName());
        output.writeUTF(serialize());
        player.sendPluginMessage(plugin, PluginInfos.CHANNEL, output.toByteArray());
    }
}
