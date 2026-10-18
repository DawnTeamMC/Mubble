package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Answering the whistle: the bird drops everything and makes for its owner. If it finds them in
 * mid-air it comes in underneath and takes them on its back, which is how anyone survives stepping
 * off Skyloft.
 */
public class LoftwingAnswerCallGoal extends Goal {
	private static final double SPEED = 1.4;
	/** How close it has to be before it can pick the owner up. */
	private static final double CATCH_DISTANCE = 4.0;
	/** How far below the falling owner it slides in. */
	private static final double CATCH_DROP = 1.6;

	private final Loftwing loftwing;

	public LoftwingAnswerCallGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return this.loftwing.getCaller() != null;
	}

	@Override
	public boolean canContinueToUse() {
		Player caller = this.loftwing.getCaller();
		return caller != null && caller.isAlive() && !this.loftwing.isVehicle();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.loftwing.setRoosting(false);
		this.loftwing.setOrderedToSit(false);
		this.loftwing.getNavigation().stop();
	}

	@Override
	public void stop() {
		this.loftwing.clearCall();
	}

	@Override
	public void tick() {
		Player caller = this.loftwing.getCaller();
		if (caller == null) {
			return;
		}
		this.loftwing.getLookControl().setLookAt(caller, 30.0F, 30.0F);

		boolean falling = !caller.onGround() && caller.getVehicle() == null;
		Vec3 target = falling ? caller.position().subtract(0.0, CATCH_DROP, 0.0) : caller.position();
		this.loftwing.getMoveControl().setWantedPosition(target.x(), target.y(), target.z(), SPEED);

		if (this.loftwing.distanceTo(caller) > CATCH_DISTANCE) {
			return;
		}
		if (falling && this.loftwing.hasBelt()) {
			caller.startRiding(this.loftwing);
		}
		this.loftwing.clearCall();
	}
}
