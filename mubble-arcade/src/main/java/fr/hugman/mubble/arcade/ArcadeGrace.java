package fr.hugman.mubble.arcade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * The grace mechanics of a profile: everything that makes the controller work for the player.
 *
 * @param coyoteTicks           ticks a jump is still accepted after walking off a ledge; the base
 *                              value of {@code mubble:arcade_coyote_ticks}
 * @param jumpBufferMs          how long a jump pressed in the air is remembered for the landing, in
 *                              milliseconds
 * @param actionBufferMs        how long an action press is remembered, in milliseconds
 * @param chainWindowTicks      ticks after a landing during which the next jump chains into a
 *                              double or triple jump
 * @param ledgeMagnetism        how far a grabbable edge pulls the player in, in blocks
 * @param ledgeFacingDot        how squarely the player has to face a ledge to grab it, as the cosine
 *                              of the largest angle allowed
 * @param ledgeRegrabTicks      ticks after letting go of a ledge before it can be grabbed again
 * @param cornerCorrection      how far the player is nudged around a corner on a head bonk or a
 *                              ledge clip, in blocks
 * @param wallJumpLeniencyTicks ticks a wall jump is still accepted after leaving the wall
 * @param landingLagTicks       ticks of the landing state, which every move can cancel anyway
 */
public record ArcadeGrace(
        int coyoteTicks,
        int jumpBufferMs,
        int actionBufferMs,
        int chainWindowTicks,
        double ledgeMagnetism,
        double ledgeFacingDot,
        int ledgeRegrabTicks,
        double cornerCorrection,
        int wallJumpLeniencyTicks,
        int landingLagTicks
) {
    public static final Codec<ArcadeGrace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(0, 40).fieldOf("coyote_ticks").forGetter(ArcadeGrace::coyoteTicks),
            Codec.intRange(0, 2000).fieldOf("jump_buffer_ms").forGetter(ArcadeGrace::jumpBufferMs),
            Codec.intRange(0, 2000).fieldOf("action_buffer_ms").forGetter(ArcadeGrace::actionBufferMs),
            Codec.intRange(0, 40).fieldOf("chain_window_ticks").forGetter(ArcadeGrace::chainWindowTicks),
            Codec.doubleRange(0.0D, 2.0D).fieldOf("ledge_magnetism").forGetter(ArcadeGrace::ledgeMagnetism),
            Codec.doubleRange(-1.0D, 1.0D).fieldOf("ledge_facing_dot").forGetter(ArcadeGrace::ledgeFacingDot),
            Codec.intRange(0, 200).fieldOf("ledge_regrab_ticks").forGetter(ArcadeGrace::ledgeRegrabTicks),
            Codec.doubleRange(0.0D, 1.0D).fieldOf("corner_correction").forGetter(ArcadeGrace::cornerCorrection),
            Codec.intRange(0, 40).fieldOf("wall_jump_leniency_ticks").forGetter(ArcadeGrace::wallJumpLeniencyTicks),
            Codec.intRange(0, 40).fieldOf("landing_lag_ticks").forGetter(ArcadeGrace::landingLagTicks)
    ).apply(instance, ArcadeGrace::new));
}
