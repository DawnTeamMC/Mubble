package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

/**
 * The numbers behind a ridden loftwing, and nothing but the numbers: the model that reads them
 * lives in {@link Loftwing}, under {@code RIDING}.
 * <p>
 * That model is a glider with a wingbeat. The bird carries one scalar of speed along its own nose,
 * and gravity works on that speed rather than on the bird: pointing the nose down spends height to
 * buy speed, pointing it up spends the speed back for height. A dive is therefore how a rider earns
 * a climb, and the steeper and longer the dive the higher the climb it pays for. Drag takes its cut
 * of both, so none of it is free, and a wingbeat is the one thing that puts energy back in — which
 * is why a loftwing can climb all day where an elytra cannot, and why it comes down once the
 * reserve of wingbeats is spent.
 */
public final class LoftwingFlight {
	private LoftwingFlight() {
	}

	// THE RESERVE

	/** A full reserve, in stamina. Worth {@link #MAX_STAMINA} / {@link #FLAP_COST} wingbeats. */
	public static final int MAX_STAMINA = 200;
	/** What one wingbeat costs. A full reserve should carry a bird from the sea to the clouds. */
	public static final int FLAP_COST = 4;
	/** What a tick of dashing costs. */
	public static final int DASH_COST = 3;
	/** How much a tick of gliding gives back. */
	public static final int GLIDE_GAIN = 1;
	/** Gliding only pays back every other tick, so a reserve takes twice as long to fill as to spend. */
	public static final int GLIDE_INTERVAL = 2;

	// SPEED

	/**
	 * The slowest a loftwing ever flies, in blocks a tick. This is a safety net and nothing more:
	 * it is there so a bird that has spent everything on a climb keeps gliding instead of hanging
	 * in the air. Nothing drops the nose when the speed gets here — the pitch stays the rider's.
	 */
	public static final double MIN_SPEED = 0.25;
	/** The fastest, in blocks a tick. Drag alone would let a dive past this, so it is a real ceiling. */
	public static final double MAX_SPEED = 1.8;
	/** How much speed a vertical dive buys in one tick, and a vertical climb gives back. */
	public static final double GRAVITY = 0.045;
	/**
	 * What the air costs, against the square of the speed. It is deliberately low: drag is what
	 * eats the speed a dive earned before the climb can spend it on height, so raising it takes
	 * the point out of diving. What it does set is the speed steady wingbeats settle at,
	 * {@code sqrt((FLAP_SPEED_GAIN / FLAP_INTERVAL) / DRAG)}.
	 */
	public static final double DRAG = 0.005;
	/**
	 * The height a gliding loftwing always loses, in blocks a tick, whatever its nose is doing.
	 * This is what makes letting go of the wingbeat enough to land, and it has to stay above
	 * {@code MIN_SPEED * sin(MAX_CLIMB_PITCH)}, or a bird at its slowest would climb for nothing
	 * and a reserve of wingbeats would buy no height worth having.
	 */
	public static final double GLIDE_SINK = 0.19;
	/** How much of the gap to the heading it wants a bird closes in one tick. Lower is heavier. */
	public static final double HEADING_INERTIA = 0.2;

	// WINGBEATS

	/** How hard one beat throws the bird upwards, in blocks a tick. */
	public static final double FLAP_LIFT = 1.0;
	/** What is left of a beat's lift a tick later. The rest of the surge is the heading inertia. */
	public static final double FLAP_LIFT_DECAY = 0.8;
	/** How much speed a beat adds on top of the lift, in blocks a tick. With {@link #DRAG}, this is what sets the cruise. */
	public static final double FLAP_SPEED_GAIN = 0.03;
	/** How long before the wings can beat again, so holding the key gives a rhythm and not a shove. */
	public static final int FLAP_INTERVAL = 8;

	// TURNING

	/** Degrees of yaw a bare-headed loftwing comes round in one tick, at {@link #MIN_SPEED}. */
	public static final float YAW_DEGREES_PER_TICK = 2.5F;
	/** Degrees of pitch. The nose answers quicker than the body: it is what a rider trades with. */
	public static final float PITCH_DEGREES_PER_TICK = 5.0F;
	/**
	 * How much speed widens a turn. Every this many blocks a tick above {@link #MIN_SPEED} halves
	 * the yaw rate, so a bird at a dive's speed comes round in a long arc where a gliding one turns
	 * almost on the spot.
	 */
	public static final double YAW_WIDENING_SPEED = 1.0;
	/** How far up a rider may point the bird. See {@link #GLIDE_SINK} for what caps this. */
	public static final float MAX_CLIMB_PITCH = 45.0F;
	/** And how far down. A dive only ever buys speed, so it can be far steeper than a climb. */
	public static final float MAX_DIVE_PITCH = 80.0F;

	// DASHING

	/** How much speed a tick of dashing adds, before drag. */
	public static final double DASH_THRUST = 0.03;
	/** How long one dash lasts. */
	public static final int DASH_TICKS = 20;
	/** How long before the bird will dash again. */
	public static final int DASH_COOLDOWN = 40;

	// CRASHING

	/** Hitting a wall slower than this is a bump, not a crash: a bird gliding in to land is spared. */
	public static final double CRASH_SPEED = 0.6;
	public static final float CRASH_DAMAGE_TO_RIDER = 6.0F;
	public static final float CRASH_DAMAGE_TO_LOFTWING = 12.0F;
	/** How hard a crash throws the rider off. */
	public static final double CRASH_EJECT_SPEED = 0.6;
}
