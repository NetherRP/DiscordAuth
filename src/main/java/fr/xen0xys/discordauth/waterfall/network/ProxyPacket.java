package fr.xen0xys.discordauth.waterfall.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.GsonUtils;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.Packet;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.exceptions.PacketEncryptionException;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public abstract class ProxyPacket extends Packet {

    public static void sendProxy(@NotNull final ProxiedPlayer player, @NotNull final SubChannels channel, @NotNull final Packet packet) throws PacketEncryptionException{
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel.getName());
        String encryptedPacket = new Encryption().encryptSymmetric(DiscordAuthProxy.getCommonConfig().getSecret(), packet.serialize());
        if(Objects.isNull(encryptedPacket))
            throw new PacketEncryptionException("Failed to encrypt packet !");
        output.writeUTF(encryptedPacket);
        player.getServer().getInfo().sendData(PluginInfos.CHANNEL, output.toByteArray());
    }

    @Nullable
    public static <T extends Packet> T decryptProxy(@NotNull final Class<T> packetClass, @NotNull final String encryptedPacket) throws PacketEncryptionException{
        String decryptedPacket = new Encryption().decryptSymmetric(DiscordAuthProxy.getCommonConfig().getSecret(), encryptedPacket);
        if(Objects.isNull(decryptedPacket))
            throw new PacketEncryptionException("Failed to decrypt packet !");
        return GsonUtils.getGson().fromJson(decryptedPacket, packetClass);
    }
}
