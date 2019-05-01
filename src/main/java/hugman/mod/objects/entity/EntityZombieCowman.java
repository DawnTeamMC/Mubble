package hugman.mod.objects.entity;

import java.util.UUID;

import hugman.mod.init.MubbleEntities;
import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityZombieCowman extends EntityPigZombie
{
	private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = (new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, 0)).setSaved(false);
	private int angerLevel;
	private int randomSoundDelay;
	private UUID angerTargetUUID;
	
	public EntityZombieCowman(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	public EntityType<?> getType()
	{
		return MubbleEntities.ZOMBIE_COWMAN;
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MubbleSounds.ENTITY_ZOMBIE_COWMAN_AMBIENT;
	}
	
	@Override
	protected SoundEvent getStepSound()
	{
		return MubbleSounds.ENTITY_ZOMBIE_COWMAN_STEP;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MubbleSounds.ENTITY_ZOMBIE_COWMAN_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MubbleSounds.ENTITY_ZOMBIE_COWMAN_DEATH;
	}
	
	protected void applyEntityAI()
	{
		this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
		this.targetTasks.addTask(1, new EntityZombieCowman.AIHurtByAggressor(this));
		this.targetTasks.addTask(2, new EntityZombieCowman.AITargetAggressor(this));
	}
	
	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.34F);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}
	
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
	}
	
	protected void updateAITasks()
	{
		IAttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (this.isAngry())
		{
			if (!this.isChild() && !iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
			{
				iattributeinstance.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
			}

			--this.angerLevel;
		}
		else if (iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
		{
			iattributeinstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
		}

		if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0)
		{
			this.playSound(MubbleSounds.ENTITY_ZOMBIE_COWMAN_ANGRY, this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getRevengeTarget() == null)
		{
			EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
			this.setRevengeTarget(entityplayer);
			this.attackingPlayer = entityplayer;
			this.recentlyHit = this.getRevengeTimer();
		}

		super.updateAITasks();
	}
	
	private void becomeAngryAt(Entity p_70835_1_)
	{
		this.angerLevel = 400 + this.rand.nextInt(400);
		this.randomSoundDelay = this.rand.nextInt(20);
		if (p_70835_1_ instanceof EntityLivingBase)
		{
			this.setRevengeTarget((EntityLivingBase)p_70835_1_);
		}
	}
	
	static class AIHurtByAggressor extends EntityAIHurtByTarget
	{
		public AIHurtByAggressor(EntityZombieCowman p_i45828_1_)
		{
			super(p_i45828_1_, true);
		}

		protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
		{
			super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
			if (creatureIn instanceof EntityZombieCowman)
			{
				((EntityZombieCowman)creatureIn).becomeAngryAt(entityLivingBaseIn);
			}
		}
	}

	static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
	{
		public AITargetAggressor(EntityZombieCowman p_i45829_1_)
		{
			super(p_i45829_1_, EntityPlayer.class, true);
		}
		
		public boolean shouldExecute()
		{
			return ((EntityZombieCowman)this.taskOwner).isAngry() && super.shouldExecute();
		}
	}
}