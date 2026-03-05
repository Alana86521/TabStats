package com.tabstats.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TabPressPayload() implements CustomPayload {
    public static final CustomPayload.Id<TabPressPayload> ID = new CustomPayload.Id<>(Identifier.of("tabstats", "tab_press"));
    public static final PacketCodec<RegistryByteBuf, TabPressPayload> CODEC = PacketCodec.unit(new TabPressPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
