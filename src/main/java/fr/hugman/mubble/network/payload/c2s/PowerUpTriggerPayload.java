package fr.hugman.mubble.network.payload.c2s;

import fr.hugman.mubble.network.payload.MubblePayloads;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class PowerUpTriggerPayload implements CustomPayload {
    public static final PowerUpTriggerPayload INSTANCE = new PowerUpTriggerPayload();
    public static final PacketCodec<RegistryByteBuf, PowerUpTriggerPayload> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends PowerUpTriggerPayload> getId() {
        return MubblePayloads.POWER_UP_TRIGGER;
    }
}
