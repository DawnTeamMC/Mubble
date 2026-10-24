package fr.hugman.mubble.platformer;

import net.minecraft.util.Mth;

/**
 * How the orbit camera swings round behind the player on its own, the way the camera of Super Mario
 * Odyssey does: so slowly and so late that the player hardly notices it. The camera is the player's
 * to turn; following only tidies up after them.
 * <ul>
 *     <li><b>Speed</b> is what lets it turn readily: walking, it does not move; running, it barely
 *     does; rolling, diving or long jumping past the run speed, it follows plainly.</li>
 *     <li><b>Distance</b> makes up for the rest: going a long way in one direction, past a few
 *     strides, the camera slowly comes round, however slow the player goes. Turning sharply, or
 *     standing, starts the count afresh.</li>
 *     <li><b>The hand</b> comes first: once the player turns the camera, it stays where they left it
 *     for a few seconds, then wakes up gently.</li>
 *     <li><b>Smoothness</b>: the angular speed of the camera eases up and down, never jumps, so that
 *     it starts and stops turning as a dolly would.</li>
 *     <li><b>Facing the camera</b>, it never turns: the more the player runs towards it, the less it
 *     follows, and not at all past 45 degrees off straight at it.</li>
 * </ul>
 * Common code, so that unit tests can hold it; the client steps it every frame.
 */
public final class PlatformerCameraFollow {
    /** How long the camera stays where the player turned it, in seconds. */
    public static final double TOUCH_DELAY = 2.5D;
    /** How long following takes to come back in full, after that, in seconds. */
    public static final double RESUME_SECONDS = 1.5D;
    /** The slowest the player may go for the camera to follow them, in blocks per tick: standing still, it never moves. */
    public static final double MIN_SPEED = 0.02D;
    /** Between which shares of the run speed the speed alone brings following from nothing to its full rate. */
    public static final double SPEED_FROM = 0.9D;
    public static final double SPEED_TO = 2.0D;
    /** How far the player has to go in one direction, in blocks, before the distance starts to count, and to count in full. */
    public static final double TRAVEL_FROM = 10.0D;
    public static final double TRAVEL_TO = 32.0D;
    /** The share of the full rate a long way in one direction is worth, at the run speed and below. */
    public static final double TRAVEL_GAIN = 0.3D;
    /** How far the player may turn from where they were going, in degrees, and still go in the same direction. */
    public static final double HEADING_TOLERANCE = 45.0D;
    /** How fast where they were going follows a gentle curve, in degrees per second. */
    public static final double HEADING_FOLLOW = 60.0D;
    /** How long the distance takes to fade by about two thirds while the player stands, in seconds. */
    public static final double STOP_FADE = 0.75D;
    /** How fast the angular speed of the camera may change, in degrees per second, every second. */
    public static final double ACCELERATION = 60.0D;
    /** Past this far from where the player goes, in degrees, the player runs towards the camera and it starts to let go. */
    public static final double FACING_FROM = 90.0D;
    /** And past this far, it no longer follows at all. */
    public static final double FACING_TO = 135.0D;

    /** How far the player went in one direction, in blocks. */
    private double travel;
    /** Where the player has been going, in degrees, and whether they have been going anywhere yet. */
    private float heading;
    private boolean headed;
    /** How fast the camera turns, in degrees per second, signed. */
    private double rate;
    /** How long since the player last turned the camera, in seconds. */
    private double sinceTouch = Double.MAX_VALUE;

    /** The player turned the camera: it stays where they left it a while, and the distance counts afresh. */
    public void touch() {
        this.sinceTouch = 0.0D;
        this.rate = 0.0D;
        this.travel = 0.0D;
    }

    /** Stops the camera turning on the spot, as when something else (a recenter) turns it. */
    public void halt() {
        this.rate = 0.0D;
    }

    /** Forgets everything: following is off, or the orbit is. */
    public void reset() {
        this.travel = 0.0D;
        this.headed = false;
        this.rate = 0.0D;
        this.sinceTouch = Double.MAX_VALUE;
    }

    /** How fast the camera turns, in degrees per second, signed. */
    public double rate() {
        return this.rate;
    }

    /** How far the player went in one direction, in blocks. */
    public double travel() {
        return this.travel;
    }

    /**
     * One step of following.
     *
     * @param cameraYaw where the camera looks, in degrees
     * @param playerYaw where the player faces, which is where they go, in degrees
     * @param speed     how fast the player goes, in blocks per tick
     * @param runSpeed  the run speed of the player, in blocks per tick
     * @param topRate   the fastest the camera may turn, rolling sideways past the run speed, in degrees per second
     * @param seconds   how long the step lasts
     * @return how far the camera turns, in degrees
     */
    public float step(float cameraYaw, float playerYaw, double speed, double runSpeed, double topRate, double seconds) {
        if (seconds <= 0.0D) {
            return 0.0F;
        }
        this.sinceTouch = Math.min(this.sinceTouch + seconds, Double.MAX_VALUE / 2.0D);
        double share = speed / Math.max(1.0E-3D, runSpeed);
        boolean moving = speed >= MIN_SPEED;

        // how far the player went in one direction: a gentle curve keeps counting, a sharp turn starts afresh
        if (!moving) {
            this.travel *= Math.exp(-seconds / STOP_FADE);
        } else if (!this.headed || Math.abs(Mth.degreesDifference(this.heading, playerYaw)) > HEADING_TOLERANCE) {
            this.heading = playerYaw;
            this.headed = true;
            this.travel = speed * 20.0D * seconds;
        } else {
            this.heading = Mth.approachDegrees(this.heading, playerYaw, (float) (HEADING_FOLLOW * seconds));
            this.travel += speed * 20.0D * seconds;
        }

        // how readily the camera follows: speed, then the way gone, then how long since the hand let go
        double gain = 0.0D;
        if (moving) {
            double fromSpeed = smoothstep((share - SPEED_FROM) / (SPEED_TO - SPEED_FROM));
            double fromTravel = TRAVEL_GAIN * smoothstep((this.travel - TRAVEL_FROM) / (TRAVEL_TO - TRAVEL_FROM)) * Math.min(1.0D, share);
            gain = Math.min(1.0D, fromSpeed + fromTravel);
        }
        gain *= smoothstep((this.sinceTouch - TOUCH_DELAY) / RESUME_SECONDS);

        // the more sideways the player goes, the faster, but never round when they run at the camera
        double gap = Mth.degreesDifference(cameraYaw, playerYaw);
        double off = Math.abs(gap);
        double sideways = Math.sin(off * Mth.DEG_TO_RAD) * (1.0D - smoothstep((off - FACING_FROM) / (FACING_TO - FACING_FROM)));
        double target = Math.signum(gap) * topRate * gain * sideways;

        // the angular speed eases towards what it should be, and the camera never turns past the player
        double change = ACCELERATION * seconds;
        this.rate += Mth.clamp(target - this.rate, -change, change);
        double turn = this.rate * seconds;
        if (turn * gap > 0.0D && Math.abs(turn) > off) {
            turn = gap;
        }
        return (float) turn;
    }

    private static double smoothstep(double t) {
        t = Mth.clamp(t, 0.0D, 1.0D);
        return t * t * (3.0D - 2.0D * t);
    }
}
