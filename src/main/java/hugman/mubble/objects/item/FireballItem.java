package hugman.mubble.objects.item;

import hugman.mubble.objects.entity.FireballEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.Projectile;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
		
	    DispenserBlock.registerBehavior(Items.SNOWBALL, new ProjectileDispenserBehavior()
	    {
	    	@Override
	    	protected Projectile createProjectile(World world, Position pos, ItemStack stack)
	    	{
	    		return Util.make(new SnowballEntity(world, pos.getX(), pos.getY(), pos.getZ()), (entity) ->
	    		{
	    			entity.setItem(stack);
	    		});
	    	}
	    });
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);
		world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
		if(!world.isClient)
		{
			FireballEntity entity = new FireballEntity(world, player);
			entity.setItem(stack);
			entity.setProperties(player, player.pitch, player.yaw, 0.0F, 1.5F, 1.0F);
			world.spawnEntity(entity);
		}
		
		player.incrementStat(Stats.USED.getOrCreateStat(this));
		if(!player.abilities.creativeMode)
		{
			stack.decrement(1);
		}
		
		return TypedActionResult.success(stack);
	}
}