package fr.xen0xys.discordauth.common.network;

import org.jetbrains.annotations.NotNull;

public record PacketTuple<A extends Packet, B>(@NotNull A packet, B player) {}
