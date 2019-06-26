package hugman.mod.objects.entity;

import hugman.mod.init.MubbleEntities;
import hugman.mod.init.MubbleLootTables;
import hugman.mod.init.MubbleSounds;
import hugman.mod.init.MubbleTags;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class ToadEntity extends AnimalEntity
{
	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(ToadEntity.class, DataSerializers.VARINT);
	private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromTag(MubbleTags.Items.TEMPTING_TO_TOAD);
	
    public ToadEntity(EntityType<? extends ToadEntity> type, World worldIn) 
    {
        super(type, worldIn);
    }
    
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag)
    {
        this.setVariant(this.world.rand.nextInt(16));
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }
    
    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, ChinchoEntity.class, 10f, 1.2d, 1.45d, EntityPredicates.IS_ALIVE::test));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, checkedEntity -> MubbleTags.Items.SCARY_TO_TOAD.contains(((LivingEntity) checkedEntity).getHeldItemMainhand().getItem()), 10f, 1.2f, 1.45f, EntityPredicates.CAN_AI_TARGET::test));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, checkedEntity -> MubbleTags.Items.SCARY_TO_TOAD.contains(((LivingEntity) checkedEntity).getHeldItemOffhand().getItem()), 10f, 1.2f, 1.45f, EntityPredicates.CAN_AI_TARGET::test));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LivingEntity.class, checkedEntity -> MubbleTags.Items.SCARY_TO_TOAD.contains(((LivingEntity) checkedEntity).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()), 10f, 1.2f, 1.45f, EntityPredicates.CAN_AI_TARGET::test));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.6D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.4D, false, TEMPTATION_ITEMS));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtGoal(this, ChickenEntity.class, 10.0F));
        this.goalSelector.addGoal(6, new LookAtGoal(this, this.getClass(), 8.0F));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }
    
    @Override
    protected void registerAttributes() 
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }
    
    @Override
    public float getEyeHeight(Pose pose)
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
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditional(CompoundNBT compound)
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
            return MubbleLootTables.WHITE_TOAD;
        case 1:
            return MubbleLootTables.LIGHT_GRAY_TOAD;
        case 2:
            return MubbleLootTables.GRAY_TOAD;
        case 3:
            return MubbleLootTables.BLACK_TOAD;
        case 4:
            return MubbleLootTables.BROWN_TOAD;
        case 5:
            return MubbleLootTables.RED_TOAD;
        case 6:
            return MubbleLootTables.ORANGE_TOAD;
        case 7:
            return MubbleLootTables.YELLOW_TOAD;
        case 8:
            return MubbleLootTables.LIME_TOAD;
        case 9:
            return MubbleLootTables.GREEN_TOAD;
        case 10:
            return MubbleLootTables.CYAN_TOAD;
        case 11:
            return MubbleLootTables.LIGHT_BLUE_TOAD;
        case 12:
            return MubbleLootTables.BLUE_TOAD;
        case 13:
            return MubbleLootTables.PURPLE_TOAD;
        case 14:
            return MubbleLootTables.MAGENTA_TOAD;
        case 15:
            return MubbleLootTables.PINK_TOAD;
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
    public AgeableEntity createChild(AgeableEntity ageable)
    {
    	ToadEntity childToad = new ToadEntity(MubbleEntities.TOAD, this.world);
    	childToad.setVariant(this.world.rand.nextInt(16));
    	return childToad;
    }
    
    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return TEMPTATION_ITEMS.test(stack);
    }
}