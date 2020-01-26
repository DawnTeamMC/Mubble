package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FireballEntity extends ProjectileItemEntity
{
	public FireballEntity(EntityType<? extends ProjectileItemEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public FireballEntity(World world, LivingEntity owner)
	{
		super(MubbleEntities.FIREBALL, owner, world);
	}
	
	public FireballEntity(World world, double x, double y, double z)
	{
		super(MubbleEntities.FIREBALL, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return MubbleItems.FIREBALL;
	}

	@Override
	protected void onImpact(RayTraceResult result)
	{
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			Entity entity = ((EntityRayTraceResult)result).getEntity();
			int i = entity instanceof BlazeEntity ? 0 : 3;
			entity.setFire(5);
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)i);
		}
		
		if(result.getType() == RayTraceResult.Type.BLOCK)
		{
			onBlockImpact(((BlockRayTraceResult)result).getPos());
		}
		
		if(!world.isRemote)
		{
			world.setEntityState(this, (byte)3);
			remove();
		}
	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	private void onBlockImpact(BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		if(state.getBlock().isIn(BlockTags.ICE))
		{
			if(world.dimension.doesWaterVaporize())
			{
				world.removeBlock(pos, false);
			}
			else
			{
				world.setBlockState(pos, Blocks.WATER.getDefaultState());
				world.neighborChanged(pos, Blocks.WATER, pos);
			}
		}
	}
}
