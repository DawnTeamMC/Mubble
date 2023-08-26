package fr.hugman.mubble.item.weapon;

import fr.hugman.mubble.entity.projectile.ShooterInkBulletEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AutomaticShooterItem extends Item {
	private final AutomaticShooterConfig config;

	public AutomaticShooterItem(AutomaticShooterConfig config, Settings settings) {
		super(settings);
		this.config = config;
	}

	public AutomaticShooterItem(AutomaticShooterConfig config) {
		this(config, new Settings().maxCount(1));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(!canUse(world, user, stack)) {
			return TypedActionResult.fail(stack);
		}
		user.setCurrentHand(hand);
		return TypedActionResult.consume(stack);
	}

	public boolean canUse(World world, PlayerEntity user, ItemStack stack) {
		return true;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if(user instanceof PlayerEntity player) {
			if(!player.getItemCooldownManager().isCoolingDown(this)) {
				//world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
				//TODO: custom sound
				player.getItemCooldownManager().set(this, (int) this.config.cooldown());
				if(!world.isClient) {
					float angleDeviation = (user.isOnGround() ? this.config.angleDeviation() : this.config.jumpingAngleDeviation());
					var bullet = new ShooterInkBulletEntity(world, user, this.config.bulletConfig(), angleDeviation);
					world.spawnEntity(bullet);
				}
				player.incrementStat(Stats.USED.getOrCreateStat(this));
				super.usageTick(world, user, stack, remainingUseTicks);
			}
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}
}
