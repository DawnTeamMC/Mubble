package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import hugman.mubble.objects.entity.FireballEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class FireballItem extends Item
{
	public FireballItem(Settings builder)
	{
		super(builder);
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);
		world.playSound(null, player.getX(), player.getY(), player.getZ(), MubbleSounds.ENTITY_FIREBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		if (!world.isClient)
		{
			FireballEntity entity = new FireballEntity(world, player);
			entity.setItem(stack);
			entity.setProperties(player, player.pitch, player.yaw, 0.0F, 1.5F, 1.0F);
			world.spawnEntity(entity);
		}
		player.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!player.abilities.creativeMode)
		{
			stack.decrement(1);
		}
		return TypedActionResult.success(stack);
	}

	public static final ItemDispenserBehavior DISPENSER_BEHAVIOR = new ProjectileDispenserBehavior()
	{
		@Override
		protected ProjectileEntity createProjectile(World world, Position pos, ItemStack stack)
		{
			return Util.make(new FireballEntity(world, pos.getX(), pos.getY(), pos.getZ()), (entity) ->
			{
				entity.setItem(stack);
			});
		}

	};
}