package fr.hugman.mubble.arcade.sim;

import net.minecraft.util.Mth;

/**
 * Folds the inputs sampled every frame into one {@link ArcadeInputFrame} per tick.
 * <p>
 * At 20 ticks per second, polling the keys once per tick misses whatever happens in between: a jump
 * pressed and released within the same 50 ms never shows up, and a press right before a tick looks
 * exactly like one right after the previous tick. Sampled every frame, a press is caught however
 * short it is, and stamped with the time it happened, which is what lets the jump buffer count its
 * milliseconds from the press itself rather than from the next tick.
 */
public final class ArcadeInputCollector {
    private static final int[] ACTIONS = {ArcadeInputFrame.JUMP, ArcadeInputFrame.CROUCH, ArcadeInputFrame.ACTION, ArcadeInputFrame.SPRINT, ArcadeInputFrame.SPIN};
    private static final long NANOS_PER_MILLI = 1_000_000L;

    private int held;
    private int pressedSinceTick;
    private final long[] lastPress = new long[ACTIONS.length];

    /**
     * Takes in one frame of input.
     *
     * @param nanos  when the frame was sampled, in nanoseconds of any monotonic clock
     * @param down   the actions held down, as {@link ArcadeInputFrame} bits
     * @param clicks the actions pressed since the previous frame, even if already released
     */
    public void sampleFrame(long nanos, int down, int clicks) {
        int presses = (down & ~this.held) | clicks;
        for (int i = 0; i < ACTIONS.length; i++) {
            if ((presses & ACTIONS[i]) != 0) {
                this.lastPress[i] = nanos;
            }
        }
        this.pressedSinceTick |= presses;
        this.held = down;
    }

    /**
     * Closes the tick: everything sampled since the previous one becomes a frame.
     *
     * @param nanos when the tick runs, on the same clock as the samples
     */
    public ArcadeInputFrame tick(int tick, long nanos, float stickX, float stickZ, float viewYaw, boolean coupled) {
        var frame = new ArcadeInputFrame(
                tick, stickX, stickZ, viewYaw, coupled,
                (byte) this.held,
                (byte) this.pressedSinceTick,
                this.ageMs(0, ArcadeInputFrame.JUMP, nanos),
                this.ageMs(2, ArcadeInputFrame.ACTION, nanos)
        );
        this.pressedSinceTick = 0;
        return frame;
    }

    private short ageMs(int index, int action, long nanos) {
        if ((this.pressedSinceTick & action) == 0) {
            return 0;
        }
        return (short) Mth.clamp((nanos - this.lastPress[index]) / NANOS_PER_MILLI, 0L, 1000L);
    }

    /** Forgets everything: a frame built right after holds no press. */
    public void reset() {
        this.held = 0;
        this.pressedSinceTick = 0;
    }
}
