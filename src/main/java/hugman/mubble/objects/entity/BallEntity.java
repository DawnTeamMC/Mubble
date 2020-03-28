package hugman.mubble.objects.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class BallEntity extends ProjectileItemEntity
{
	protected int reboundingAmount = 3;
	
	public BallEntity(EntityType<? extends BallEntity> entityType, World world)
	{
		super(entityType, world);
	}
	
	public BallEntity(EntityType<? extends BallEntity> entityType, World world, LivingEntity owner)
	{
		super(entityType, owner, world);
	}
	
	public BallEntity(EntityType<? extends BallEntity> entityType, World world, double x, double y, double z)
	{
		super(entityType, x, y, z, world);
	}
	
	protected abstract SoundEvent getDeathSound();
	
	protected abstract IParticleData getDeathParticle();

	protected abstract boolean onEntityImpact(EntityRayTraceResult result);
	
	protected abstract boolean onBlockImpact(BlockRayTraceResult result);
	
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
				world.playSound((PlayerEntity)null, getX(), getY(), getZ(), getDeathSound(), SoundCategory.NEUTRAL, 0.5F, 1.0F);
			}
		}
		else
		{
			reboundingAmount--;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte state)
	{
		if(state == 3)
		{
			this.spawnDeathParticles();
		}
	}
	
	protected void spawnDeathParticles()
	{
		for(int i = 0; i < 8; ++i)
		{
			float s1 = rand.nextFloat() * 0.2F - 0.1F;
			float s2 = rand.nextFloat() * 0.2F - 0.1F;
			float s3 = rand.nextFloat() * 0.2F - 0.1F;
			world.addParticle(getDeathParticle(), this.getX(), this.getY(), this.getZ(), s1, s2, s3);
		}
	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
