package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

/**
 * A loftwing that turned you down and later sees you flying past on one of its own kind drops you a
 * chainmail helmet, the way a cat leaves something on the doorstep. It is the only way to get one.
 */
public class LoftwingGiftGoal extends Goal {
	private static final double SIGHT_RANGE = 16.0;
	/** Roughly once every ten minutes of being watched by a bird that refused you. */
	private static final int GIFT_CHANCE = 12000;
	private static final int DELIVERY_TICKS = 40;
	private static final double DROP_HEIGHT = 1.0;

	private final Loftwing loftwing;

	@Nullable
	private Player recipient;
	private int ticksLeft;

	public LoftwingGiftGoal(Loftwing loftwing) {
		this.loftwing = loftwing;
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.loftwing.isBaby() || this.loftwing.getRandom().nextInt(GIFT_CHANCE) != 0) {
			return false;
		}
		this.recipient = this.findSomeoneItRefused();
		return this.recipient != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.ticksLeft > 0 && this.recipient != null && this.recipient.isAlive();
	}

	@Override
	public void start() {
		this.ticksLeft = DELIVERY_TICKS;
	}

	@Override
	public void stop() {
		this.recipient = null;
		this.ticksLeft = 0;
	}

	@Override
	public void tick() {
		if (this.recipient == null) {
			return;
		}
		this.loftwing.getLookControl().setLookAt(this.recipient, 30.0F, 30.0F);
		if (--this.ticksLeft <= 0) {
			this.dropGift();
		}
	}

	private void dropGift() {
		Vec3 above = this.recipient.position().add(0.0, DROP_HEIGHT, 0.0);
		ItemEntity gift = new ItemEntity(this.loftwing.level(), above.x(), above.y(), above.z(), new ItemStack(ZeldaItems.CHAINMAIL_LOFTWING_HELMET));
		gift.setDefaultPickUpDelay();
		this.loftwing.level().addFreshEntity(gift);
		this.recipient = null;
	}

	/** Someone riding a loftwing of their own, who this bird once passed over. */
	@Nullable
	private Player findSomeoneItRefused() {
		List<Player> nearby = this.loftwing.level().getEntitiesOfClass(
				Player.class,
				this.loftwing.getBoundingBox().inflate(SIGHT_RANGE),
				player -> this.loftwing.hasRejected(player) && player.getVehicle() instanceof Loftwing
		);
		return nearby.isEmpty() ? null : nearby.getFirst();
	}
}
