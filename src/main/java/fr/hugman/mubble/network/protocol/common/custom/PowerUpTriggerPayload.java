package fr.hugman.mubble.network.protocol.common.custom;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PowerUpTriggerPayload implements CustomPacketPayload {
    public static final PowerUpTriggerPayload INSTANCE = new PowerUpTriggerPayload();
    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpTriggerPayload> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends PowerUpTriggerPayload> type() {
        return MubblePayloadTypes.POWER_UP_TRIGGER;
    }
}
