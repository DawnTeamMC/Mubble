package hugman.mod.entity;

import java.util.Set;

import com.google.common.collect.Sets;

import hugman.mod.init.ItemInit;
import hugman.mod.util.handlers.LootTableHandler;
import hugman.mod.util.handlers.SoundHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityToad extends EntityAnimal
{
	private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(ItemInit.SUPER_MUSHROOM, ItemInit.PEACH, ItemInit.SUPER_STAR);
	
	public EntityToad(World worldIn) 
	{
		super(worldIn);
		this.setSize(0.6F, 1.4F);
	}
	
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityHorse.class, 10, 2.2f, 2.4f));
        this.tasks.addTask(2, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.4D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(4, new EntityAIFindEntityNearest(this, EntityChicken.class));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityChicken.class, 10.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityToad.class, 8.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
	}
	
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}
	
	@Override
	public float getEyeHeight()
    {
        return 1.25f;
    }
	
	@Override
	protected SoundEvent getAmbientSound() 
	{
		return SoundHandler.ENTITY_TOAD_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) 
	{
		return SoundHandler.ENTITY_TOAD_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() 
	{
		return SoundHandler.ENTITY_TOAD_DEATH;
	}
	
	@Override
	protected ResourceLocation getLootTable() 
	{
		return LootTableHandler.TOAD;
	}
	
	public EntityToad createChild(EntityAgeable ageable) 
	{
		return null;
	}
}
