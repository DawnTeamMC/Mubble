package fr.hugman.mubble.network.protocol.common.custom;

import fr.hugman.mubble.power_up.PowerUp;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PowerUpChangePayload(
        Optional<Holder<PowerUp>> previous,
        Optional<Holder<PowerUp>> next
) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, PowerUpChangePayload> PACKET_CODEC = StreamCodec.composite(
            PowerUp.OPTIONAL_ENTRY_PACKET_CODEC, (powerUpChangePayload -> powerUpChangePayload.previous),
            PowerUp.OPTIONAL_ENTRY_PACKET_CODEC, (powerUpChangePayload -> powerUpChangePayload.next),
            PowerUpChangePayload::new
    );

    @Override
    public Type<? extends PowerUpChangePayload> type() {
        return MubblePayloadTypes.POWER_UP_CHANGE;
    }
}
