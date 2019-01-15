package hugman.mod.entity;

import java.util.Set;

import com.google.common.collect.Sets;

import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleItems;
import hugman.mod.util.handlers.LootTableHandler;
import hugman.mod.util.handlers.SoundHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityToad extends EntityAnimal
{
    private static final DataParameter<Integer> TOAD_COLOR = EntityDataManager.<Integer>createKey(EntityToad.class, DataSerializers.VARINT);
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(MubbleItems.SUPER_MUSHROOM, MubbleItems.PEACH, MubbleItems.SUPER_STAR);
    
    public EntityToad(World worldIn) 
    {
        super(worldIn);
        this.setSize(0.6F, 1.4F);
    }
    
    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(TOAD_COLOR, Integer.valueOf(0));
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        this.setColor(this.world.rand.nextInt(16));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityChincho.class, 10, 1.2f, 1.45f));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityItem.class, checkedEntity -> (checkedEntity).getItem().getItem() == MubbleCostumes.SUPER_CROWN, 10, 1.2f, 1.45f));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityPlayer.class, checkedEntity -> (checkedEntity).getHeldItem(EnumHand.MAIN_HAND).getItem() == MubbleCostumes.SUPER_CROWN, 10, 1.2f, 1.45f));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityPlayer.class, checkedEntity -> (checkedEntity).getHeldItem(EnumHand.OFF_HAND).getItem() == MubbleCostumes.SUPER_CROWN, 10, 1.2f, 1.45f));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityPlayer.class, checkedEntity -> (checkedEntity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == MubbleCostumes.SUPER_CROWN, 10, 1.2f, 1.45f));
        this.tasks.addTask(1, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(2, new EntityAIPanic(this, 1.6D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.4D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityChicken.class, 10.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, this.getClass(), 8.0F));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
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
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("ToadColor", this.getColor());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setColor(compound.getInteger("ToadColor"));
    }
    
    public int getColor()
    {
        return ((Integer)this.dataManager.get(TOAD_COLOR)).intValue();
    }

    public void setColor(int skinId)
    {
        this.dataManager.set(TOAD_COLOR, Integer.valueOf(skinId));
    }
    
    @Override
    protected ResourceLocation getLootTable() 
    {
    	if(this.getColor()==0) return LootTableHandler.BLUE_TOAD;
    	if(this.getColor()==1) return LootTableHandler.LIGHT_BLUE_TOAD;
    	if(this.getColor()==2) return LootTableHandler.CYAN_TOAD;
    	if(this.getColor()==3) return LootTableHandler.GREEN_TOAD;
    	if(this.getColor()==4) return LootTableHandler.LIME_TOAD;
    	if(this.getColor()==5) return LootTableHandler.YELLOW_TOAD;
    	if(this.getColor()==6) return LootTableHandler.ORANGE_TOAD;
    	if(this.getColor()==7) return LootTableHandler.RED_TOAD;
    	if(this.getColor()==8) return LootTableHandler.PINK_TOAD;
    	if(this.getColor()==9) return LootTableHandler.MAGENTA_TOAD;
    	if(this.getColor()==10) return LootTableHandler.PURPLE_TOAD;
    	if(this.getColor()==11) return LootTableHandler.BROWN_TOAD;
    	if(this.getColor()==12) return LootTableHandler.WHITE_TOAD;
    	if(this.getColor()==13) return LootTableHandler.LIGHT_GRAY_TOAD;
    	if(this.getColor()==14) return LootTableHandler.GRAY_TOAD;
    	if(this.getColor()==15) return LootTableHandler.BLACK_TOAD;
        if(this.getColor() == 100) return LootTableHandler.CAPTAIN_TOAD;
        if(this.getColor() == 101) return LootTableHandler.HINT_TOAD;
        if(this.getColor() == 102) return LootTableHandler.BANKTOAD;
        if(this.getColor() == 102) return LootTableHandler.YELLOWB_TOAD;
        if(this.getColor() == 104) return LootTableHandler.MAILTOAD;
        if(this.getColor() == 105) return LootTableHandler.RED_TOAD;
        return LootTableHandler.TOAD;
    }
    
    public EntityToad createChild(EntityAgeable ageable) 
    {
        return null;
    }
}
