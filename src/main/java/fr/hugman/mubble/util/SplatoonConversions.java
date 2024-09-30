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
    public static final int MINECRAFT_TPS = SharedConstants.TICKS_PER_SECOND;
    public static final int SPLATOON_TPS = 60;
    public static final float MINECRAFT_METER = 1.0f;
    public static final float SPLATOON_METER = 1.0f;
    public static final int MINECRAFT_FULL_HEALTH = 20;
    public static final int SPLATOON_FULL_HEALTH = 1000;

    public static final float TPS_RATIO = (float) MINECRAFT_TPS / SPLATOON_TPS;
    public static final float DISTANCE_RATIO = MINECRAFT_METER / SPLATOON_METER;
    public static final float DAMAGE_RATIO = (float) MINECRAFT_FULL_HEALTH / SPLATOON_FULL_HEALTH;

    public static final float SPEED_RATIO = (MINECRAFT_METER / MINECRAFT_TPS) / (SPLATOON_METER / SPLATOON_TPS);

    /**
     * Converts Splatoon 3 frames to Minecraft ticks.
     */
    public static long time(int frames) {
        return (long) (frames * TPS_RATIO);
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
