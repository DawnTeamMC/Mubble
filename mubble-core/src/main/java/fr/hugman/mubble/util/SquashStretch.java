package fr.hugman.mubble.util;

import net.minecraft.util.Mth;
import org.joml.Vector3f;

/**
 * Cartoon squash and stretch: scales that keep the volume of what they deform.
 */
public final class SquashStretch {
    private static final float MIN_SCALE = 0.05F;

    private SquashStretch() {
    }

    /**
     * @return the scale the two other axes take when one is scaled by {@code main}
     */
    public static float side(float main) {
        return (float) (1.0D / Math.sqrt(Math.max(MIN_SCALE, main)));
    }

    /**
     * @param amount stretches when positive, squashes when negative, 0 leaving the shape alone
     */
    public static Vector3f alongX(float amount) {
        float main = Math.max(MIN_SCALE, 1.0F + amount);
        float side = side(main);
        return new Vector3f(main, side, side);
    }

    public static Vector3f alongY(float amount) {
        float main = Math.max(MIN_SCALE, 1.0F + amount);
        float side = side(main);
        return new Vector3f(side, main, side);
    }

    public static Vector3f alongZ(float amount) {
        float main = Math.max(MIN_SCALE, 1.0F + amount);
        float side = side(main);
        return new Vector3f(side, side, main);
    }

    /**
     * Rises from 0 to 1 and back over a progress going from 0 to 1.
     */
    public static float pulse(float progress) {
        return Mth.sin(Mth.clamp(progress, 0.0F, 1.0F) * Mth.PI);
    }

    /**
     * Rings out after a hit: starts at 1, swings around 0 and dies off as progress goes from 0 to 1.
     *
     * @param swings how many times it swings back and forth on the way
     */
    public static float wobble(float progress, float swings) {
        float clamped = Mth.clamp(progress, 0.0F, 1.0F);
        float fade = 1.0F - clamped;
        return Mth.cos(clamped * swings * Mth.TWO_PI) * fade * fade;
    }

    /**
     * @return how much of {@code max} a speed is worth, {@code fullSpeed} and above being all of it
     */
    public static float fromSpeed(double speed, double fullSpeed, float max) {
        if (fullSpeed <= 0.0D) {
            return 0.0F;
        }
        return max * (float) Mth.clamp(speed / fullSpeed, 0.0D, 1.0D);
    }
}
