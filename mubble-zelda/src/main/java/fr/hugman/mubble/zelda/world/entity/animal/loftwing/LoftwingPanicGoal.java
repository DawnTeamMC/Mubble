package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import net.minecraft.world.entity.ai.goal.PanicGoal;

/**
 * Vanilla panic, plus the flag that makes the bird throw its wings open: a startled loftwing is as
 * wide as a flying one, which is what the player sees and bumps into.
 */
public class LoftwingPanicGoal extends PanicGoal {
	private final Loftwing loftwing;

	public LoftwingPanicGoal(Loftwing loftwing, double speedModifier) {
		super(loftwing, speedModifier);
		this.loftwing = loftwing;
	}

	@Override
	public void start() {
		super.start();
		this.loftwing.setPanicking(true);
	}

	@Override
	public void stop() {
		super.stop();
		this.loftwing.setPanicking(false);
	}
}
