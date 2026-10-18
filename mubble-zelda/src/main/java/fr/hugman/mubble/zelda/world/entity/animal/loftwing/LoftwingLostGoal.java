package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * A loftwing never blinks across the world to its owner. When it cannot find them it hangs where it
 * is, beating its wings, and calls out until someone comes looking.
 */
public class LoftwingLostGoal extends Goal {
	/** Past this, the bird considers its owner gone. */
	private static final double LOST_DISTANCE = 48.0;
	/** Roughly once every ten seconds it starts calling again. */
	private static final int CALLING_INTERVAL = 200;
	private static final int CALL_TICKS = 100;
	private static final int TICKS_BETWEEN_CRIES = 25;

	private final Loftwing loftwing;
	private int ticksLeft;

	public LoftwingLostGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.isOwnerLost() && this.loftwing.getRandom().nextInt(CALLING_INTERVAL) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return this.ticksLeft > 0 && this.isOwnerLost();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.ticksLeft = CALL_TICKS;
		this.loftwing.getNavigation().stop();
	}

	@Override
	public void stop() {
		this.ticksLeft = 0;
	}

	@Override
	public void tick() {
		this.ticksLeft--;
		// Hold station and flap, rather than drift off while calling.
		this.loftwing.getMoveControl().setWantedPosition(this.loftwing.getX(), this.loftwing.getY(), this.loftwing.getZ(), 1.0);
		if (this.ticksLeft % TICKS_BETWEEN_CRIES == 0) {
			this.loftwing.playSound(ZeldaSounds.LOFTWING_CALL, 2.0F, 1.0F);
		}
	}

	/** Only a bird with an owner can miss one, and only when it is free to go looking. */
	private boolean isOwnerLost() {
		if (!this.loftwing.isTame() || !this.loftwing.isFreeToRoam() || this.loftwing.isRoosting()) {
			return false;
		}
		LivingEntity owner = this.loftwing.getOwner();
		return owner == null || owner.level() != this.loftwing.level() || this.loftwing.distanceTo(owner) > LOST_DISTANCE;
	}
}
