package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

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
	/** How far round the circle the bird aims each time, in radians: a twelfth of a lap. */
	private static final float STEP = (float) (Math.PI / 6.0);
	/** How far above the clouds it settles. */
	private static final int MIN_HEIGHT_ABOVE_CLOUDS = 4;
	private static final int MAX_HEIGHT_ABOVE_CLOUDS = 28;
	/** How close is close enough to aim at the next point of the circle. */
	private static final double WAYPOINT_REACHED = 6.0;
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
	public void tick() {
		if (++this.ticksOnCircle > CIRCLE_TICKS) {
			this.pickCircle();
		}
		Vec3 waypoint = this.waypoint();
		if (!this.loftwing.getMoveControl().hasWanted() || this.loftwing.position().distanceTo(waypoint) < WAYPOINT_REACHED) {
			this.angle += STEP;
			waypoint = this.waypoint();
			this.loftwing.getMoveControl().setWantedPosition(waypoint.x(), waypoint.y(), waypoint.z(), SPEED);
		}
	}

	/** Centres a fresh circle on where the bird is now, at cruising height. */
	private void pickCircle() {
		var random = this.loftwing.getRandom();
		this.centerX = this.loftwing.getX();
		this.centerZ = this.loftwing.getZ();
		this.angle = random.nextFloat() * (float) (Math.PI * 2.0);
		float clouds = LoftwingSky.cloudLevel(this.loftwing.level(), this.loftwing.blockPosition());
		this.altitude = clouds + MIN_HEIGHT_ABOVE_CLOUDS + random.nextInt(MAX_HEIGHT_ABOVE_CLOUDS - MIN_HEIGHT_ABOVE_CLOUDS);
		this.ticksOnCircle = 0;
	}

	private Vec3 waypoint() {
		return new Vec3(
				this.centerX + Math.cos(this.angle) * RADIUS,
				this.altitude,
				this.centerZ + Math.sin(this.angle) * RADIUS
		);
	}
}
