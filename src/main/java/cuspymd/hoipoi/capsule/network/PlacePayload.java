package cuspymd.hoipoi.capsule.network;

import cuspymd.hoipoi.capsule.HoipoiCapsuleMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record PlacePayload() implements CustomPayload {
    public static final Id<PlacePayload> ID = new Id<>(HoipoiCapsuleMod.PLACE_PACKET_ID);
    public static final PacketCodec<PacketByteBuf, PlacePayload> CODEC = PacketCodec.unit(new PlacePayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}