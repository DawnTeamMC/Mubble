package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * A loftwing that spots someone it does not know comes down for a look: it circles them for a while,
 * head turned their way, then climbs back. Without this the birds would never meet anyone, since
 * they spend their lives above the clouds, and nobody would ever be chosen by one.
 */
public class LoftwingCuriosityGoal extends Goal {
	private static final double SPEED = 1.0;
	private static final double SIGHT_RANGE = 32.0;
	/** How far off it keeps while it looks you over. */
	private static final double ORBIT_RADIUS = 6.0;
	private static final double ORBIT_HEIGHT = 3.0;
	/** Roughly once every fifteen seconds it bothers to look for someone. */
	private static final int INTEREST_INTERVAL = 300;
	private static final int MIN_TICKS = 200;
	private static final int MAX_TICKS = 400;
	private static final float STEP = (float) (Math.PI / 20.0);

	private final Loftwing loftwing;

	@Nullable
	private Player subject;
	private int ticksLeft;
	private float angle;

	public LoftwingCuriosityGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (!this.loftwing.isFreeToRoam() || this.loftwing.isRoosting() || this.loftwing.isPreening()) {
			return false;
		}
		if (this.loftwing.getRandom().nextInt(INTEREST_INTERVAL) != 0) {
			return false;
		}
		this.subject = this.loftwing.level().getNearestPlayer(this.loftwing, SIGHT_RANGE);
		return this.subject != null && this.loftwing.isCuriousAbout(this.subject);
	}

	@Override
	public boolean canContinueToUse() {
		return this.subject != null
				&& this.subject.isAlive()
				&& this.ticksLeft > 0
				&& this.loftwing.isFreeToRoam()
				&& this.loftwing.isCuriousAbout(this.subject)
				&& this.loftwing.distanceTo(this.subject) < SIGHT_RANGE * 2.0;
	}

	@Override
	public void start() {
		this.ticksLeft = this.loftwing.getRandom().nextIntBetweenInclusive(MIN_TICKS, MAX_TICKS);
		this.angle = this.loftwing.getRandom().nextFloat() * (float) (Math.PI * 2.0);
	}

	@Override
	public void stop() {
		this.subject = null;
		this.ticksLeft = 0;
		this.loftwing.getNavigation().stop();
	}

	@Override
	public void tick() {
		if (this.subject == null) {
			return;
		}
		this.ticksLeft--;
		this.loftwing.getLookControl().setLookAt(this.subject, 30.0F, 30.0F);
		this.angle += STEP;
		this.loftwing.getMoveControl().setWantedPosition(
				this.subject.getX() + Math.cos(this.angle) * ORBIT_RADIUS,
				this.subject.getY() + ORBIT_HEIGHT,
				this.subject.getZ() + Math.sin(this.angle) * ORBIT_RADIUS,
				SPEED
		);
	}
}
