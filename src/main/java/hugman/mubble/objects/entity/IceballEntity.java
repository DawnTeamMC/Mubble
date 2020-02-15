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
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class IceballEntity extends ProjectileItemEntity
{
	private int reboundingAmount = 3;
	
	public IceballEntity(EntityType<? extends ProjectileItemEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public IceballEntity(World world, LivingEntity owner)
	{
		super(MubbleEntities.ICEBALL, owner, world);
	}
	
	public IceballEntity(World world, double x, double y, double z)
	{
		super(MubbleEntities.ICEBALL, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem()
	{
		return MubbleItems.ICEBALL;
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putInt("ReboundingAmount", reboundingAmount);
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		this.reboundingAmount = compound.getInt("ReboundingAmount");
	}

	@Override
	protected void onImpact(RayTraceResult result)
	{
		boolean removeOnImpact = true;
		
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			removeOnImpact = onEntityImpact((EntityRayTraceResult)result);
		}
		else if(result.getType() == RayTraceResult.Type.BLOCK)
		{
			removeOnImpact = onBlockImpact(((BlockRayTraceResult)result));
		}
		
		boolean cantRebound = reboundingAmount <= 0;
		
		if(removeOnImpact || cantRebound)
		{
			if(!world.isRemote)
			{
				world.setEntityState(this, (byte)3);
				remove();
			}
			if(!removeOnImpact && cantRebound)
			{
				world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			}
		}
		else
		{
			reboundingAmount--;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte state)
	{
		if(state == 3)
		{
			for(int i = 0; i < 8; ++i)
			{
				float s1 = rand.nextFloat() * 0.2F - 0.1F;
				float s2 = rand.nextFloat() * 0.2F - 0.1F;
				float s3 = rand.nextFloat() * 0.2F - 0.1F;
				world.addParticle(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), s1, s2, s3);
			}
		}
	}
	
	private boolean onEntityImpact(EntityRayTraceResult result)
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
            if(!(entity instanceof SnowGolemEntity) || entity instanceof LivingEntity)
            {
            	LivingEntity livingEntity = (LivingEntity)entity;
            	livingEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 40, 1));
            	livingEntity.addPotionEffect(new EffectInstance(MubbleEffects.HEAVINESS, 40));
            }
        }
		world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}
	
	private boolean onBlockImpact(BlockRayTraceResult result)
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

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
