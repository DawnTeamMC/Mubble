package fr.hugman.mubble.item.weapon;

import fr.hugman.mubble.component.MubbleDataComponentsTypes;
import fr.hugman.mubble.entity.projectile.ShooterInkBulletEntity;
import fr.hugman.mubble.sound.MubbleSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SplatoonWeaponItem extends Item {
	public SplatoonWeaponItem(Settings settings) {
		super(settings);
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
		RegistryEntry<SplatoonWeapon> weaponEntry = stack.get(MubbleDataComponentsTypes.SPLATOON_WEAPON);
		if(weaponEntry == null) {
			return;
		}
		var weapon = weaponEntry.value();

		if(user instanceof PlayerEntity player) {
			if(weapon instanceof AutomaticShooterConfig config) {
				if (!player.getItemCooldownManager().isCoolingDown(this)) {
					this.shootShooterInkBullet(world, player, config);
					super.usageTick(world, user, stack, remainingUseTicks);
				}
			}
		}
	}

	private void shootShooterInkBullet(World world, PlayerEntity player, AutomaticShooterConfig config) {
		world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.SPLATTERSHOT_SHOOT, SoundCategory.PLAYERS, 0.5f, 1.0F);
		player.getItemCooldownManager().set(this, (int) config.cooldown());
		if (!world.isClient) {
			float angleDeviation = (player.isOnGround() ? config.angleDeviation() : config.jumpingAngleDeviation());
			var bullet = new ShooterInkBulletEntity(world, player, config.bulletConfig(), angleDeviation);
			world.spawnEntity(bullet);
		}
		player.incrementStat(Stats.USED.getOrCreateStat(this));
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 72000;
	}
}
