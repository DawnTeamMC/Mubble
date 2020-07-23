package hugman.mubble.object.entity;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleStats;
import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.util.CalendarEvents;
import hugman.mubble.util.trade_offer.ToadTradeOffers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_5425;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ToadEntity extends AbstractTraderEntity {
	private static final TrackedData<Integer> TOAD_VARIANT = DataTracker.registerData(ToadEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public ToadEntity(EntityType<? extends ToadEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public EntityData initialize(class_5425 world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
		ToadEntity.Type type = ToadEntity.Type.getTypeByIndex(this.world.random.nextInt(16));
		if(entityData instanceof ToadEntity.ToadData) {
			type = ((ToadEntity.ToadData) entityData).type;
		}
		else {
			entityData = new ToadEntity.ToadData(type);
		}
		this.setVariant(type);
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
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
		this.goalSelector.add(9, new StopAndLookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
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
			return this.hasCustomer() ? MubbleSounds.ENTITY_TOAD_TRADE : MubbleSounds.ENTITY_TOAD_AMBIENT;
		}
	}

	@Override
	public SoundEvent getYesSound() {
		return MubbleSounds.ENTITY_TOAD_YES;
	}

	@Override
	protected SoundEvent getTradingSound(boolean sold) {
		return sold ? SoundEvents.ENTITY_VILLAGER_YES : SoundEvents.ENTITY_VILLAGER_NO;
	}

	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions size) {
		return this.isBaby() ? size.height * 0.85F : size.height * 0.77F;
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
	public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
		ToadEntity child = new ToadEntity(MubbleEntities.TOAD, this.world);
		child.setVariant(this.random.nextFloat() < 0.5f ? ((ToadEntity) (mate)).getVariant() : this.getVariant());
		return child;
	}

	@Environment(EnvType.CLIENT)
	public Vec3d method_29919() {
		return new Vec3d(0.0D, 0.6F * this.getStandingEyeHeight(), this.getWidth() * 0.4F);
	}

	public static boolean canSpawn(EntityType<ToadEntity> entity, WorldAccess world, SpawnReason reason, BlockPos pos, Random rand) {
		return true;
	}
	// TRADING STUFF

	@Override
	public boolean isLeveledTrader() {
		return false;
	}

	@Override
	protected void fillRecipes() {
		TraderOfferList traderOfferList = this.getOffers();
		for(Pair<TradeOffers.Factory[], Integer> tradeEntry : this.getVariant().getTrades()) {
			if(tradeEntry.getLeft() != null) {
				this.fillRecipesFromPool(traderOfferList, tradeEntry.getLeft(), tradeEntry.getRight());
			}
		}
	}

	@Override
	protected Text getDefaultName() {
		return new TranslatableText(this.getType().getTranslationKey() + '.' + this.getVariant().getName().replace("/", "."));
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		if(itemStack.getItem() != MubbleItems.TOAD_SPAWN_EGG && this.isAlive() && !this.hasCustomer()) {
			if(this.isBaby()) {
				this.sayNo();
				return ActionResult.success(this.world.isClient);
			}
			else {
				boolean bl = this.getOffers().isEmpty();
				if(hand == Hand.MAIN_HAND) {
					if(bl && !this.world.isClient) {
						this.sayNo();
					}
					player.incrementStat(MubbleStats.TALKED_TO_TOAD);
				}
				if(bl) {
					return ActionResult.success(this.world.isClient);
				}
				else {
					if(!this.world.isClient && !this.offers.isEmpty()) {
						this.setCurrentCustomer(player);
						this.sendOffers(player, this.getDisplayName(), 1);
					}
					return ActionResult.success(this.world.isClient);
				}
			}
		}
		else {
			return super.interactMob(player, hand);
		}
	}

	private void sayNo() {
		this.setHeadRollingTimeLeft(40);
		if(!this.world.isClient()) {
			this.playSound(MubbleSounds.ENTITY_TOAD_NO, this.getSoundVolume(), this.getSoundPitch());
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
			super(false);
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
		CAPTAIN_TOAD(16, "brigade/captain_toad"),
		BRIGADIER_HINT_TOAD(17, "brigade/hint_toad"),
		BRIGADIER_BANKTOAD(18, "brigade/banktoad"),
		BRIGADIER_YELLOW(19, "brigade/yellow"),
		BRIGADIER_MAILTOAD(20, "brigade/mailtoad"),
		PARTY(21, "party"),
		KISEKAE(22, "kisekae");

		private final int index;
		private final String name;
		private final List<Pair<TradeOffers.Factory[], Integer>> trades;
		private static final ToadEntity.Type[] typeList = Arrays.stream(values()).sorted(Comparator.comparingInt(ToadEntity.Type::getIndex)).toArray((index) ->
		{
			return new ToadEntity.Type[index];
		});
		private static final Map<String, ToadEntity.Type> TYPES_BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(ToadEntity.Type::getName, (type) ->
		{
			return type;
		}));

		Type(int index, String name, Pair<TradeOffers.Factory[], Integer>... tradeEntries) {
			this.index = index;
			this.name = name;
			this.trades = Arrays.asList(tradeEntries);
		}

		Type(int index, String name) {
			this.index = index;
			this.name = name;
			this.trades = Arrays.asList(
					new Pair<>(ToadTradeOffers.COIN_TRADES, 3),
					new Pair<>(ToadTradeOffers.PRIMARY_COSTUMES_TRADES, 3),
					new Pair<>(ToadTradeOffers.SECONDARY_COSTUMES_TRADES, 1));
		}

		public String getName() {
			return this.name;
		}

		public int getIndex() {
			return this.index;
		}

		public List<Pair<TradeOffers.Factory[], Integer>> getTrades() {
			return this.trades;
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