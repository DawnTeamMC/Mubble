package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

/**
 * The numbers behind a whistle. Blowing one is how anyone survives stepping off Skyloft, so the
 * one that matters most is how long the bird has to get there: a rescue that arrives late is no
 * rescue at all.
 */
public final class LoftwingCall {
	private LoftwingCall() {
	}

	/** How long a bird keeps flying towards a whistle before it gives up on finding its owner. */
	public static final int GIVE_UP_TICKS = 600;

	/**
	 * How long a falling owner waits before the bird stops flying and simply appears beneath them.
	 * <p>
	 * No wings cross a hundred blocks in the second a fall lasts, so past this the last of the
	 * distance is crossed in one beat. Half a second is long enough to see the bird coming and
	 * short enough that nobody hits the ground first.
	 */
	public static final int RESCUE_TICKS = 10;

	/** How far below its owner the bird comes in, so they land on its back rather than beside it. */
	public static final double RESCUE_DROP = 1.6;

	/**
	 * How far someone has to have dropped before the bird reads it as a fall worth saving. A jump
	 * never gains this much, so hopping on the spot does not summon a bird under your feet.
	 */
	public static final double RESCUE_FALL_DISTANCE = 2.0;
}
