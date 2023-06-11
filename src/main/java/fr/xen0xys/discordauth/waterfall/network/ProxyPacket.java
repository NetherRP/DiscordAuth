package fr.xen0xys.discordauth.waterfall.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.GsonUtils;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.Packet;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public abstract class ProxyPacket extends Packet {

    public static void sendProxy(ProxiedPlayer player, SubChannels channel, Packet packet){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel.getName());
        Encryption encryption = new Encryption(DiscordAuthProxy.getInstance().getLogger());
        String encryptedPacket = encryption.encryptSymmetric(DiscordAuthProxy.getConfiguration().getSecret(), packet.serialize());
        if(Objects.isNull(encryptedPacket)){
            DiscordAuthProxy.getInstance().getLogger().severe("Failed to encrypt packet !");
            return;
        }
        output.writeUTF(encryptedPacket);
        player.getServer().getInfo().sendData(PluginInfos.CHANNEL, output.toByteArray());
    }

    @Nullable
    public static <T extends Packet> T decryptProxy(Class<T> packetClass, String encryptedPacket){
        Logger logger = DiscordAuthProxy.getInstance().getLogger();
        Encryption encryption = new Encryption(logger);
        String decryptedPacket = encryption.decryptSymmetric(DiscordAuthProxy.getConfiguration().getSecret(), encryptedPacket);
        if(Objects.isNull(decryptedPacket)){
            logger.severe("Failed to decrypt packet !");
            return null;
        }
        return GsonUtils.getGson().fromJson(decryptedPacket, packetClass);
    }
}
