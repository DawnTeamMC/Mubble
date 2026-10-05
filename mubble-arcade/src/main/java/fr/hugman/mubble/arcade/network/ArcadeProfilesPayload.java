package fr.hugman.mubble.arcade.network;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import fr.hugman.mubble.arcade.ArcadeProfile;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;

/**
 * The current values of every arcade profile, sent on join and after each {@code /reload}, see
 * {@link fr.hugman.mubble.arcade.ArcadeProfiles}.
 */
public record ArcadeProfilesPayload(Map<ResourceKey<ArcadeProfile>, ArcadeProfile> profiles) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ArcadeProfilesPayload> STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, ResourceKey<ArcadeProfile>, ArcadeProfile, Map<ResourceKey<ArcadeProfile>, ArcadeProfile>>map(
            HashMap::new,
            ResourceKey.streamCodec(ArcadeRegistries.ARCADE_PROFILE),
            ArcadeProfile.DIRECT_STREAM_CODEC
    ).map(ArcadeProfilesPayload::new, ArcadeProfilesPayload::profiles);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ArcadePayloadTypes.ARCADE_PROFILES;
    }
}
