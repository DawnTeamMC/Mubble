package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class FireballEntity extends ProjectileItemEntity
{
	private int reboundingAmount = 3;
	
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
		
		if(removeOnImpact || reboundingAmount <= 0)
		{
			world.playSound((PlayerEntity)null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_DEATH, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			if(!world.isRemote)
			{
				world.setEntityState(this, (byte)3);
				remove();
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
				world.addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), s1, s2, s3);
			}
		}
	}
	
	private boolean onEntityImpact(EntityRayTraceResult result)
	{
		Entity entity = ((EntityRayTraceResult)result).getEntity();
		float damage = entity.isImmuneToFire() ? 1.0F : 3.0F;
        boolean flag = entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.owner), damage);
        if(!entity.isImmuneToFire())
        {
            int timer = entity.getFireTimer();
            entity.setFire(5);
            if (flag)
            {
               this.applyEnchantments(this.owner, entity);
            }
            else
            {
               entity.setFireTimer(timer);
            }
         }
		return true;
	}
	
	private boolean onBlockImpact(BlockRayTraceResult result)
	{
		BlockPos pos = result.getPos();
		BlockState state = world.getBlockState(pos);
		Direction face = result.getFace();
		FireBlock fire = (FireBlock)Blocks.FIRE;
		if(state.getBlock().isIn(MubbleTags.Blocks.MELTABLE_TO_WATER))
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
		if(fire.canCatchFire(world, pos, face))
		{
			BlockPos firePos = pos.offset(face);
            if(this.world.isAirBlock(firePos))
            {
               this.world.setBlockState(firePos, fire.getStateForPlacement(world, firePos));
            }
            return true;
		}
		if(result.getFace() == Direction.UP)
		{
			setMotion(getMotion().subtract(0, getMotion().y * 1.3, 0));
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
