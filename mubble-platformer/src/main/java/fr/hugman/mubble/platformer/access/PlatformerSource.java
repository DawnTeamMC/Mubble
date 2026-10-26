package fr.hugman.mubble.platformer.access;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.platformer.PlatformerProfile;
import java.util.Map;
import java.util.Optional;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Something that has a say in the platformer movement of a player: an item worn, a trial ruleset, a
 * Vessel, a command...
 *
 * @param id       what the source is, so that it can be replaced or removed later
 * @param profile  the profile the source asks for; the one of the source with the highest priority
 *                 declaring one is the active profile
 * @param moves    what the source says about each move or tag of moves
 * @param priority how the source ranks against the others when choosing the profile
 * @param expiry   the game time at which the source stops counting, if it ever does
 */
public record PlatformerSource(
        Identifier id,
        Optional<ResourceKey<PlatformerProfile>> profile,
        Map<MoveSelector, AccessMode> moves,
        int priority,
        Optional<Long> expiry
) {
    public static final Codec<PlatformerSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("source_id").forGetter(PlatformerSource::id),
            ResourceKey.codec(PlatformerRegistries.PLATFORMER_PROFILE).optionalFieldOf("profile").forGetter(PlatformerSource::profile),
            Codec.unboundedMap(MoveSelector.CODEC, AccessMode.CODEC).optionalFieldOf("moves", Map.of()).forGetter(PlatformerSource::moves),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(PlatformerSource::priority),
            Codec.LONG.optionalFieldOf("expiry").forGetter(PlatformerSource::expiry)
    ).apply(instance, PlatformerSource::new));

    public static final StreamCodec<ByteBuf, PlatformerSource> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, PlatformerSource::id,
            ResourceKey.streamCodec(PlatformerRegistries.PLATFORMER_PROFILE).apply(ByteBufCodecs::optional), PlatformerSource::profile,
            ByteBufCodecs.map(java.util.HashMap::new, MoveSelector.STREAM_CODEC, AccessMode.STREAM_CODEC), PlatformerSource::moves,
            ByteBufCodecs.VAR_INT, PlatformerSource::priority,
            ByteBufCodecs.VAR_LONG.apply(ByteBufCodecs::optional), PlatformerSource::expiry,
            PlatformerSource::new
    );

    public boolean isExpired(long gameTime) {
        return this.expiry.isPresent() && gameTime >= this.expiry.get();
    }
}
