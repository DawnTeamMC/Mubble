package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * On the ground, the bird stops and runs its beak through its feathers, shaking the water out. It
 * does it most readily straight out of the water, which is where a fishing trip leaves it.
 */
public class LoftwingPreenGoal extends Goal {
	/** Roughly once a minute while dripping, once every ten otherwise. */
	private static final int WET_INTERVAL = 200;
	private static final int DRY_INTERVAL = 2000;
	private static final int MIN_TICKS = 60;
	private static final int MAX_TICKS = 100;
	private static final int DROPLETS_PER_TICK = 2;

	private final Loftwing loftwing;
	private int ticksLeft;

	public LoftwingPreenGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (!this.loftwing.onGround() || !this.loftwing.isFreeToRoam() || this.loftwing.isRoosting()) {
			return false;
		}
		return this.loftwing.getRandom().nextInt(this.loftwing.isDrying() ? WET_INTERVAL : DRY_INTERVAL) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return this.ticksLeft > 0 && this.loftwing.onGround() && this.loftwing.isFreeToRoam();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.ticksLeft = this.loftwing.getRandom().nextIntBetweenInclusive(MIN_TICKS, MAX_TICKS);
		this.loftwing.getNavigation().stop();
		this.loftwing.setPreening(true);
		this.loftwing.playSound(ZeldaSounds.LOFTWING_PREEN, 1.0F, 1.0F);
	}

	@Override
	public void stop() {
		this.loftwing.setPreening(false);
		this.ticksLeft = 0;
	}

	@Override
	public void tick() {
		this.ticksLeft--;
		if (this.loftwing.isDrying() && this.loftwing.level() instanceof ServerLevel level) {
			level.sendParticles(
					ParticleTypes.SPLASH,
					this.loftwing.getX(), this.loftwing.getY(0.7), this.loftwing.getZ(),
					DROPLETS_PER_TICK,
					this.loftwing.getBbWidth() / 3.0, this.loftwing.getBbHeight() / 3.0, this.loftwing.getBbWidth() / 3.0,
					0.0
			);
		}
	}
}
