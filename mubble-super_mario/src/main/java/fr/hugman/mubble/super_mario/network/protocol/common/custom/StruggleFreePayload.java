package fr.hugman.mubble.super_mario.network.protocol.common.custom;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Sent every time a frozen player smashes one of their movement keys, to melt a bit of the ice they
 * are stuck in.
 */
public class StruggleFreePayload implements CustomPacketPayload {
    public static final StruggleFreePayload INSTANCE = new StruggleFreePayload();
    public static final StreamCodec<RegistryFriendlyByteBuf, StruggleFreePayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends StruggleFreePayload> type() {
        return SuperMarioPayloadTypes.STRUGGLE_FREE;
    }
}
