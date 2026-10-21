package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Answering the whistle: the bird drops everything and makes for its owner. If it finds them in
 * mid-air it comes in underneath and takes them on its back, which is how anyone survives stepping
 * off Skyloft.
 * <p>
 * An owner on their feet is kept waiting: the bird flies the whole way, and arriving is the end of
 * it. An owner in free fall cannot wait, so the bird is given
 * {@link LoftwingCall#RESCUE_TICKS a moment} to reach them by wing and then crosses whatever is
 * left in one beat.
 */
public class LoftwingAnswerCallGoal extends Goal {
	private static final double SPEED = 1.4;
	/** How close it has to be before it can pick the owner up. */
	private static final double CATCH_DISTANCE = 4.0;

	private final Loftwing loftwing;
	/** How long the owner has been falling with this bird on its way, which is what it is racing. */
	private int divingTicks;

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
		this.divingTicks = 0;
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

		boolean rescue = this.loftwing.canCatch(caller);
		Vec3 target = rescue ? caller.position().subtract(0.0, LoftwingCall.RESCUE_DROP, 0.0) : caller.position();
		this.loftwing.getMoveControl().setWantedPosition(target.x(), target.y(), target.z(), SPEED);

		if (rescue) {
			if (++this.divingTicks >= LoftwingCall.RESCUE_TICKS || this.loftwing.distanceTo(caller) <= CATCH_DISTANCE) {
				this.loftwing.catchFalling(caller);
			}
			return;
		}
		// Nobody to catch yet, so the clock stops and reaching them is all this was.
		this.divingTicks = 0;
		if (this.hasArrived(caller)) {
			this.loftwing.clearCall();
		}
	}

	/**
	 * Whether the bird can call the errand done and go back to its own business.
	 * <p>
	 * Feet off the ground means not yet, however close it is: a fall two ticks old has not dropped
	 * far enough to be worth catching, and a bird that stood down in that gap would be a bird that
	 * was there right up until it mattered.
	 */
	private boolean hasArrived(Player caller) {
		return this.loftwing.distanceTo(caller) <= CATCH_DISTANCE && (caller.onGround() || caller.isPassenger());
	}
}
