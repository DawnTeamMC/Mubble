package hugman.mubble.objects.entity;

import java.util.Random;

import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.packet.EntityVelocityUpdateS2CPacket;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.GoToWalkTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GoombaEntity extends MobEntityWithAi
{    
    public GoombaEntity(EntityType<? extends GoombaEntity> type, World worldIn) 
    {
        super(type, worldIn);
    }
    
	@Override
    protected void initGoals()
    {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0D));
        this.goalSelector.add(7, new EscapeDangerGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[] {GoombaEntity.class}));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, ToadEntity.class, true));
    }
    
    @Override
    protected void initAttributes() 
    {
        super.initAttributes();        
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(12.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(25.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttributes().register(EntityAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }
    
    @Override
    public float getEyeHeight(EntityPose pose)
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
    public void onPlayerCollision(PlayerEntity playerIn)
    {
    	Box hitbox = this.getBoundingBox().shrink(0, -1, 0).expand(-0.4, 0, -0.4);
    	Vec3d vec3d = playerIn.getVelocity();
    	if(!this.world.isClient() && !playerIn.isSpectator() && hitbox.intersects(playerIn.getBoundingBox()) && vec3d.y < 0.3D && !this.dead)
    	{
    		playerIn.setVelocity(vec3d.x, 0.5D, vec3d.z);
			((ServerPlayerEntity) playerIn).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(playerIn));
    		playerIn.fallDistance = 0.0F;
    		this.damage(DamageSource.player(playerIn), Float.MAX_VALUE);
    		this.playSound(MubbleSounds.ENTITY_GOOMBA_CRUSH, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    	}
    }
    
	public static boolean canSpawn(EntityType<GoombaEntity> entity, IWorld world, SpawnType reason, BlockPos pos, Random rand)
	{
		return world.getDifficulty() != Difficulty.PEACEFUL;
	}
}