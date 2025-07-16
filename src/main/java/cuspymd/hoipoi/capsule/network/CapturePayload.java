package cuspymd.hoipoi.capsule.network;

import cuspymd.hoipoi.capsule.HoipoiCapsuleMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record CapturePayload() implements CustomPayload {
    public static final Id<CapturePayload> ID = new Id<>(HoipoiCapsuleMod.CAPTURE_PACKET_ID);
    public static final PacketCodec<PacketByteBuf, CapturePayload> CODEC = PacketCodec.unit(new CapturePayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}