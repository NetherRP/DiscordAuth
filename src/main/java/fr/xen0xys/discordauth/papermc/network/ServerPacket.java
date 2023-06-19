package fr.xen0xys.discordauth.papermc.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xen0xys.discordauth.common.GsonUtils;
import fr.xen0xys.discordauth.common.PluginInfos;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.common.network.Packet;
import fr.xen0xys.discordauth.common.network.SubChannels;
import fr.xen0xys.discordauth.common.network.exceptions.PacketEncryptionException;
import fr.xen0xys.discordauth.papermc.DiscordAuthPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public abstract class ServerPacket extends Packet {

    public static void sendServer(@NotNull final Player player, @NotNull final SubChannels channel, @NotNull final Packet packet){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel.getName());
        String encryptedPacket = new Encryption().encryptSymmetric(DiscordAuthPlugin.getCommonConfig().getSecret(), packet.serialize());
        if(Objects.isNull(encryptedPacket))
            throw new PacketEncryptionException("Failed to encrypt packet !");
        output.writeUTF(encryptedPacket);
        player.sendPluginMessage(DiscordAuthPlugin.getInstance(), PluginInfos.CHANNEL, output.toByteArray());
    }

    public static <T extends Packet> T decryptServer(@NotNull final Class<T> packetClass, @NotNull final String encryptedPacket) throws PacketEncryptionException {
        String decryptedPacket = new Encryption().decryptSymmetric(DiscordAuthPlugin.getCommonConfig().getSecret(), encryptedPacket);
        if(Objects.isNull(decryptedPacket))
            throw new PacketEncryptionException("Failed to decrypt packet !");
        return GsonUtils.getGson().fromJson(decryptedPacket, packetClass);
    }
}
