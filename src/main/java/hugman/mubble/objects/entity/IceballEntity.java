package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IceballEntity extends BallEntity
{	
	public IceballEntity(EntityType<? extends BallEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public IceballEntity(World world, LivingEntity owner)
	{
		super(MubbleEntities.ICEBALL, world, owner);
	}
	
	public IceballEntity(World world, double x, double y, double z)
	{
		super(MubbleEntities.ICEBALL, world, x, y, z);
	}

	@Override
	protected Item getDefaultItem()
	{
		return MubbleItems.ICEBALL;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return MubbleSounds.ENTITY_ICEBALL_HIT_BLOCK;
	}

	@Override
	protected IParticleData getDeathParticle()
	{
		return ParticleTypes.CLOUD;
	}
	
	@Override
	protected boolean onEntityImpact(EntityRayTraceResult result)
	{
		Entity entity = ((EntityRayTraceResult)result).getEntity();
		float damage = entity instanceof SnowGolemEntity ? 1.0F : 3.0F;
        boolean flag = entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.owner), damage);
        if(flag)
        {
            this.applyEnchantments(this.owner, entity);
        }
        if(!world.isRemote)
        {
            if(!(entity instanceof SnowGolemEntity) && entity instanceof LivingEntity)
            {
            	LivingEntity livingEntity = (LivingEntity)entity;
            	livingEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 40, 1));
            	livingEntity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 40));
            }
        }
		world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}
	
	@Override
	protected boolean onBlockImpact(BlockRayTraceResult result)
	{
		BlockPos pos = result.getPos();
		BlockState state = world.getBlockState(pos);
		Direction face = result.getFace();
		Block resultBlock = null;
		
		if(state.getBlock().isIn(MubbleTags.Blocks.FREEZABLE_TO_PACKED_ICE))
		{
			resultBlock = Blocks.PACKED_ICE;
		}
		
		if(resultBlock != null)
		{
			if(!world.isRemote)
			{
				if(resultBlock instanceof AirBlock)
				{
					world.removeBlock(pos, false);
				}
				else
				{
					world.setBlockState(pos, resultBlock.getDefaultState());
					world.neighborChanged(pos, resultBlock, pos);
				}
			}
			world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
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
			world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
	}
}
