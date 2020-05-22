package hugman.mubble.objects.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

public class DuckEntity extends AnimalEntity
{
	private static final DataParameter<Integer> DUCK_TYPE = EntityDataManager.createKey(DuckEntity.class, DataSerializers.VARINT);
	private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromTag(MubbleTags.Items.DUCK_FEEDING);
	public float wingRotation;
	public float destPos;
	public float oFlapSpeed;
	public float oFlap;
	public float wingRotDelta = 1.0F;
	
    public DuckEntity(EntityType<? extends DuckEntity> type, World worldIn) 
    {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }
    
    @Override
    protected void registerData()
    {
    	super.registerData();
    	this.dataManager.register(DUCK_TYPE, 0);
    }
    
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, ILivingEntityData data, CompoundNBT compound)
    {
		Biome biome = world.getBiome(new BlockPos(this));
		DuckEntity.Type type = DuckEntity.Type.getTypeByBiome(biome);
		if(data instanceof DuckEntity.DuckData)
		{
			type = ((DuckEntity.DuckData)data).type;
		}
		else
		{
			data = new DuckEntity.DuckData(type);
		}
		
		this.setVariantType(type);
    	return super.onInitialSpawn(world, difficulty, spawnReason, data, compound);
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
    public void writeAdditional(CompoundNBT compound)
    {
    	super.writeAdditional(compound);
    	compound.putString("Type", this.getVariantType().getName());
    }
    
    @Override
    public void readAdditional(CompoundNBT compound)
    {
    	super.readAdditional(compound);
    	this.setVariantType(DuckEntity.Type.getTypeByName(compound.getString("Type")));
    }
    
    @Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}
    
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size)
    {
    	return this.isChild() ? size.height * 0.85F : size.height * 0.92F;
    }
    
    @Override
    public void livingTick()
    {
		super.livingTick();
		this.oFlap = this.wingRotation;
		this.oFlapSpeed = this.destPos;
		this.destPos = (float)((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
		this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
		if(!this.onGround && this.wingRotDelta < 0.55F)
		{
			this.wingRotDelta = 0.55F;
		}

		this.wingRotDelta = (float)((double) this.wingRotDelta * 0.9D);
		Vec3d vec3d = this.getMotion();
		if(!this.onGround && vec3d.y < 0.0D)
		{
			this.setMotion(vec3d.mul(1.0D, 0.75D, 1.0D));
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
    	DuckEntity entity = MubbleEntities.DUCK.create(this.world);
    	entity.setVariantType(((DuckEntity)parent).getVariantType());
    	return entity;
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
	
	public static class DuckData extends AgeableEntity.AgeableData
	{
		public final DuckEntity.Type type;

		public DuckData(DuckEntity.Type typeIn)
		{
			this.setBabyAllowed(false);
			this.type = typeIn;
		}
	}
    
	public DuckEntity.Type getVariantType()
	{
		return DuckEntity.Type.getTypeByIndex(this.dataManager.get(DUCK_TYPE));
	}

	private void setVariantType(DuckEntity.Type type)
	{
		this.dataManager.set(DUCK_TYPE, type.getIndex());
	}
	
	public static List<Biome> getSpawnBiomes()
	{
		ArrayList<Biome> biomeList = new ArrayList<Biome>();
		for(DuckEntity.Type type : Type.typeList)
		{
			biomeList.addAll(type.getSpawnBiomes());
		}
		List<Biome> fBiomeList = biomeList.stream().distinct().collect(Collectors.toList());
		return fBiomeList;
	}
	
	public static enum Type
	{
		PEKIN(0, "pekin", Biomes.PLAINS, Biomes.FOREST),
		MALLARD(1, "mallard", Biomes.PLAINS, Biomes.RIVER, Biomes.SWAMP);

		private static final DuckEntity.Type[] typeList = Arrays.stream(values()).sorted(Comparator.comparingInt(DuckEntity.Type::getIndex)).toArray((index) ->
		{
			return new DuckEntity.Type[index];
		});
		private static final Map<String, DuckEntity.Type> TYPES_BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(DuckEntity.Type::getName, (type) ->
		{
			return type;
		}));
		private final int index;
		private final String name;
		private final List<Biome> spawnBiomes;

		private Type(int indexIn, String nameIn, Biome... spawnBiomesIn)
		{
			this.index = indexIn;
			this.name = nameIn;
			this.spawnBiomes = Arrays.asList(spawnBiomesIn);
		}

		public String getName() {
			return this.name;
		}

		public List<Biome> getSpawnBiomes()
		{
			return this.spawnBiomes;
		}

		public int getIndex()
		{
			return this.index;
		}

		public static DuckEntity.Type getTypeByName(String name)
		{
			return TYPES_BY_NAME.getOrDefault(name, PEKIN);
		}

		public static DuckEntity.Type getTypeByIndex(int index)
		{
			if(index < 0 || index > typeList.length)
			{
				index = 0;
			}

			return typeList[index];
		}
		
		public static DuckEntity.Type getTypeByBiome(Biome biome)
		{
			List<Type> shuffledList = Arrays.asList(typeList.clone());
			Collections.shuffle(shuffledList);
			for(DuckEntity.Type type : shuffledList)
			{
				if(type.getSpawnBiomes().contains(biome))
				{
					return type;
				}
				else
				{
					continue;
				}
			}
			return PEKIN;
		}
	}
}