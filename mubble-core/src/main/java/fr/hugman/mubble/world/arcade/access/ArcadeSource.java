package fr.hugman.mubble.world.arcade.access;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import java.util.Map;
import java.util.Optional;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Something that has a say in the arcade movement of a player: an item worn, a trial ruleset, a
 * Vessel, a command...
 *
 * @param id       what the source is, so that it can be replaced or removed later
 * @param profile  the profile the source asks for; the one of the source with the highest priority
 *                 declaring one is the active profile
 * @param moves    what the source says about each move or tag of moves
 * @param priority how the source ranks against the others when choosing the profile
 * @param expiry   the game time at which the source stops counting, if it ever does
 */
public record ArcadeSource(
        Identifier id,
        Optional<ResourceKey<ArcadeProfile>> profile,
        Map<MoveSelector, AccessMode> moves,
        int priority,
        Optional<Long> expiry
) {
    public static final Codec<ArcadeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("source_id").forGetter(ArcadeSource::id),
            ResourceKey.codec(MubbleRegistries.ARCADE_PROFILE).optionalFieldOf("profile").forGetter(ArcadeSource::profile),
            Codec.unboundedMap(MoveSelector.CODEC, AccessMode.CODEC).optionalFieldOf("moves", Map.of()).forGetter(ArcadeSource::moves),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(ArcadeSource::priority),
            Codec.LONG.optionalFieldOf("expiry").forGetter(ArcadeSource::expiry)
    ).apply(instance, ArcadeSource::new));

    public static final StreamCodec<ByteBuf, ArcadeSource> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ArcadeSource::id,
            ResourceKey.streamCodec(MubbleRegistries.ARCADE_PROFILE).apply(ByteBufCodecs::optional), ArcadeSource::profile,
            ByteBufCodecs.map(java.util.HashMap::new, MoveSelector.STREAM_CODEC, AccessMode.STREAM_CODEC), ArcadeSource::moves,
            ByteBufCodecs.VAR_INT, ArcadeSource::priority,
            ByteBufCodecs.VAR_LONG.apply(ByteBufCodecs::optional), ArcadeSource::expiry,
            ArcadeSource::new
    );

    public boolean isExpired(long gameTime) {
        return this.expiry.isPresent() && gameTime >= this.expiry.get();
    }
}
