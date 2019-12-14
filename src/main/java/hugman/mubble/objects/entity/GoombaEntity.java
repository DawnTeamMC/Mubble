package hugman.mubble.objects.entity;

import java.util.Random;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GoombaEntity extends MonsterEntity
{    
    public GoombaEntity(EntityType<? extends GoombaEntity> type, World worldIn) 
    {
        super(type, worldIn);
    }
    
	@Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[] {GoombaEntity.class}));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ToadEntity.class, true));
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
    public float getEyeHeight(Pose pose)
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
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
    	 this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}
    
    @Override
    public void onCollideWithPlayer(PlayerEntity playerIn)
    {
    	AxisAlignedBB hitbox = this.getBoundingBox().contract(0, -1, 0).grow(-0.4, 0, -0.4);
    	Vec3d vec3d = playerIn.getMotion();
    	if(!this.world.isRemote() && !playerIn.isSpectator() && hitbox.intersects(playerIn.getBoundingBox()) && vec3d.y < 0.3D && !this.dead)
    	{
    		playerIn.setMotion(vec3d.x, 0.5D, vec3d.z);
			((ServerPlayerEntity)playerIn).connection.sendPacket(new SEntityVelocityPacket(playerIn));
    		playerIn.fallDistance = 0.0F;
    		this.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), Float.MAX_VALUE);
    		this.playSound(MubbleSounds.ENTITY_GOOMBA_CRUSH, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    	}
    }
    
	public static boolean canSpawn(EntityType<GoombaEntity> entity, IWorld world, SpawnReason reason, BlockPos pos, Random rand)
	{
		return world.getDifficulty() != Difficulty.PEACEFUL;
	}
}