package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
	protected ParticleEffect getDeathParticle()
	{
		return ParticleTypes.FLAME;
	}

	@Override
	protected boolean onEntityImpact(EntityHitResult result)
	{
		Entity entity = result.getEntity();
		float damage = entity.isFireImmune() ? 1.0F : 3.0F;
		boolean flag = entity.damage(DamageSource.thrownProjectile(this, this.getOwner()).setExplosive(), damage);
		if (flag)
		{
			this.dealDamage((LivingEntity) this.getOwner(), entity);
		}
		if (!entity.isFireImmune())
		{
			entity.setOnFireFor(5);
		}
		world.playSound(null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}

	@Override
	protected boolean onBlockImpact(BlockHitResult result)
	{
		BlockPos pos = result.getBlockPos();
		BlockState state = this.world.getBlockState(pos);
		Direction face = result.getSide();
		//AbstractFireBlock fire = (AbstractFireBlock) Blocks.FIRE;
		Block resultBlock = null;
		if (state.getBlock().isIn(MubbleTags.Blocks.MELTABLE_TO_AIR))
		{
			resultBlock = Blocks.AIR;
		}
		else if (state.getBlock().isIn(MubbleTags.Blocks.MELTABLE_TO_ICE))
		{
			resultBlock = Blocks.ICE;
		}
		else if (state.getBlock().isIn(MubbleTags.Blocks.MELTABLE_TO_WATER))
		{
			resultBlock = Blocks.WATER;
		}
		if (resultBlock != null)
		{
			if (!world.isClient)
			{
				if (world.getDimension().isUltrawarm() || resultBlock instanceof AirBlock)
				{
					world.removeBlock(pos, false);
				}
				else
				{
					world.setBlockState(pos, resultBlock.getDefaultState());
					world.updateNeighbor(pos, resultBlock, pos);
				}
			}
			world.playSound(null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_MELTABLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
		if (state.method_27851(BlockTags.CAMPFIRES, (abstractBlockState) ->
		{
			return abstractBlockState.contains(CampfireBlock.LIT) && abstractBlockState.contains(CampfireBlock.WATERLOGGED);
		}))
		{
			if (!state.get(CampfireBlock.LIT) && !state.get(CampfireBlock.WATERLOGGED))
			{
				if (!world.isClient)
				{
					world.setBlockState(pos, state.with(CampfireBlock.LIT, true));
				}
				world.playSound(null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
				return true;
			}
		}
		/* TODO
		if(fire.isFlammable(state))
		{
			BlockPos firePos = pos.offset(face);
            if(this.world.isAir(firePos) && !world.isClient)
            {
               this.world.setBlockState(firePos, AbstractFireBlock.getState(world, pos));
            }
			world.playSound((PlayerEntity) null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            return true;
		}
		*/
		if (face == Direction.UP)
		{
			Vec3d motion = this.getVelocity().subtract(0.0D, this.getVelocity().y * 1.25D, 0.0D);
			double minY = 0.3D;
			if (motion.y < minY)
			{
				motion = new Vec3d(motion.x, minY, motion.z);
			}
			this.setVelocity(motion);
			return false;
		}
		else
		{
			world.playSound(null, getX(), getY(), getZ(), MubbleSounds.ENTITY_FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
	}
}
