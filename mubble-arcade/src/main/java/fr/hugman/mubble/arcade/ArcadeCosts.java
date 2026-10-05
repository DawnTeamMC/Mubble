package fr.hugman.mubble.arcade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Whether a profile charges food for its moves.
 *
 * @param exhaustion            whether moves add their declared exhaustion
 * @param hungerGatesSpeedMoves whether the moves tagged {@code mubble:speed} need enough food, the way
 *                              a vanilla sprint does
 */
public record ArcadeCosts(boolean exhaustion, boolean hungerGatesSpeedMoves) {
    public static final Codec<ArcadeCosts> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("exhaustion").forGetter(ArcadeCosts::exhaustion),
            Codec.BOOL.fieldOf("hunger_gates_speed_moves").forGetter(ArcadeCosts::hungerGatesSpeedMoves)
    ).apply(instance, ArcadeCosts::new));
}
