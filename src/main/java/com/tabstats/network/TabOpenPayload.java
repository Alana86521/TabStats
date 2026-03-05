package com.tabstats.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TabOpenPayload() implements CustomPayload {
    public static final CustomPayload.Id<TabOpenPayload> ID = new CustomPayload.Id<>(Identifier.of("tabstats", "tab_open"));
    public static final PacketCodec<RegistryByteBuf, TabOpenPayload> CODEC = PacketCodec.unit(new TabOpenPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
