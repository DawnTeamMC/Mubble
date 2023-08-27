package fr.hugman.mubble.util;

import net.minecraft.SharedConstants;

/**
 * This class contains methods to convert values from Splatoon 3 to Minecraft.
 *
 *
 * <table>
 *     <caption>Conversion</caption>
 *     <tr><th>Real life</th><th>Minecraft</th><th>Splatoon 3</th></tr>
 *     <tr><td>1 second</td><td>20 ticks</td><td>60 frames</td></tr>
 *     <tr><td></td><td>20HP</td><td>1000HP</td></tr>
 *     <tr><td>1 meter</td><td>1 block</td><td>10 units</td></tr>
 * </table>
 *
 * @author Hugman
 * @since 4.0.0
 */
public final class SplatoonConversions {
    public static final float TICK_RATIO = (float) SharedConstants.TICKS_PER_SECOND / 60;
    public static final float DAMAGE_RATIO = 20.0F / (100 * 10);
    public static final float DISTANCE_RATIO = 1.0f / 10f;
    public static final float SPEED_RATIO = DISTANCE_RATIO / TICK_RATIO;

    /**
     * Converts Splatoon 3 frames to Minecraft ticks.
     */
    public static long time(int frames) {
        return (long) (frames * TICK_RATIO);
    }

    /**
     * Converts Splatoon 3 damage to Minecraft damage.
     */
    public static float damage(int damage) {
        return damage * DAMAGE_RATIO;
    }

    /**
     * Converts Splatoon 3 units to Minecraft blocks.
     */
    public static float distance(float units) {
        return units * DISTANCE_RATIO;
    }

    /**
     * Converts speeds.
     */
    public static float speed(float unitsPerFrame) {
        return unitsPerFrame * SPEED_RATIO;
    }
}
