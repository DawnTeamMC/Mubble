package fr.hugman.mubble.arcade;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * The point the orbit camera looks at, framing the player the way the cameras of 3D platformers do
 * rather than nailing them to the middle of the screen: a step aside, a hop or a jump in place
 * leaves the camera where it is, and it is moving on that brings the player back to the middle.
 * <ul>
 *     <li><b>Sideways</b>, the player moves freely within a margin around the focus. Past it, the
 *     focus is pushed along, softly, and never lets the player more than a hard limit away. While the
 *     player keeps moving, the focus catches up faster than they go, so that a run ends with the
 *     player in the middle; a step or two does not.</li>
 *     <li><b>Upwards</b>, as in platformers since Super Mario World, the height of the camera only
 *     follows the ground: while the player is in the air, it stays put, as long as they stay within
 *     a band above it and do not fall below it. Back on the ground, on a ladder, a wall or in water,
 *     it eases to the height of the feet.</li>
 * </ul>
 * Turning the camera lets go of the offset too, gently, as much as it turns: the offset only stays
 * while neither the camera nor the player moves.
 * Every distance scales with a margin setting: at 0, the focus sits on the player.
 */
public final class ArcadeFraming {
    /** How far the player may stray sideways before the focus moves, in blocks. */
    public static final double SIDE_MARGIN = 0.8D;
    /** How far above the focus the player may jump before it rises, in blocks: past a plain jump. */
    public static final double ABOVE_MARGIN = 2.5D;
    /** How far below the focus the player may fall before it follows, in blocks. */
    public static final double BELOW_MARGIN = 0.75D;
    /** How far past a margin the player may get, once it pushes the focus, in blocks. */
    public static final double HARD_LIMIT = 1.0D;
    /** Share of how far past a margin the player is that the focus leaves every tick: it follows softly rather than locked. */
    public static final double EDGE_KEEP = 0.5D;
    /** Share of the height between the focus and the feet of a player on the ground left every tick. */
    public static final double GROUND_KEEP = 0.75D;
    /** How much faster than the player the focus catches up, once they keep moving. */
    public static final double CATCH_UP = 1.25D;
    /** The slowest the player may go, in blocks per tick, for the focus to catch up with them. */
    public static final double MOVING_SPEED = 0.05D;
    /** How many ticks of moving it takes for the focus to catch up at full speed, and of standing still for it to stop. */
    public static final double ENGAGE_TICKS = 12.0D;
    public static final double RELEASE_TICKS = 6.0D;
    /** How far into moving the player has to be before the focus starts catching up: a step or two is not moving on. */
    public static final double ENGAGE_DELAY = 0.4D;
    /** Share of the way to the player the focus leaves every tick of a recenter, whatever the margins. */
    public static final double RECENTER_KEEP = 0.6D;
    /** How close to the player the focus has to come for a recenter to be over, in blocks. */
    public static final double RECENTERED = 0.01D;
    /** How far the camera has to turn, in degrees, for the player to come about two thirds of the way back to the middle. */
    public static final double TURN_RECENTER_DEGREES = 90.0D;
    /** Farther than this, in blocks, the player was teleported, and the focus snaps to them. */
    public static final double SNAP_DISTANCE = 8.0D;

    @Nullable
    private Vec3 focus;
    @Nullable
    private Vec3 previousTarget;
    /** How far into catching up the focus is, from 0 (the player stands, or only just moved) to 1. */
    private double engagement;
    /** Whether the focus is on its way back onto the player, see {@link #recenter()}. */
    private boolean recentering;

    /** The focus, or {@code null} before the first tick. */
    @Nullable
    public Vec3 focus() {
        return this.focus;
    }

    /** Puts the focus back on the player, on the next tick. */
    public void reset() {
        this.focus = null;
        this.previousTarget = null;
        this.engagement = 0.0D;
        this.recentering = false;
    }

    /** Brings the focus back onto the player, quickly, margins aside: the player ends in the middle of the screen. */
    public void recenter() {
        this.recentering = true;
    }

    /** Whether the focus is still on its way back onto the player. */
    public boolean isRecentering() {
        return this.recentering;
    }

    /** Corrects the focus from outside, as when it would sit behind a block. */
    public void set(Vec3 focus) {
        this.focus = focus;
    }

    /**
     * One tick of the focus after the feet of the player.
     *
     * @param target    where the feet of the player are
     * @param supported whether the player stands on something: the ground, a ladder, a wall, water
     * @param margin    the scale of every margin, 1 by default, 0 for a focus on the player
     * @param turned    how far the camera turned since the last tick, in degrees: turning it brings the
     *                  player back to the middle, gently, as moving on does
     */
    public Vec3 tick(Vec3 target, boolean supported, double margin, double turned) {
        var focus = this.focus;
        var previous = this.previousTarget;
        this.previousTarget = target;
        if (focus == null || previous == null || focus.distanceTo(target) > SNAP_DISTANCE || previous.distanceTo(target) > SNAP_DISTANCE) {
            this.engagement = 0.0D;
            this.recentering = false;
            return this.focus = target;
        }
        if (this.recentering) {
            var gap = target.subtract(focus).scale(RECENTER_KEEP);
            if (gap.length() < RECENTERED) {
                this.recentering = false;
                return this.focus = target;
            }
            return this.focus = target.subtract(gap);
        }
        margin = Math.max(0.0D, margin);

        // sideways: catching up while the player keeps moving, pushed along past the margin
        double speed = Math.sqrt(Mth.square(target.x - previous.x) + Mth.square(target.z - previous.z));
        this.engagement = speed > MOVING_SPEED
                ? Math.min(1.0D, this.engagement + 1.0D / ENGAGE_TICKS)
                : Math.max(0.0D, this.engagement - 1.0D / RELEASE_TICKS);
        double gapX = target.x - focus.x;
        double gapZ = target.z - focus.z;
        double gap = Math.sqrt(gapX * gapX + gapZ * gapZ);
        double catchUp = Math.max(0.0D, (this.engagement - ENGAGE_DELAY) / (1.0D - ENGAGE_DELAY)) * CATCH_UP;
        double side = Math.max(0.0D, gap - catchUp * speed);
        side = soften(side, SIDE_MARGIN * margin, (SIDE_MARGIN + HARD_LIMIT) * margin);
        double scale = gap > 1.0E-9D ? side / gap : 0.0D;

        // upwards: the ground sets the height, the air only pushes it past the band
        double height = target.y - focus.y;
        if (supported) {
            height *= GROUND_KEEP;
        }
        if (height >= 0.0D) {
            height = soften(height, ABOVE_MARGIN * margin, (ABOVE_MARGIN + HARD_LIMIT) * margin);
        } else {
            height = -soften(-height, BELOW_MARGIN * margin, (BELOW_MARGIN + HARD_LIMIT) * margin);
        }

        // turning the camera lets go of the offset, as much as it turned: still, it stays
        double keep = Math.exp(-Math.abs(turned) / TURN_RECENTER_DEGREES);
        scale *= keep;
        height *= keep;

        return this.focus = new Vec3(target.x - gapX * scale, target.y - height, target.z - gapZ * scale);
    }

    /** A distance past {@code margin} shrunk towards it, softly, and never past {@code limit}. */
    private static double soften(double distance, double margin, double limit) {
        if (distance <= margin) {
            return distance;
        }
        return Math.min(margin + (distance - margin) * EDGE_KEEP, limit);
    }
}
