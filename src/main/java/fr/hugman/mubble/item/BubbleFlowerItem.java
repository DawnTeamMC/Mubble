package fr.hugman.mubble.item;

import fr.hugman.mubble.entity.BubbleEntity;
import fr.hugman.mubble.registry.MubbleSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * The Bubble Flower item, which transforms the holder into Bubble form.
 * <p>
 * In Bubble form the player has 3 charges. Using the item shoots a {@link BubbleEntity}.
 * One empty charge refills every 3 seconds (60 ticks). Charges and their refill timer
 * are stored directly in the item's NBT so the system works without extra data attachments.
 *
 * @author Copilot
 * @since v4.0.0
 */
public class BubbleFlowerItem extends Item {
	// NBT keys
	private static final String KEY_CHARGES = "Charges";
	private static final String KEY_CHARGE_TIMER = "ChargeTimer";

	public static final int MAX_CHARGES = 3;
	public static final int CHARGE_REFILL_TICKS = 60; // 3 seconds

	public BubbleFlowerItem(Item.Settings settings) {
		super(settings);
	}

	// =====================
	//   Charge helpers
	// =====================

	/** Returns the current number of charges stored in the item's NBT (default: MAX_CHARGES). */
	public static int getCharges(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null || !nbt.contains(KEY_CHARGES)) return MAX_CHARGES;
		return nbt.getInt(KEY_CHARGES);
	}

	private static void setCharges(ItemStack stack, int charges) {
		stack.getOrCreateNbt().putInt(KEY_CHARGES, Math.max(0, Math.min(MAX_CHARGES, charges)));
	}

	private static int getChargeTimer(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null || !nbt.contains(KEY_CHARGE_TIMER)) return 0;
		return nbt.getInt(KEY_CHARGE_TIMER);
	}

	private static void setChargeTimer(ItemStack stack, int timer) {
		stack.getOrCreateNbt().putInt(KEY_CHARGE_TIMER, timer);
	}

	// =====================
	//   Inventory tick
	// =====================

	/**
	 * Called every tick while the item is in any inventory slot.
	 * Handles charge refilling at a rate of 1 charge per {@link #CHARGE_REFILL_TICKS} ticks.
	 */
	@Override
	public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
		if (world.isClient) return;

		int charges = getCharges(stack);
		if (charges < MAX_CHARGES) {
			int timer = getChargeTimer(stack) + 1;
			if (timer >= CHARGE_REFILL_TICKS) {
				setCharges(stack, charges + 1);
				timer = 0;
			}
			setChargeTimer(stack, timer);
		}
	}

	// =====================
	//   Use (shoot bubble)
	// =====================

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		int charges = getCharges(stack);

		if (charges <= 0) {
			// No charges left – play a "click" sound or just pass
			return TypedActionResult.fail(stack);
		}

		if (!world.isClient) {
			// Compute shoot direction from player look, plus player's current motion
			Vec3d look = player.getRotationVec(1.0f);
			Vec3d velocity = look.multiply(1.5).add(player.getVelocity());

			BubbleEntity bubble = new BubbleEntity(world, player, velocity);
			world.spawnEntity(bubble);

			world.playSound(null, player.getX(), player.getY(), player.getZ(),
					MubbleSounds.BUBBLE_SHOOT, SoundCategory.PLAYERS, 0.6f, 1.0f);
		}

		setCharges(stack, charges - 1);
		// Reset refill timer when a charge is spent
		setChargeTimer(stack, 0);

		player.incrementStat(Stats.USED.getOrCreateStat(this));
		return TypedActionResult.success(stack);
	}

	// =====================
	//   Tooltip / name colour
	// =====================

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return getCharges(stack) < MAX_CHARGES;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.round(getCharges(stack) * 13.0f / MAX_CHARGES);
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		// Light blue to match the bubble theme
		return 0x55DDFF;
	}
}
