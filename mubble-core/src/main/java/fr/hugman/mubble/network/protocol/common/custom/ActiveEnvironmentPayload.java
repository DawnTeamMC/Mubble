package fr.hugman.mubble.network.protocol.common.custom;

import java.util.Optional;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributeMap;

/**
 * Tells the client which environment is active for it right now.
 *
 * <p>Sent on trial entry, and again with an empty profile on voyage exit. The client <em>applies</em>
 * this; it never recomputes it, which is why the per-instance overrides arrive already resolved and
 * why the seed is never sent.
 *
 * @param profile   the profile to apply, or empty to clear the override and go back to vanilla
 * @param overrides per-instance values resolved server-side, layered on top of the profile
 */
public record ActiveEnvironmentPayload(
        Optional<Identifier> profile,
        EnvironmentAttributeMap overrides
) implements CustomPacketPayload {
    /**
     * The profile travels as a bare id rather than a registry reference on purpose.
     *
     * <p>A registry reference that the client cannot resolve fails inside the packet decoder and
     * disconnects the player with something unreadable. An id lets the client say precisely which
     * profile it was never sent, which is the only useful thing to know when this goes wrong.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, ActiveEnvironmentPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional), ActiveEnvironmentPayload::profile,
            ByteBufCodecs.fromCodecWithRegistries(EnvironmentAttributeMap.NETWORK_CODEC), ActiveEnvironmentPayload::overrides,
            ActiveEnvironmentPayload::new
    );

    public static ActiveEnvironmentPayload clear() {
        return new ActiveEnvironmentPayload(Optional.empty(), EnvironmentAttributeMap.EMPTY);
    }

    @Override
    public Type<? extends ActiveEnvironmentPayload> type() {
        return MubblePayloadTypes.ACTIVE_ENVIRONMENT;
    }
}
