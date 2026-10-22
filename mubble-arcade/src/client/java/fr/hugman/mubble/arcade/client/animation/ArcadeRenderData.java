package fr.hugman.mubble.arcade.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.AnimationState;
import org.jspecify.annotations.Nullable;

/**
 * What the render state of a player carries about its arcade animation: the animation to play and
 * when it started, and while the model eases from one animation into the next, the one it leaves and
 * how far into the next it is. {@link #NONE} when no arcade move is playing, which leaves the model
 * alone.
 *
 * @param animation     the animation to play, or {@code null} for the vanilla pose
 * @param previous      the animation the model eases out of, or {@code null} for the vanilla pose
 * @param weight        how far the model is into {@code animation}, from 0 (all {@code previous}) to 1
 * @param turn          the quarter turn of an animation played side on, eased with the blend, in degrees
 *                      towards the left of the way the move faces the player
 */
@Environment(EnvType.CLIENT)
public record ArcadeRenderData(@Nullable ArcadeAnimation animation, AnimationState state,
                               @Nullable ArcadeAnimation previous, AnimationState previousState, float weight, float turn) {
    public static final ArcadeRenderData NONE = new ArcadeRenderData(null, new AnimationState(), null, new AnimationState(), 1.0F, 0.0F);

    /** Whether the model is in an arcade animation, or easing out of one. */
    public boolean isPlaying() {
        return this.animation != null || this.blending() && this.previous != null;
    }

    /** Whether the model is between two poses. */
    public boolean blending() {
        return this.weight < 1.0F;
    }

    /** Whether an animation of this frame turns the whole body, in which case vanilla's own body rotations stay out of it. */
    public boolean drivesBody() {
        return this.turn != 0.0F
                || this.animation != null && this.animation.body().isPresent()
                || this.blending() && this.previous != null && this.previous.body().isPresent();
    }
}
