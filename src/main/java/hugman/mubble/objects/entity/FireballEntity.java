package hugman.mubble.objects.entity;

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
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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

public class FireballEntity extends ThrownItemEntity
{
	private int reboundingAmount = 3;
	
	public FireballEntity(EntityType<? extends ThrownItemEntity> entityType, World world)
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
			removeOnImpact = onBlockImpact((BlockHitResult) result);
		}
		
		boolean cantRebound = reboundingAmount <= 0;
		
		if(removeOnImpact || cantRebound)
		{
			if(!this.world.isClient)
			{
				this.world.sendEntityStatus(this, (byte) 3);
				this.remove();
			}
			if(!removeOnImpact && cantRebound)
			{
				this.world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
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
				world.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), s1, s2, s3);
			}
		}
	}
	
	private boolean onEntityImpact(EntityHitResult result)
	{
		Entity entity = result.getEntity();
		float damage = entity.isFireImmune() ? 1.0F : 3.0F;
        boolean flag = entity.damage(DamageSource.thrownProjectile(this, this.owner).setFire(), damage);
        if(flag)
        {
           this.dealDamage(this.owner, entity);
        }
        if(!entity.isFireImmune())
        {
            entity.setOnFireFor(5);
        }
		world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}
	
	private boolean onBlockImpact(BlockHitResult result)
	{
		BlockPos pos = result.getBlockPos();
		BlockState state = this.world.getBlockState(pos);
		Direction face = result.getSide();
		FireBlock fire = (FireBlock) Blocks.FIRE;
		Block resultBlock = null;
		
		if(state.getBlock().matches(MubbleTags.Blocks.MELTABLE_TO_AIR))
		{
			resultBlock = Blocks.AIR;
		}
		else if(state.getBlock().matches(MubbleTags.Blocks.MELTABLE_TO_ICE))
		{
			resultBlock = Blocks.ICE;
		}
		else if(state.getBlock().matches(MubbleTags.Blocks.MELTABLE_TO_WATER))
		{
			resultBlock = Blocks.WATER;
		}
		
		if(resultBlock != null)
		{
			if(!world.isClient)
			{
				if(world.dimension.doesWaterVaporize() || resultBlock instanceof AirBlock)
				{
					world.removeBlock(pos, false);
				}
				else
				{
					world.setBlockState(pos, resultBlock.getDefaultState());
					world.updateNeighbor(pos, resultBlock, pos);
				}
			}
			world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_MELTABLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
		if(fire.isFlammable(state))
		{
			BlockPos firePos = pos.offset(face);
            if(this.world.isAir(firePos) && !world.isClient)
            {
               this.world.setBlockState(firePos, fire.getStateForPosition(world, firePos));
            }
			world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
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
			world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
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
