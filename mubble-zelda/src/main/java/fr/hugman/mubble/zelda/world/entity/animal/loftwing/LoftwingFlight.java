package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

/**
 * The numbers behind a ridden loftwing. Flight is deliberately not free: a bird that never tires
 * would turn the overworld into a flat map, so climbing and dashing both draw on the same reserve
 * of wingbeats, and only gliding pays it back.
 */
public final class LoftwingFlight {
	private LoftwingFlight() {
	}

	/** A full reserve, in ticks of climbing. */
	public static final int MAX_STAMINA = 200;
	/** What a tick of climbing costs. */
	public static final int CLIMB_COST = 1;
	/** What a tick of dashing costs, on top of the climb. */
	public static final int DASH_COST = 3;
	/** How much a tick of gliding gives back. */
	public static final int GLIDE_GAIN = 1;
	/** Gliding only pays back every other tick, so a reserve takes twice as long to fill as to spend. */
	public static final int GLIDE_INTERVAL = 2;

	/** Blocks per tick in level flight. */
	public static final double CRUISE_SPEED = 0.55;
	/** Blocks per tick while dashing. */
	public static final double DASH_SPEED = 1.1;
	/** How long one dash lasts. */
	public static final int DASH_TICKS = 20;
	/** How long before the bird will dash again. */
	public static final int DASH_COOLDOWN = 40;
	/** Climbing is slower than diving, as it is for anything with wings. */
	public static final double CLIMB_FACTOR = 0.6;
	public static final double DIVE_FACTOR = 1.4;

	/** Hitting a wall slower than this is a bump, not a crash. */
	public static final double CRASH_SPEED = 0.45;
	public static final float CRASH_DAMAGE_TO_RIDER = 6.0F;
	public static final float CRASH_DAMAGE_TO_LOFTWING = 12.0F;
	/** How hard a crash throws the rider off. */
	public static final double CRASH_EJECT_SPEED = 0.6;
}
