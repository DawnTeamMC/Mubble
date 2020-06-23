package hugman.mubble.objects.entity;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleStats;
import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.util.CalendarEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.stream.Collectors;

public class ToadEntity extends AbstractTraderEntity {
	private static final TrackedData<Integer> TOAD_VARIANT = DataTracker.registerData(ToadEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public ToadEntity(EntityType<? extends ToadEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData data, CompoundTag compound) {
		ToadEntity.Type type = ToadEntity.Type.getTypeByIndex(this.world.random.nextInt(16));
		if(data instanceof ToadEntity.ToadData) {
			type = ((ToadEntity.ToadData) data).type;
		}
		else {
			data = new ToadEntity.ToadData(type);
		}
		this.setVariant(type);
		return super.initialize(world, difficulty, reason, data, compound);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new StopFollowingCustomerGoal(this));
		this.goalSelector.add(1, new FleeEntityGoal<>(this, ChinchoEntity.class, 10f, 1.2d, 1.45d, EntityPredicates.VALID_LIVING_ENTITY::test));
		this.goalSelector.add(1, new FleeEntityGoal<>(this, LivingEntity.class, checkedEntity -> MubbleTags.Items.TOAD_FEAR.contains(checkedEntity.getMainHandStack().getItem()), 10f, 1.2f, 1.45f, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test));
		this.goalSelector.add(1, new FleeEntityGoal<>(this, LivingEntity.class, checkedEntity -> MubbleTags.Items.TOAD_FEAR.contains(checkedEntity.getOffHandStack().getItem()), 10f, 1.2f, 1.45f, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test));
		this.goalSelector.add(1, new FleeEntityGoal<>(this, LivingEntity.class, checkedEntity -> MubbleTags.Items.TOAD_FEAR.contains(checkedEntity.getEquippedStack(EquipmentSlot.HEAD).getItem()), 10f, 1.2f, 1.45f, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test));
		this.goalSelector.add(1, new LongDoorInteractGoal(this, true));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.6D));
		this.goalSelector.add(1, new LookAtCustomerGoal(this));
		this.goalSelector.add(4, new GoToWalkTargetGoal(this, 1.45D));
		this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.45D));
		this.goalSelector.add(9, new GoToEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
	}

	public static Builder createToadAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 9.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		if(CalendarEvents.isAprilFools) {
			return MubbleSounds.ENTITY_TOAD_BUP;
		}
		else {
			return MubbleSounds.ENTITY_TOAD_AMBIENT;
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return MubbleSounds.ENTITY_TOAD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MubbleSounds.ENTITY_TOAD_DEATH;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(TOAD_VARIANT, 0);
	}

	@Override
	public void writeCustomDataToTag(CompoundTag compound) {
		super.writeCustomDataToTag(compound);
		compound.putString("Type", this.getVariant().getName());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag compound) {
		super.readCustomDataFromTag(compound);
		this.setVariant(ToadEntity.Type.getTypeByName(compound.getString("Type")));
	}

	@Override
	public PassiveEntity createChild(PassiveEntity mate) {
		ToadEntity child = new ToadEntity(MubbleEntities.TOAD, this.world);
		child.setVariant(this.random.nextFloat() < 0.5f ? ((ToadEntity)(mate)).getVariant() : this.getVariant());
		return child;
	}

	@Environment(EnvType.CLIENT)
	public Vec3d method_29919() {
		return new Vec3d(0.0D, (double)(0.6F * this.getStandingEyeHeight()), (double)(this.getWidth() * 0.4F));
	}

	public static boolean canSpawn(EntityType<ToadEntity> entity, WorldAccess world, SpawnReason reason, BlockPos pos, Random rand) {
		return true;
	}

	// TRADING STUFF

	@Override
	public boolean isLevelledTrader() {
		return false;
	}

	@Override
	protected void fillRecipes() {
		TradeOffers.Factory[] factory1 = TradeOffers.WANDERING_TRADER_TRADES.get(1);
		TradeOffers.Factory[] factory2 = TradeOffers.WANDERING_TRADER_TRADES.get(2);
		if(factory1 != null && factory2 != null) {
			TraderOfferList traderOfferList = this.getOffers();
			this.fillRecipesFromPool(traderOfferList, factory1, 5);
			int i = this.random.nextInt(factory2.length);
			TradeOffers.Factory factory = factory2[i];
			TradeOffer tradeOffer = factory.create(this, this.random);
			if(tradeOffer != null) {
				traderOfferList.add(tradeOffer);
			}

		}
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		if(itemStack.getItem() != MubbleItems.TOAD_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && !this.isBaby()) {
			if(hand == Hand.MAIN_HAND) {
				player.incrementStat(MubbleStats.TALKED_TO_TOAD);
			}
			if(!this.getOffers().isEmpty()) {
				if(!this.world.isClient) {
					this.setCurrentCustomer(player);
					this.sendOffers(player, this.getDisplayName(), 1);
				}

			}
			return ActionResult.success(this.world.isClient);
		}
		else {
			return super.interactMob(player, hand);
		}
	}

	@Override
	protected void afterUsing(TradeOffer offer) {
		if(offer.shouldRewardPlayerExperience()) {
			int i = 3 + this.random.nextInt(4);
			this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.getX(), this.getY() + 0.5D, this.getZ(), i));
		}
	}

	private void setVariant(ToadEntity.Type type) {
		this.dataTracker.set(TOAD_VARIANT, type.getIndex());
	}

	public ToadEntity.Type getVariant() {
		return ToadEntity.Type.getTypeByIndex(this.dataTracker.get(TOAD_VARIANT));
	}

	@Override
	protected Identifier getLootTableId() {
		return new Identifier(Mubble.MOD_ID, "entities/toad/" + getVariant().getName());
	}

	public static class ToadData extends PassiveEntity.PassiveData {
		public final ToadEntity.Type type;

		public ToadData(ToadEntity.Type typeIn) {
			this.setBabyAllowed(false);
			this.type = typeIn;
		}
	}

	public enum Type {
		WHITE(0, "species/white"),
		LIGHT_GRAY(1, "species/light_gray"),
		GRAY(2, "species/gray"),
		BLACK(3, "species/black"),
		BROWN(4, "species/brown"),
		RED(5, "species/red"),
		ORANGE(6, "species/orange"),
		YELLOW(7, "species/yellow"),
		LIME(8, "species/lime"),
		GREEN(9, "species/green"),
		CYAN(10, "species/cyan"),
		LIGHT_BLUE(11, "species/light_blue"),
		BLUE(12, "species/blue"),
		PURPLE(13, "species/purple"),
		MAGENTA(14, "species/magenta"),
		PINK(15, "species/pink"),
		CAPTAIN_TOAD(100, "brigade/captain_toad"),
		BRIGADIER_HINT_TOAD(101, "brigade/hint_toad"),
		BRIGADIER_BANKTOAD(102, "brigade/banktoad"),
		BRIGADIER_YELLOW(103, "brigade/yellow"),
		BRIGADIER_MAILTOAD(104, "brigade/mailtoad"),
		PARTY(105, "party"),
		KISEKAE(106, "kisekae");

		private final int index;
		private final String name;
		private static final ToadEntity.Type[] typeList = Arrays.stream(values()).sorted(Comparator.comparingInt(ToadEntity.Type::getIndex)).toArray((index) ->
		{
			return new ToadEntity.Type[index];
		});
		private static final Map<String, ToadEntity.Type> TYPES_BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(ToadEntity.Type::getName, (type) ->
		{
			return type;
		}));

		Type(int index, String name) {
			this.index = index;
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public int getIndex() {
			return this.index;
		}

		public static ToadEntity.Type getTypeByName(String name) {
			return TYPES_BY_NAME.getOrDefault(name, RED);
		}

		public static ToadEntity.Type getTypeByIndex(int index) {
			if(index < 0 || index > typeList.length) {
				index = 0;
			}
			return typeList[index];
		}
	}
}