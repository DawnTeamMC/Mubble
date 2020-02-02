package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleSounds;
import hugman.mubble.objects.entity.IceballEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class IceballItem extends Item
{
	public IceballItem(Properties builder)
	{
		super(builder);
	    DispenserBlock.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		world.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), MubbleSounds.ENTITY_ICEBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		if(!world.isRemote)
		{
			IceballEntity entity = new IceballEntity(world, player);
			entity.setItem(stack);
			entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			world.addEntity(entity);
		}
		
		player.addStat(Stats.ITEM_USED.get(this));
		if(!player.abilities.isCreativeMode)
		{
			stack.shrink(1);
		}
		
		return ActionResult.success(stack);
	}
	
	public static final IDispenseItemBehavior DISPENSER_BEHAVIOR = new ProjectileDispenseBehavior()
	{
		@Override
		protected IProjectile getProjectileEntity(World world, IPosition pos, ItemStack stack)
		{
			return Util.make(new IceballEntity(world, pos.getX(), pos.getY(), pos.getZ()), (entity) ->
    		{
    			entity.setItem(stack);
    		});
		};
	};
}