package fr.hugman.mubble.platformer.network;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import fr.hugman.mubble.platformer.PlatformerProfile;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;

/**
 * The current values of every platformer profile, sent on join and after each {@code /reload}, see
 * {@link fr.hugman.mubble.platformer.PlatformerProfiles}.
 */
public record PlatformerProfilesPayload(Map<ResourceKey<PlatformerProfile>, PlatformerProfile> profiles) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, PlatformerProfilesPayload> STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, ResourceKey<PlatformerProfile>, PlatformerProfile, Map<ResourceKey<PlatformerProfile>, PlatformerProfile>>map(
            HashMap::new,
            ResourceKey.streamCodec(PlatformerRegistries.PLATFORMER_PROFILE),
            PlatformerProfile.DIRECT_STREAM_CODEC
    ).map(PlatformerProfilesPayload::new, PlatformerProfilesPayload::profiles);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PlatformerPayloadTypes.PLATFORMER_PROFILES;
    }
}
