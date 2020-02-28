package hugman.mubble.objects.entity;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DuckEntity extends AnimalEntity
{
	private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromTag(MubbleTags.Items.DUCK_FEEDING);
	public float wingRotation;
	public float destPos;
	public float oFlapSpeed;
	public float oFlap;
	public float wingRotDelta = 1.0F;
	
    public DuckEntity(EntityType<? extends DuckEntity> type, World worldIn) 
    {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, 0.5F);
    }
    
    @Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, false, TEMPTATION_ITEMS));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
	}
    
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size)
    {
    	return this.isChild() ? size.height * 0.85F : size.height * 0.92F;
    }
    
    @Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}
    
    @Override
    public void livingTick()
    {
		super.livingTick();
		this.oFlap = this.wingRotation;
		this.oFlapSpeed = this.destPos;
		this.destPos = (float)((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
		this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
		if(!this.onGround && this.wingRotDelta < 1.5F)
		{
			this.wingRotDelta = 1.5F;
		}

		this.wingRotDelta = (float)((double) this.wingRotDelta * 0.9D);
		Vec3d vec3d = this.getMotion();
		if(!this.onGround && vec3d.y < 0.0D)
		{
			this.setMotion(vec3d.mul(1.0D, 0.5D, 1.0D));
		}
		
		this.wingRotation += this.wingRotDelta * 2.0F;
    }
    
    @Override
    public boolean handleFallDamage(float distance, float multiplier)
    {
    	return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound()
    {
    	return MubbleSounds.ENTITY_DUCK_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
    	return MubbleSounds.ENTITY_DUCK_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
    	return MubbleSounds.ENTITY_DUCK_DEATH;
    }
    
    @Override
	protected void playStepSound(BlockPos pos, BlockState state)
	{
		this.playSound(MubbleSounds.ENTITY_DUCK_STEP, 0.15F, 1.0F);
	}
	
    @Override
    public DuckEntity createChild(AgeableEntity parent)
    {
    	return MubbleEntities.DUCK.create(this.world);
    }
    
    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
    	return TEMPTATION_ITEMS.test(stack);
    }
    
    @Override
	public void updatePassenger(Entity entity)
    {
		super.updatePassenger(entity);
		float f = MathHelper.sin(this.renderYawOffset * ((float) Math.PI / 180F));
		float f1 = MathHelper.cos(this.renderYawOffset * ((float) Math.PI / 180F));
		entity.setPosition(this.getX() + (double) (0.1F * f), this.getBodyY(0.5D) + entity.getYOffset() + 0.0D, this.getZ() - (double) (0.1F * f1));
		if(entity instanceof LivingEntity)
		{
			((LivingEntity)entity).renderYawOffset = this.renderYawOffset;
		}

	}
}