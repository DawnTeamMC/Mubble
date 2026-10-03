package fr.hugman.mubble.world.arcade;

import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

/**
 * What other players need to know about the arcade movement of a player to show it: the move, when
 * it started, and how hard it started. It is synced with the entity, like the pose.
 *
 * @param move      the current move, or {@code null} when the layer is off
 * @param seq       the number of the start of that move, which changes even when the same move starts again
 * @param intensity how hard the move started: the landing speed of a landing, the speed otherwise
 * @param profile   the active profile, whose cues and animations the move plays with
 */
public record ArcadeVisual(@Nullable ArcadeMove move, int seq, float intensity, @Nullable ResourceKey<ArcadeProfile> profile) {
    public static final ArcadeVisual NONE = new ArcadeVisual(null, 0, 0.0F, null);

    public static final StreamCodec<ByteBuf, ArcadeVisual> STREAM_CODEC = StreamCodec.of(
            (buf, visual) -> {
                var out = new FriendlyByteBuf(buf);
                out.writeVarInt(visual.move == null ? 0 : MubbleBuiltInRegistries.ARCADE_MOVE.getId(visual.move) + 1);
                out.writeVarInt(visual.seq);
                out.writeFloat(visual.intensity);
                out.writeBoolean(visual.profile != null);
                if (visual.profile != null) {
                    out.writeIdentifier(visual.profile.identifier());
                }
            },
            buf -> {
                var in = new FriendlyByteBuf(buf);
                int id = in.readVarInt();
                var move = id == 0 ? null : MubbleBuiltInRegistries.ARCADE_MOVE.byId(id - 1);
                int seq = in.readVarInt();
                float intensity = in.readFloat();
                ResourceKey<ArcadeProfile> profile = in.readBoolean() ? ResourceKey.create(MubbleRegistries.ARCADE_PROFILE, in.readIdentifier()) : null;
                return new ArcadeVisual(move, seq, intensity, profile);
            }
    );

    public boolean isActive() {
        return this.move != null;
    }
}
