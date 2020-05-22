package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEffects;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IceballEntity extends ThrownItemEntity
{
	private int reboundingAmount = 3;
	
	public IceballEntity(EntityType<? extends ThrownItemEntity> entityType, World world)
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
	public void writeCustomDataToTag(CompoundTag compound)
	{
		super.writeCustomDataToTag(compound);
		compound.putInt("ReboundingAmount", reboundingAmount);
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag compound)
	{
		super.readCustomDataFromTag(compound);
		this.reboundingAmount = compound.getInt("ReboundingAmount");
	}

	@Override
	protected void onCollision(HitResult result)
	{
		boolean removeOnImpact = true;
		
		if(result.getType() == HitResult.Type.ENTITY)
		{
			removeOnImpact = onEntityImpact((EntityHitResult) result);
		}
		else if(result.getType() == HitResult.Type.BLOCK)
		{
			removeOnImpact = onBlockImpact(((BlockHitResult) result));
		}
		
		boolean cantRebound = reboundingAmount <= 0;
		
		if(removeOnImpact || cantRebound)
		{
			if(!this.world.isClient)
			{
				this.world.sendEntityStatus(this, (byte)3);
				this.remove();
			}
			if(!removeOnImpact && cantRebound)
			{
				this.world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			}
		}
		else
		{
			reboundingAmount--;
		}
	}

	@Environment(EnvType.CLIENT)
	public void handleStatus(byte state)
	{
		if(state == 3)
		{
			for(int i = 0; i < 8; ++i)
			{
				float s1 = random.nextFloat() * 0.2F - 0.1F;
				float s2 = random.nextFloat() * 0.2F - 0.1F;
				float s3 = random.nextFloat() * 0.2F - 0.1F;
				world.addParticle(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), s1, s2, s3);
			}
		}
	}
	
	private boolean onEntityImpact(EntityHitResult result)
	{
		Entity entity = result.getEntity();
		float damage = entity instanceof SnowGolemEntity ? 1.0F : 3.0F;
        boolean flag = entity.damage(DamageSource.thrownProjectile(this, this.owner), damage);
        if(flag)
        {
            this.dealDamage(this.owner, entity);
        }
        if(!world.isClient)
        {
            if(!(entity instanceof SnowGolemEntity) && entity instanceof LivingEntity)
            {
            	LivingEntity livingEntity = (LivingEntity) entity;
            	livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1));
            	livingEntity.addStatusEffect(new StatusEffectInstance(MubbleEffects.HEAVINESS, 40));
            }
        }
		world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}
	
	private boolean onBlockImpact(BlockHitResult result)
	{
		BlockPos pos = result.getBlockPos();
		BlockState state = world.getBlockState(pos);
		Direction face = result.getSide();
		Block resultBlock = null;
		
		if(state.getBlock().matches(MubbleTags.Blocks.FREEZABLE_TO_PACKED_ICE))
		{
			resultBlock = Blocks.PACKED_ICE;
		}
		
		if(resultBlock != null)
		{
			if(!world.isClient)
			{
				if(resultBlock instanceof AirBlock)
				{
					world.removeBlock(pos, false);
				}
				else
				{
					world.setBlockState(pos, resultBlock.getDefaultState());
					world.updateNeighbor(pos, resultBlock, pos);
				}
			}
			world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            return true;
		}
		if(face == Direction.UP)
		{
			Vec3d motion = this.getVelocity().subtract(0.0D, this.getVelocity().y * 1.25D, 0.0D);
			double minY = 0.3D;
			if(motion.y < minY)
			{
				motion = new Vec3d(motion.x, minY, motion.z);
			}
			this.setVelocity(motion);
			return false;
		}
		else
		{
			world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		Entity entity = this.getOwner();
		return new EntitySpawnS2CPacket(this, entity == null ? 0 : entity.getEntityId());
	}
}
