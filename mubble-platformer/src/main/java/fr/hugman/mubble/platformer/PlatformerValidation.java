package fr.hugman.mubble.platformer;

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
 * @param freeImpulse           a change of velocity small enough to come from the world, in blocks per
 *                              tick: entities shoving each other, water currents, bubble columns
 * @param impulseMargin         how much stronger than the push the server sent a reported one may be
 * @param freeImpulseBudget     how much the small changes of velocity no block explains may add up to
 *                              in a row, in blocks per tick: without a bound, a client claiming one
 *                              every tick would rise faster than gravity pulls it down
 * @param freeImpulseRecovery   how much of that budget comes back every tick: kept under gravity, and
 *                              over what a water current pushes
 */
public record PlatformerValidation(double positionTolerance, double displacementTolerance, int impulseWindowTicks, double freeImpulse, double impulseMargin,
                                   double freeImpulseBudget, double freeImpulseRecovery) {
    public static final PlatformerValidation DEFAULT = new PlatformerValidation(0.1D, 0.05D, 40, 0.15D, 0.3D, 0.45D, 0.02D);
    public static final Codec<PlatformerValidation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.doubleRange(0.0D, 16.0D).fieldOf("position_tolerance").forGetter(PlatformerValidation::positionTolerance),
            Codec.doubleRange(0.0D, 16.0D).fieldOf("displacement_tolerance").forGetter(PlatformerValidation::displacementTolerance),
            Codec.intRange(0, 1200).fieldOf("impulse_window_ticks").forGetter(PlatformerValidation::impulseWindowTicks),
            Codec.doubleRange(0.0D, 16.0D).fieldOf("free_impulse").forGetter(PlatformerValidation::freeImpulse),
            Codec.doubleRange(0.0D, 16.0D).fieldOf("impulse_margin").forGetter(PlatformerValidation::impulseMargin),
            Codec.doubleRange(0.0D, 16.0D).optionalFieldOf("free_impulse_budget", DEFAULT.freeImpulseBudget()).forGetter(PlatformerValidation::freeImpulseBudget),
            Codec.doubleRange(0.0D, 16.0D).optionalFieldOf("free_impulse_recovery", DEFAULT.freeImpulseRecovery()).forGetter(PlatformerValidation::freeImpulseRecovery)
    ).apply(instance, PlatformerValidation::new));
}
