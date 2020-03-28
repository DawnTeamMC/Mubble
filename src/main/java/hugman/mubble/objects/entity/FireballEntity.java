package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireballEntity extends BallEntity
{
	public FireballEntity(EntityType<? extends BallEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public FireballEntity(World world, LivingEntity owner)
	{
		super(MubbleEntities.FIREBALL, world, owner);
	}
	
	public FireballEntity(World world, double x, double y, double z)
	{
		super(MubbleEntities.FIREBALL, world, x, y, z);
	}

	@Override
	protected Item getDefaultItem()
	{
		return MubbleItems.FIREBALL;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK;
	}

	@Override
	protected IParticleData getDeathParticle()
	{
		return ParticleTypes.FLAME;
	}
	
	@Override
	protected boolean onEntityImpact(EntityRayTraceResult result)
	{
		Entity entity = ((EntityRayTraceResult)result).getEntity();
		float damage = entity.isImmuneToFire() ? 1.0F : 3.0F;
        boolean flag = entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.owner).setFireDamage(), damage);
        if(flag)
        {
        	this.applyEnchantments(this.owner, entity);
        }
        if(!entity.isImmuneToFire())
        {
            entity.setFire(5);
        }
		world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}
	
	@Override
	protected boolean onBlockImpact(BlockRayTraceResult result)
	{
		BlockPos pos = result.getPos();
		BlockState state = world.getBlockState(pos);
		Direction face = result.getFace();
		FireBlock fire = (FireBlock)Blocks.FIRE;
		Block resultBlock = null;
		
		if(state.getBlock().isIn(MubbleTags.Blocks.MELTABLE_TO_AIR))
		{
			resultBlock = Blocks.AIR;
		}
		else if(state.getBlock().isIn(MubbleTags.Blocks.MELTABLE_TO_ICE))
		{
			resultBlock = Blocks.ICE;
		}
		else if(state.getBlock().isIn(MubbleTags.Blocks.MELTABLE_TO_WATER))
		{
			resultBlock = Blocks.WATER;
		}
		
		if(resultBlock != null)
		{
			if(!world.isRemote)
			{
				if(world.dimension.doesWaterVaporize() || resultBlock instanceof AirBlock)
				{
					world.removeBlock(pos, false);
				}
				else
				{
					world.setBlockState(pos, resultBlock.getDefaultState());
					world.neighborChanged(pos, resultBlock, pos);
				}
			}
			world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_MELTABLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            return true;
		}
		if(fire.canCatchFire(world, pos, face))
		{
			BlockPos firePos = pos.offset(face);
            if(this.world.isAirBlock(firePos) && !world.isRemote)
            {
               this.world.setBlockState(firePos, fire.getStateForPlacement(world, firePos));
            }
			world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            return true;
		}
		if(face == Direction.UP)
		{
			Vec3d motion = getMotion().subtract(0.0D, getMotion().y * 1.25D, 0.0D);
			double minY = 0.3D;
			if(motion.y < minY)
			{
				motion = new Vec3d(motion.x, minY, motion.z);
			}
			setMotion(motion);
			return false;
		}
		else
		{
			world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
	}
}
