package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KirbyBallEntity extends BallEntity
{
	public KirbyBallEntity(EntityType<? extends BallEntity> entityType, World world)
	{
		super(entityType, world);
		reboundingAmount = 10;
	}

	public KirbyBallEntity(World world, LivingEntity owner)
	{
		super(MubbleEntities.KIRBY_BALL, world, owner);
		reboundingAmount = 10;
	}

	public KirbyBallEntity(World world, double x, double y, double z)
	{
		super(MubbleEntities.KIRBY_BALL, world, x, y, z);
		reboundingAmount = 10;
	}

	@Override
	protected Item getDefaultItem()
	{
		return MubbleItems.KIRBY_BALL;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return MubbleSounds.ENTITY_KIRBY_BALL_HIT_BLOCK;
	}

	@Override
	protected ParticleEffect getDeathParticle()
	{
		return ParticleTypes.CLOUD;
	}

	@Override
	protected boolean onEntityImpact(EntityHitResult result)
	{
		Entity entity = result.getEntity();
		boolean flag = entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), 2.5F);
		if (flag)
		{
			this.dealDamage((LivingEntity) this.getOwner(), entity);
		}
		world.playSound(null, getX(), getY(), getZ(), MubbleSounds.ENTITY_KIRBY_BALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}

	@Override
	protected boolean onBlockImpact(BlockHitResult result)
	{
		Direction face = result.getSide();
		Vec3d motion = getVelocity();
		if (face == Direction.UP || face == Direction.DOWN)
		{
			motion = motion.subtract(0.0D, getVelocity().y * 1.25D, 0.0D);
			if (face == Direction.UP)
			{
				double minY = 0.3D;
				if (motion.y < minY)
				{
					motion = new Vec3d(motion.x, minY, motion.z);
				}
			}
		}
		else if (face == Direction.WEST || face == Direction.EAST)
		{
			motion = motion.subtract(getVelocity().x * 1.25D, 0.0D, 0.0D);
		}
		else if (face == Direction.NORTH || face == Direction.SOUTH)
		{
			motion = motion.subtract(0.0D, 0.0D, getVelocity().z * 1.25D);
		}
		setVelocity(motion);
		world.playSound(null, getX(), getY(), getZ(), MubbleSounds.ENTITY_KIRBY_BALL_REBOUND, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return false;
	}
}
