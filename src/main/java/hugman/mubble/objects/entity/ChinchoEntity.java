package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class ChinchoEntity extends MobEntityWithAi {
	public ChinchoEntity(EntityType<? extends ChinchoEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.add(3, new FleeEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0D));
		this.goalSelector.add(7, new EscapeDangerGoal(this, 1.0D));
		this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this, ChinchoEntity.class));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, ToadEntity.class, true));
	}

	public static Builder createChinchoAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 12.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public EntityGroup getGroup() {
		return EntityGroup.UNDEAD;
	}

	protected boolean shouldBurnInDay() {
		return true;
	}

	@Override
	public void tickMovement() {
		if(this.world.isDay() && !this.world.isClient && this.shouldBurnInDay()) {
			float f = this.getBrightnessAtEyes();
			if(f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.isSkyVisible(new BlockPos(this.getX(), this.getY() + this.getEyeY(), this.getZ()))) {
				boolean flag = true;
				ItemStack itemstack = this.getEquippedStack(EquipmentSlot.HEAD);
				if(!itemstack.isEmpty()) {
					if(itemstack.isDamageable()) {
						itemstack.setDamage(itemstack.getDamage() + this.random.nextInt(2));
						if(itemstack.getDamage() >= itemstack.getMaxDamage()) {
							this.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
							this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
						}
					}
					flag = false;
				}
				if(flag) {
					this.setFireTicks(8);
				}
			}
		}
		super.tickMovement();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return MubbleSounds.ENTITY_CHINCHO_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return MubbleSounds.ENTITY_CHINCHO_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MubbleSounds.ENTITY_CHINCHO_DEATH;
	}

	public static boolean canSpawn(EntityType<ChinchoEntity> entity, WorldAccess world, SpawnReason reason, BlockPos pos, Random rand) {
		return world.getDifficulty() != Difficulty.PEACEFUL;
	}
}