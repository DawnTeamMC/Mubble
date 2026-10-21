package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * What a loftwing does when nothing else needs doing, which is nearly always: it climbs back above
 * the clouds and rides wide, slow circles up there. Every other goal of the bird outranks this one,
 * so an errand below simply interrupts the circle and the bird picks it up again afterwards.
 */
public class LoftwingSoarGoal extends Goal {
	private static final double SPEED = 1.0;
	/** How wide the circle is, in blocks. */
	private static final double RADIUS = 20.0;
	/** How far round the circle the bird keeps its aim ahead of itself, in degrees: a twelfth of a lap. */
	private static final float LEAD = 30.0F;
	/** How far round the circle the aim may slide in one tick, in degrees. */
	private static final float AIM_SLIDE = 3.0F;
	/** A yaw is measured from south and the angle round the circle from east: a quarter turn apart. */
	private static final float YAW_TO_CIRCLE = 90.0F;
	/** How far above the clouds it settles. */
	private static final int MIN_HEIGHT_ABOVE_CLOUDS = 4;
	private static final int MAX_HEIGHT_ABOVE_CLOUDS = 28;
	/** However low the clouds are, the bird keeps at least this much air under it. */
	private static final int MIN_CLEARANCE_ABOVE_GROUND = 12;
	/** How far outside its own circle the bird can be found before that circle is somewhere it used to be. */
	private static final double OFF_THE_CIRCLE = RADIUS / 2.0;
	/** A circle is left behind after this long, so that birds drift instead of grinding a groove in the sky. */
	private static final int CIRCLE_TICKS = 600;

	private final Loftwing loftwing;

	private double centerX;
	private double centerZ;
	private double altitude;
	private float angle;
	private int ticksOnCircle;

	public LoftwingSoarGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return this.loftwing.isFreeToRoam() && !this.loftwing.isRoosting() && !this.loftwing.isPreening();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void start() {
		this.pickCircle();
	}

	@Override
	public void stop() {
		this.loftwing.getNavigation().stop();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		if (++this.ticksOnCircle > CIRCLE_TICKS || this.hasLeftTheCircle()) {
			this.pickCircle();
		}
		this.aimAhead();
		Vec3 waypoint = this.waypoint();
		// A flying move control only steers for the one tick after it is told where to go, and lets
		// the bird fall the rest of the time: it has to be aimed again every single tick.
		this.loftwing.getMoveControl().setWantedPosition(waypoint.x(), waypoint.y(), waypoint.z(), SPEED);
	}

	/** Centres a fresh circle on where the bird is now, at cruising height. */
	private void pickCircle() {
		var random = this.loftwing.getRandom();
		this.centerX = this.loftwing.getX();
		this.centerZ = this.loftwing.getZ();
		// A fresh circle starts from wherever the bird is already pointed, so that picking one is a
		// lean and not a snap: the point it steers for stays straight ahead of its beak.
		this.angle = this.loftwing.getYRot() + YAW_TO_CIRCLE;

		float clouds = LoftwingSky.cloudLevel(this.loftwing.level(), this.loftwing.blockPosition());
		this.altitude = clouds + MIN_HEIGHT_ABOVE_CLOUDS + random.nextInt(MAX_HEIGHT_ABOVE_CLOUDS - MIN_HEIGHT_ABOVE_CLOUDS);
		this.ticksOnCircle = 0;
	}

	/**
	 * Whether the bird has ended up somewhere its circle no longer explains: knocked out of the sky,
	 * carried off, or simply fallen. A circle is a stretch of sky the bird rides, not a destination
	 * it owes anything to, so once the bird is off it, steering for it is steering back to where the
	 * bird used to be — which is exactly how one that had come down kept hauling itself towards the
	 * spot it fell from.
	 */
	private boolean hasLeftTheCircle() {
		return Math.hypot(this.loftwing.getX() - this.centerX, this.loftwing.getZ() - this.centerZ) > RADIUS + OFF_THE_CIRCLE;
	}

	/**
	 * Slides the aim round the circle so that it stays a twelfth of a lap ahead of wherever the bird
	 * has got to, rather than being a point the bird arrives at and then has to be given a new one.
	 * <p>
	 * Which is the whole trick. A point the bird can reach is a point it ends up sitting on, and the
	 * direction to a point underfoot is rounding error; a flying move control is allowed a quarter
	 * turn a tick to chase that direction, which is how a perched loftwing span like a top. Kept a
	 * lap-twelfth ahead, the aim is never close enough to the bird to be ill-aimed.
	 * <p>
	 * It slides rather than jumps, by a few degrees a tick, for the same reason: a bird at the very
	 * middle of its own circle has no bearing worth reading, and rate-limiting the slide lets that
	 * rounding error pass by instead of being steered on.
	 */
	private void aimAhead() {
		double dx = this.loftwing.getX() - this.centerX;
		double dz = this.loftwing.getZ() - this.centerZ;
		float bearing = (float) (Math.atan2(dz, dx) * Mth.RAD_TO_DEG);
		this.angle = Mth.approachDegrees(this.angle, bearing + LEAD, AIM_SLIDE);
	}

	/**
	 * The height to actually aim for. A bird already in the air makes for the cruising altitude,
	 * above or below it; a bird with its feet on something always aims up, whatever the clouds of
	 * this dimension are doing, because the alternative is pressing itself into the floor.
	 */
	private double targetAltitude() {
		return this.loftwing.onGround()
				? Math.max(this.altitude, this.loftwing.getY() + MIN_CLEARANCE_ABOVE_GROUND)
				: this.altitude;
	}

	private Vec3 waypoint() {
		float radians = this.angle * Mth.DEG_TO_RAD;
		return new Vec3(
				this.centerX + Math.cos(radians) * RADIUS,
				this.targetAltitude(),
				this.centerZ + Math.sin(radians) * RADIUS
		);
	}
}
