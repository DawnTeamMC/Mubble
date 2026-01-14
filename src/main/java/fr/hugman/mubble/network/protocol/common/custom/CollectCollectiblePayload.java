package fr.hugman.mubble.network.protocol.common.custom;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CollectCollectiblePayload(
        int itemId,
        int amount
) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, CollectCollectiblePayload> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CollectCollectiblePayload::itemId,
            ByteBufCodecs.VAR_INT, CollectCollectiblePayload::amount,
            CollectCollectiblePayload::new
    );

    @Override
    public Type<? extends CollectCollectiblePayload> type() {
        return MubblePayloadTypes.COLLECT_COLLECTIBLE;
    }
}
