package fr.hugman.mubble.item.weapon;

import fr.hugman.mubble.item.weapon.stats.AutomaticShooterConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
				world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
				player.getItemCooldownManager().set(this, (int) this.config.cooldown());
				if(!world.isClient) {
					SnowballEntity bullet = new SnowballEntity(world, user);
					bullet.setItem(new ItemStack(Items.CAKE));
					this.setVelocity(user, bullet);
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

	//TODO: move this to the bullet's class
	public void setVelocity(Entity shooter, SnowballEntity bullet) {
		Random random = shooter.getEntityWorld().getRandom();

		float angleDeviation = (shooter.isOnGround() ? this.config.angleDeviation() : this.config.jumpingAngleDeviation());
		float pitch = shooter.getPitch() + (random.nextFloat() - random.nextFloat()) * angleDeviation;
		float yaw = shooter.getYaw() + (random.nextFloat() - random.nextFloat()) * angleDeviation;

		float speed = this.config.bulletConfig().initialSpeed();
		float x = -MathHelper.sin(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
		float y = -MathHelper.sin((pitch) * ((float) Math.PI / 180));
		float z = MathHelper.cos(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));

		bullet.setVelocity(x, y, z, speed, 0.0F);
	}
}
