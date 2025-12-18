package fr.hugman.mubble.network.payload.c2s;

import fr.hugman.mubble.network.payload.MubblePayloads;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PowerUpTriggerPayload implements CustomPacketPayload {
    public static final PowerUpTriggerPayload INSTANCE = new PowerUpTriggerPayload();
    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpTriggerPayload> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends PowerUpTriggerPayload> type() {
        return MubblePayloads.POWER_UP_TRIGGER;
    }
}
