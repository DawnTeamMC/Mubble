package fr.hugman.mubble.network.protocol.common.custom;

import fr.hugman.mubble.world.voyage.environment.EnvironmentProfile;
import java.util.Map;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Re-sends the environment profile registry to already-connected clients.
 *
 * <p>Datapack registries are synced once, during the configuration phase. {@code /reload} re-reads
 * them server-side but there is no vanilla or loader path to push the new contents to a client that
 * is already playing, so an edited profile would keep rendering with its old values until the player
 * reconnected. This closes that gap.
 *
 * <p>Only sent on reload. First-time sync is still the loader's job — see
 * {@link fr.hugman.mubble.core.registries.MubbleBuiltInRegistries}.
 *
 * <p>This is the first datapack registry whose client-visible slice has to survive a reload, and it
 * will not be the last: constellations, worlds, quests, trials and arenas all follow. When the second
 * one arrives, generalise this rather than copying it.
 */
public record EnvironmentProfileSyncPayload(Map<Identifier, EnvironmentProfile> profiles) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, EnvironmentProfileSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMapSupplier.INSTANCE,
                    Identifier.STREAM_CODEC,
                    ByteBufCodecs.fromCodecWithRegistries(EnvironmentProfile.NETWORK_CODEC)
            ),
            EnvironmentProfileSyncPayload::profiles,
            EnvironmentProfileSyncPayload::new
    );

    @Override
    public Type<? extends EnvironmentProfileSyncPayload> type() {
        return MubblePayloadTypes.ENVIRONMENT_PROFILE_SYNC;
    }

    /** {@link ByteBufCodecs#map} wants a size-aware factory; this keeps the codec above readable. */
    private enum HashMapSupplier implements java.util.function.IntFunction<Map<Identifier, EnvironmentProfile>> {
        INSTANCE;

        @Override
        public Map<Identifier, EnvironmentProfile> apply(int size) {
            return new java.util.HashMap<>(size);
        }
    }
}
