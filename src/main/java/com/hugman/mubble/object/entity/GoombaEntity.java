package com.hugman.mubble.object.entity;

import com.hugman.mubble.init.MubbleSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class GoombaEntity extends HostileEntity {
	private static final TrackedData<Integer> VARIANT = DataTracker.registerData(GoombaEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public GoombaEntity(EntityType<? extends GoombaEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static Builder createGoombaAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
	}

	public static boolean canSpawn(EntityType<GoombaEntity> entity, WorldAccess world, SpawnReason reason, BlockPos pos, Random rand) {
		return world.getDifficulty() != Difficulty.PEACEFUL;
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.add(2, new GoToWalkTargetGoal(this, 1.0D));
		this.goalSelector.add(3, new EscapeDangerGoal(this, 1.0D));
		this.goalSelector.add(4, new LookAtEntityGoal(this, BeeEntity.class, 10.0F));
		this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this, GoombaEntity.class));
		this.targetSelector.add(3, new FollowTargetGoal(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal(this, ToadEntity.class, true));
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return MubbleSounds.ENTITY_GOOMBA_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MubbleSounds.ENTITY_GOOMBA_DEATH;
	}

	protected SoundEvent getStepSound() {
		return MubbleSounds.ENTITY_GOOMBA_STEP;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	public int getVariant() {
		return this.dataTracker.get(VARIANT);
	}

	public void setVariant(int variantIn) {
		this.dataTracker.set(VARIANT, variantIn);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, 0);
	}

	@Override
	public void writeCustomDataToTag(CompoundTag compound) {
		super.writeCustomDataToTag(compound);
		compound.putInt("Variant", this.getVariant());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag compound) {
		super.readCustomDataFromTag(compound);
		this.setVariant(compound.getInt("Variant"));
	}

	@Override
	public void onPlayerCollision(PlayerEntity playerIn) {
		Box hitbox = this.getBoundingBox().shrink(0, -1, 0).expand(-0.4, 0, -0.4);
		Vec3d velocity = playerIn.getVelocity();
		if(!this.world.isClient() && !playerIn.isSpectator() && hitbox.intersects(playerIn.getBoundingBox()) && velocity.y < 0.3D && this.isAlive()) {
			playerIn.setVelocity(velocity.x, 0.5D, velocity.z);
			((ServerPlayerEntity) playerIn).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(playerIn));
			playerIn.fallDistance = 0.0F;
			this.damage(DamageSource.player(playerIn), Float.MAX_VALUE);
			this.playSound(MubbleSounds.ENTITY_GOOMBA_CRUSH, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
		}
	}
}