package fr.hugman.mubble.network.protocol.common.custom;

import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;

/**
 * The current values of every arcade profile, sent on join and after each {@code /reload}, see
 * {@link fr.hugman.mubble.world.arcade.ArcadeProfiles}.
 */
public record ArcadeProfilesPayload(Map<ResourceKey<ArcadeProfile>, ArcadeProfile> profiles) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ArcadeProfilesPayload> STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, ResourceKey<ArcadeProfile>, ArcadeProfile, Map<ResourceKey<ArcadeProfile>, ArcadeProfile>>map(
            HashMap::new,
            ResourceKey.streamCodec(MubbleRegistries.ARCADE_PROFILE),
            ArcadeProfile.DIRECT_STREAM_CODEC
    ).map(ArcadeProfilesPayload::new, ArcadeProfilesPayload::profiles);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return MubblePayloadTypes.ARCADE_PROFILES;
    }
}
