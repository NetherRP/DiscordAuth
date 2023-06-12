package fr.xen0xys.discordauth.papermc.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.GsonUtils;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.Packet;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public abstract class ServerPacket extends Packet {

    public static void sendServer(Player player, SubChannels channel, Packet packet){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel.getName());
        Encryption encryption = new Encryption(DiscordAuthPlugin.getInstance().getLogger());
        String encryptedPacket = encryption.encryptSymmetric(DiscordAuthPlugin.getCommonConfig().getSecret(), packet.serialize());
        if(Objects.isNull(encryptedPacket)){
            DiscordAuthPlugin.getInstance().getLogger().severe("Failed to encrypt packet !");
            return;
        }
        output.writeUTF(encryptedPacket);
        player.sendPluginMessage(DiscordAuthPlugin.getInstance(), PluginInfos.CHANNEL, output.toByteArray());
    }

    @Nullable
    public static <T extends Packet> T decryptServer(Class<T> packetClass, String encryptedPacket){
        Logger logger = DiscordAuthPlugin.getInstance().getLogger();
        Encryption encryption = new Encryption(logger);
        String decryptedPacket = encryption.decryptSymmetric(DiscordAuthPlugin.getCommonConfig().getSecret(), encryptedPacket);
        if(Objects.isNull(decryptedPacket)){
            logger.severe("Failed to decrypt packet !");
            return null;
        }
        return GsonUtils.getGson().fromJson(decryptedPacket, packetClass);
    }
}
