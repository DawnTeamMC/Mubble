package hugman.mod.objects.entity;

import hugman.mod.init.MubbleCostumes;
import hugman.mod.init.MubbleEntities;
import hugman.mod.init.MubbleItems;
import hugman.mod.init.MubbleLootTables;
import hugman.mod.init.MubbleSounds;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityToad extends EntityAnimal
{
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityToad.class, DataSerializers.VARINT);
	private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(MubbleItems.SUPER_MUSHROOM);
	
    public EntityToad(World worldIn) 
    {
        super(MubbleEntities.TOAD, worldIn);
        this.setSize(0.6F, 1.4F);
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData entityLivingData, NBTTagCompound itemNbt)
    {
        this.setVariant(this.world.rand.nextInt(16));
        return super.onInitialSpawn(difficulty, entityLivingData, itemNbt);
    }
    
    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityChincho.class, 10f, 1.2d, 1.45d, EntitySelectors.IS_ALIVE));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityItem.class, checkedEntity -> ((EntityItem) checkedEntity).getItem().getItem() == MubbleCostumes.SUPER_CROWN, 10f, 1.2d, 1.45d, EntitySelectors.IS_ALIVE));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, checkedEntity -> ((EntityLivingBase) checkedEntity).getHeldItemMainhand().getItem() == MubbleCostumes.SUPER_CROWN, 10f, 1.2f, 1.45f, EntitySelectors.CAN_AI_TARGET));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, checkedEntity -> ((EntityLivingBase) checkedEntity).getHeldItemOffhand().getItem() == MubbleCostumes.SUPER_CROWN, 10f, 1.2f, 1.45f, EntitySelectors.CAN_AI_TARGET));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, checkedEntity -> ((EntityLivingBase) checkedEntity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == MubbleCostumes.SUPER_CROWN, 10f, 1.2f, 1.45f, EntitySelectors.CAN_AI_TARGET));
        this.tasks.addTask(1, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(2, new EntityAIPanic(this, 1.6D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.4D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityChicken.class, 10.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, this.getClass(), 8.0F));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }
    
    @Override
    protected void registerAttributes() 
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }
    
    @Override
    public float getEyeHeight()
    {
    	if(this.isChild()) return 0.75f;
        return 1.25f;
    }
    
    @Override
    protected SoundEvent getAmbientSound() 
    {
        return MubbleSounds.ENTITY_TOAD_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) 
    {
        return MubbleSounds.ENTITY_TOAD_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() 
    {
        return MubbleSounds.ENTITY_TOAD_DEATH;
    }
    
    public int getVariant()
    {
    	return this.dataManager.get(VARIANT);
	}

	public void setVariant(int variantIn)
	{
		this.dataManager.set(VARIANT, variantIn);
	}
	
	@Override
	protected void registerData()
	{
		super.registerData();
		this.dataManager.register(VARIANT, 0);
	}
    
    @Override
    public void writeAdditional(NBTTagCompound compound)
    {
        super.writeAdditional(compound);
        compound.setInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
    }
    
    @Override
    protected ResourceLocation getLootTable()
    {
		switch(this.getVariant())
		{
        case 0:
            return MubbleLootTables.BLUE_TOAD;
        case 1:
            return MubbleLootTables.LIGHT_BLUE_TOAD;
        case 2:
            return MubbleLootTables.CYAN_TOAD;
        case 3:
            return MubbleLootTables.GREEN_TOAD;
        case 4:
            return MubbleLootTables.LIME_TOAD;
        case 5:
            return MubbleLootTables.YELLOW_TOAD;
        case 6:
            return MubbleLootTables.ORANGE_TOAD;
        case 7:
            return MubbleLootTables.RED_TOAD;
        case 8:
            return MubbleLootTables.PINK_TOAD;
        case 9:
            return MubbleLootTables.MAGENTA_TOAD;
        case 10:
            return MubbleLootTables.PURPLE_TOAD;
        case 11:
            return MubbleLootTables.BROWN_TOAD;
        case 12:
            return MubbleLootTables.WHITE_TOAD;
        case 13:
            return MubbleLootTables.LIGHT_GRAY_TOAD;
        case 14:
            return MubbleLootTables.GRAY_TOAD;
        case 15:
            return MubbleLootTables.BLACK_TOAD;
        case 100:
            return MubbleLootTables.CAPTAIN_TOAD;
        case 101:
            return MubbleLootTables.HINT_TOAD;
        case 102:
            return MubbleLootTables.BANKTOAD;
        case 103:
            return MubbleLootTables.YELLOW_BTOAD;
        case 104:
            return MubbleLootTables.MAILTOAD;
        case 105:
            return MubbleLootTables.RED_TOAD;
		default:
			return MubbleLootTables.RED_TOAD;
		}
    }
    
    @Override
    public EntityAgeable createChild(EntityAgeable ageable)
    {
    	EntityToad childToad = new EntityToad(this.world);
    	childToad.setVariant(this.world.rand.nextInt(16));
    	return childToad;
    }
    
    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return TEMPTATION_ITEMS.test(stack);
    }
}