package fr.xen0xys.discordauth.common.network.exceptions;

public class PacketEncryptionException extends RuntimeException{
    public PacketEncryptionException(String message) {
        super(message);
    }
}
