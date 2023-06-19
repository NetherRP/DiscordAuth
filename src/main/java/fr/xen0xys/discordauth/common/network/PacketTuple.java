package fr.xen0xys.discordauth.common.network;

public record PacketTuple<A extends Packet, B>(A packet, B player) {
}
