package hugman.mod.objects.entity;

import hugman.mod.init.MubbleEntities;
import hugman.mod.init.MubbleLootTables;
import hugman.mod.init.MubbleSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGoomba extends EntityMob
{    
    public EntityGoomba(World worldIn) 
    {
        super(MubbleEntities.GOOMBA, worldIn);
        this.setSize(0.9375F, 0.9375F);
    }
    
	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityGoomba.class}));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityToad.class, true));
    }
    
    @Override
    protected void registerAttributes() 
    {
        super.registerAttributes();        
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(25.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }
    
    @Override
    public float getEyeHeight()
    {
        return 0.40625F;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) 
    {
        return MubbleSounds.ENTITY_GOOMBA_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() 
    {
        return MubbleSounds.ENTITY_GOOMBA_DEATH;
    }
    
	protected SoundEvent getStepSound()
	{
		return MubbleSounds.ENTITY_GOOMBA_STEP;
	}

	@Override
	protected void playStepSound(BlockPos pos, IBlockState blockIn)
	{
    	 this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}
    
    @Override
    protected ResourceLocation getLootTable() 
    {
        return MubbleLootTables.CHINCHO;
    }
    
    @Override
    public void onCollideWithPlayer(EntityPlayer playerIn)
    {
    	AxisAlignedBB hitbox = this.getBoundingBox().contract(0, -1, 0).shrink(0.1, 0, 0.1);
    	if(!playerIn.isSpectator() && hitbox.intersects(playerIn.getBoundingBox()) && playerIn.motionY < 0.3D && !this.dead)
    	{
    		playerIn.motionY = 0.5D;
    		playerIn.fallDistance = 0.0F;
    		if(playerIn.motionY == 0.5D)
    		{
        		this.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), Float.MAX_VALUE);
        		this.playSound(MubbleSounds.ENTITY_GOOMBA_CRUSH, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    		}
    	}
    }
}