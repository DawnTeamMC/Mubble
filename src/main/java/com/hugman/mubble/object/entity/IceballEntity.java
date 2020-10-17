package com.hugman.mubble.object.entity;

import com.hugman.dawn.mod.init.DawnEffectPack;
import com.hugman.mubble.init.MubbleEntityPack;
import com.hugman.mubble.init.MubbleItemPack;
import com.hugman.mubble.init.MubbleSoundPack;
import com.hugman.mubble.init.data.MubbleTags;
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
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IceballEntity extends BallEntity {
	public IceballEntity(EntityType<? extends BallEntity> entityType, World world) {
		super(entityType, world);
	}

	public IceballEntity(World world, LivingEntity owner) {
		super(MubbleEntityPack.ICEBALL, world, owner);
	}

	public IceballEntity(World world, double x, double y, double z) {
		super(MubbleEntityPack.ICEBALL, world, x, y, z);
	}

	@Override
	protected Item getDefaultItem() {
		return MubbleItemPack.ICEBALL;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MubbleSoundPack.ENTITY_ICEBALL_HIT_BLOCK;
	}

	@Override
	protected ParticleEffect getDeathParticle() {
		return ParticleTypes.CLOUD;
	}

	@Override
	protected boolean onEntityImpact(EntityHitResult result) {
		Entity entity = result.getEntity();
		float damage = entity instanceof SnowGolemEntity ? 1.0F : 3.0F;
		boolean flag = entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), damage);
		if(flag) {
			this.dealDamage((LivingEntity) this.getOwner(), entity);
		}
		if(!world.isClient) {
			if(!(entity instanceof SnowGolemEntity) && entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) entity;
				livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1));
				livingEntity.addStatusEffect(new StatusEffectInstance(DawnEffectPack.HEAVINESS, 40));
			}
		}
		world.playSound(null, getX(), getY(), getZ(), MubbleSoundPack.ENTITY_ICEBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		return true;
	}

	@Override
	protected boolean onBlockImpact(BlockHitResult result) {
		BlockPos pos = result.getBlockPos();
		BlockState state = world.getBlockState(pos);
		Direction face = result.getSide();
		Block resultBlock = null;
		if(state.getBlock().isIn(MubbleTags.Blocks.FREEZABLE_TO_PACKED_ICE)) {
			resultBlock = Blocks.PACKED_ICE;
		}
		if(resultBlock != null) {
			if(!world.isClient) {
				if(resultBlock instanceof AirBlock) {
					world.removeBlock(pos, false);
				}
				else {
					world.setBlockState(pos, resultBlock.getDefaultState());
					world.updateNeighbor(pos, resultBlock, pos);
				}
			}
			world.playSound(null, getX(), getY(), getZ(), MubbleSoundPack.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
		if(face == Direction.UP) {
			Vec3d motion = this.getVelocity().subtract(0.0D, this.getVelocity().y * 1.25D, 0.0D);
			double minY = 0.3D;
			if(motion.y < minY) {
				motion = new Vec3d(motion.x, minY, motion.z);
			}
			this.setVelocity(motion);
			return false;
		}
		else {
			world.playSound(null, getX(), getY(), getZ(), MubbleSoundPack.ENTITY_ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
			return true;
		}
	}
}
