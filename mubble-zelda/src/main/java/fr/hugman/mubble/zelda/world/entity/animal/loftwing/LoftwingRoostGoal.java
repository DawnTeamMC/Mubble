package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Come nightfall the bird leaves the sky, picks the highest open perch it can find nearby and sleeps
 * there until morning. Being hurt or crowded wakes it: it would rather be in the air than cornered.
 */
public class LoftwingRoostGoal extends Goal {
	private static final double SPEED = 1.0;
	/** How far it will travel to find a perch. */
	private static final int SEARCH_RADIUS = 24;
	/** How many spots it compares before settling for the highest. */
	private static final int SEARCH_SAMPLES = 10;
	/** How close to the perch counts as landed. */
	private static final double PERCH_REACHED = 2.0;
	/** Anyone closer than this wakes the bird. */
	private static final double WAKING_DISTANCE = 4.0;
	/** Giving up on an unreachable perch rather than hovering over it all night. */
	private static final int TRAVEL_TIMEOUT = 600;

	private final Loftwing loftwing;

	@Nullable
	private BlockPos perch;
	private int travelTicks;

	public LoftwingRoostGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		if (this.loftwing.level().isBrightOutside() || !this.loftwing.isFreeToRoam()) {
			return false;
		}
		this.perch = this.findPerch();
		return this.perch != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.perch != null
				&& !this.loftwing.level().isBrightOutside()
				&& this.loftwing.isFreeToRoam()
				&& this.loftwing.getLastHurtByMob() == null
				&& !this.isCrowded();
	}

	@Override
	public void start() {
		this.travelTicks = 0;
		this.flyToPerch();
	}

	@Override
	public void stop() {
		this.loftwing.setRoosting(false);
		this.loftwing.getNavigation().stop();
		this.perch = null;
	}

	@Override
	public void tick() {
		if (this.perch == null) {
			return;
		}
		if (this.loftwing.isRoosting()) {
			this.loftwing.getNavigation().stop();
			this.loftwing.setDeltaMovement(this.loftwing.getDeltaMovement().multiply(0.0, 1.0, 0.0));
			return;
		}
		if (this.loftwing.blockPosition().distSqr(this.perch) <= PERCH_REACHED * PERCH_REACHED && this.loftwing.onGround()) {
			this.loftwing.setRoosting(true);
			return;
		}
		if (++this.travelTicks > TRAVEL_TIMEOUT) {
			this.perch = null;
			return;
		}
		if (!this.loftwing.getMoveControl().hasWanted()) {
			this.flyToPerch();
		}
	}

	private void flyToPerch() {
		if (this.perch != null) {
			this.loftwing.getMoveControl().setWantedPosition(this.perch.getX() + 0.5, this.perch.getY(), this.perch.getZ() + 0.5, SPEED);
		}
	}

	private boolean isCrowded() {
		Player player = this.loftwing.level().getNearestPlayer(this.loftwing, WAKING_DISTANCE);
		return player != null && !this.loftwing.isOwnedBy(player);
	}

	/**
	 * The highest open spot within reach. Going through the heightmap means the perch always has
	 * the sky above it, which is the only thing a loftwing asks of a place to sleep.
	 */
	@Nullable
	private BlockPos findPerch() {
		Level level = this.loftwing.level();
		var random = this.loftwing.getRandom();
		BlockPos origin = this.loftwing.blockPosition();
		BlockPos best = null;
		for (int sample = 0; sample < SEARCH_SAMPLES; sample++) {
			int x = origin.getX() + random.nextIntBetweenInclusive(-SEARCH_RADIUS, SEARCH_RADIUS);
			int z = origin.getZ() + random.nextIntBetweenInclusive(-SEARCH_RADIUS, SEARCH_RADIUS);
			BlockPos candidate = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
			if (!level.getFluidState(candidate.below()).isEmpty() || level.getBlockState(candidate.below()).isAir()) {
				continue;
			}
			if (best == null || candidate.getY() > best.getY()) {
				best = candidate;
			}
		}
		return best;
	}
}
