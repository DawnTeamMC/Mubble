package fr.hugman.mubble.splatoon;

import net.minecraft.SharedConstants;

/**
 * Converts values from Splatoon 3 to Minecraft.
 *
 * <table>
 *     <caption>Conversion</caption>
 *     <tr><th>Real life</th><th>Minecraft</th><th>Splatoon 3</th></tr>
 *     <tr><td>1 second</td><td>20 ticks</td><td>60 frames</td></tr>
 *     <tr><td>full health</td><td>20 HP</td><td>1000 HP (displayed as 100)</td></tr>
 *     <tr><td>about 1 meter</td><td>1 block</td><td>1 unit</td></tr>
 * </table>
 *
 * <p>Splatoon 3 units are what the parameter tables use (the test range lines are 5 units apart), and they match
 * blocks well: walking while firing a Splattershot ({@code MoveSpeed} 0.072 units per frame) is as fast as walking in
 * Minecraft. Splatoon 2 used units ten times smaller.
 *
 * <p>Since a tick lasts exactly {@value #FRAMES_PER_TICK} frames, anything Splatoon does every frame is done
 * {@value #FRAMES_PER_TICK} times per tick, with its own values, rather than rescaled to ticks.
 *
 * @author Hugman
 * @since 4.0.0
 */
public final class SplatoonConversions {
    public static final int MINECRAFT_TPS = SharedConstants.TICKS_PER_SECOND;
    public static final int SPLATOON_TPS = 60;
    public static final int FRAMES_PER_TICK = SPLATOON_TPS / MINECRAFT_TPS;
    public static final float BLOCKS_PER_UNIT = 1.0F;
    public static final int MINECRAFT_FULL_HEALTH = 20;
    public static final int SPLATOON_FULL_HEALTH = 1000;
    /**
     * How fast a player walks in Minecraft, in blocks per second.
     */
    public static final float MINECRAFT_WALK_SPEED = 4.317F;

    public static final float DAMAGE_RATIO = (float) MINECRAFT_FULL_HEALTH / SPLATOON_FULL_HEALTH;

    private SplatoonConversions() {
    }

    /**
     * Converts Splatoon 3 damage to Minecraft health points.
     */
    public static float damage(float damage) {
        return damage * DAMAGE_RATIO;
    }

    /**
     * Converts Splatoon 3 units to blocks.
     */
    public static double distance(double units) {
        return units * BLOCKS_PER_UNIT;
    }

    /**
     * Converts a speed in units per frame to blocks per tick.
     */
    public static double speed(double unitsPerFrame) {
        return unitsPerFrame * BLOCKS_PER_UNIT * FRAMES_PER_TICK;
    }
}
