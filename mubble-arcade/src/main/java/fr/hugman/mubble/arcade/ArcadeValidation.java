package fr.hugman.mubble.arcade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * How lenient the server is when it checks the movement a client claims under a profile. These are
 * margins for rounding and latency, not gameplay: the displacement itself always comes from the
 * shared simulation.
 *
 * @param positionTolerance     how far the start of a step may be from where the server last saw the
 *                              player, in blocks
 * @param displacementTolerance how much further than planned a step may go, in blocks
 * @param impulseWindowTicks    how long after the server pushed a player (knockback, explosion) a
 *                              client may report that push
 * @param freeImpulse           a change of velocity small enough to be accepted at any time, in blocks
 *                              per tick: bubble columns, entities shoving each other
 * @param impulseMargin         how much stronger than the push the server sent a reported one may be
 */
public record ArcadeValidation(double positionTolerance, double displacementTolerance, int impulseWindowTicks, double freeImpulse, double impulseMargin) {
    public static final ArcadeValidation DEFAULT = new ArcadeValidation(0.1D, 0.05D, 40, 0.15D, 0.3D);
    public static final Codec<ArcadeValidation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.doubleRange(0.0D, 16.0D).fieldOf("position_tolerance").forGetter(ArcadeValidation::positionTolerance),
            Codec.doubleRange(0.0D, 16.0D).fieldOf("displacement_tolerance").forGetter(ArcadeValidation::displacementTolerance),
            Codec.intRange(0, 1200).fieldOf("impulse_window_ticks").forGetter(ArcadeValidation::impulseWindowTicks),
            Codec.doubleRange(0.0D, 16.0D).fieldOf("free_impulse").forGetter(ArcadeValidation::freeImpulse),
            Codec.doubleRange(0.0D, 16.0D).fieldOf("impulse_margin").forGetter(ArcadeValidation::impulseMargin)
    ).apply(instance, ArcadeValidation::new));
}
