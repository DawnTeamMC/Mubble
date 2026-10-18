package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.world.level.storage.loot.ZeldaBuiltInLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * A loftwing feeds itself: now and then it leaves the clouds, drops onto open water, comes up with
 * whatever it caught held in its beak, and swallows it a few seconds later. A hurt bird fishes far
 * more readily, since that is how it heals on its own.
 */
public class LoftwingFishGoal extends Goal {
	private static final double SPEED = 1.1;
	/** How far it will go looking for water. */
	private static final int SEARCH_RADIUS = 32;
	private static final int SEARCH_HEIGHT = 48;
	/** Roughly once every two and a half minutes when fed, and ten times as often when hurt. */
	private static final int FISHING_INTERVAL = 3000;
	private static final int HURT_FISHING_INTERVAL = 300;
	/** Giving up on water it cannot reach. */
	private static final int TRAVEL_TIMEOUT = 600;
	/** How long the catch is carried before it goes down. */
	private static final int EATING_TICKS = 60;
	private static final float HEAL_AMOUNT = 2.0F;

	private final Loftwing loftwing;

	@Nullable
	private BlockPos water;
	private int travelTicks;
	private int eatingTicks;

	public LoftwingFishGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (!this.loftwing.isFreeToRoam() || this.loftwing.isRoosting() || !this.loftwing.level().isBrightOutside()) {
			return false;
		}
		if (!this.loftwing.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
			return false;
		}
		boolean hurt = this.loftwing.getHealth() < this.loftwing.getMaxHealth();
		if (this.loftwing.getRandom().nextInt(hurt ? HURT_FISHING_INTERVAL : FISHING_INTERVAL) != 0) {
			return false;
		}
		this.water = this.findWater();
		return this.water != null;
	}

	@Override
	public boolean canContinueToUse() {
		return (this.water != null || this.eatingTicks > 0) && this.loftwing.isFreeToRoam();
	}

	@Override
	public void start() {
		this.travelTicks = 0;
		this.eatingTicks = 0;
		this.flyToWater();
	}

	@Override
	public void stop() {
		this.water = null;
		this.eatingTicks = 0;
		this.loftwing.getNavigation().stop();
	}

	@Override
	public void tick() {
		if (this.eatingTicks > 0) {
			if (--this.eatingTicks == 0) {
				this.swallowCatch();
			}
			return;
		}
		if (this.water == null) {
			return;
		}
		if (this.loftwing.isInWater()) {
			this.catchFish();
			return;
		}
		if (++this.travelTicks > TRAVEL_TIMEOUT) {
			this.water = null;
			return;
		}
		if (!this.loftwing.getMoveControl().hasWanted()) {
			this.flyToWater();
		}
	}

	private void flyToWater() {
		if (this.water != null) {
			this.loftwing.getMoveControl().setWantedPosition(this.water.getX() + 0.5, this.water.getY(), this.water.getZ() + 0.5, SPEED);
		}
	}

	/** Rolls the catch from a loot table, so a data pack decides what these waters hold. */
	private void catchFish() {
		this.water = null;
		this.loftwing.playSound(SoundEvents.GENERIC_SPLASH, 1.0F, 1.0F);
		if (this.loftwing.level() instanceof ServerLevel level) {
			LootParams params = new LootParams.Builder(level)
					.withParameter(LootContextParams.THIS_ENTITY, this.loftwing)
					.withParameter(LootContextParams.ORIGIN, this.loftwing.position())
					.create(LootContextParamSets.GIFT);
			var items = level.getServer().reloadableRegistries().getLootTable(ZeldaBuiltInLootTables.LOFTWING_FISHING).getRandomItems(params);
			if (!items.isEmpty()) {
				this.loftwing.setItemInHand(InteractionHand.MAIN_HAND, items.getFirst());
				this.eatingTicks = EATING_TICKS;
			}
		}
		// Back up to the sky either way, with or without a fish.
		this.loftwing.getMoveControl().setWantedPosition(
				this.loftwing.getX(),
				LoftwingSky.cloudLevel(this.loftwing.level(), this.loftwing.blockPosition()),
				this.loftwing.getZ(),
				SPEED
		);
	}

	private void swallowCatch() {
		ItemStack catchStack = this.loftwing.getItemInHand(InteractionHand.MAIN_HAND);
		if (catchStack.isEmpty()) {
			return;
		}
		this.loftwing.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		this.loftwing.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
		this.loftwing.heal(HEAL_AMOUNT);
	}

	/** Open water: a water block the bird can drop straight into. */
	@Nullable
	private BlockPos findWater() {
		var level = this.loftwing.level();
		return BlockPos.findClosestMatch(this.loftwing.blockPosition(), SEARCH_RADIUS, SEARCH_HEIGHT, pos ->
				level.getFluidState(pos).is(FluidTags.WATER) && level.getBlockState(pos.above()).isAir()
		).orElse(null);
	}
}
