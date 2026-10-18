package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.core.component.ZeldaDataComponents;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.network.syncher.ZeldaEntityDataSerializers;
import fr.hugman.mubble.zelda.references.LoftwingVariantIds;
import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/**
 * The giant bird the people of Skyloft ride. It lives above the clouds and, unlike every other
 * tamable animal, it is the one that picks its owner.
 */
public class Loftwing extends TamableAnimal {
	/** On the ground, wings folded: tall and narrow. */
	public static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.scalable(1.5F, 2.5F).withEyeHeight(2.2F);
	/** In the air, wings spread: flat and wide, with room for a rider on top. */
	public static final EntityDimensions FLYING_DIMENSIONS = EntityDimensions.scalable(4.0F, 1.0F).withEyeHeight(0.7F);
	/** Startled, wings beating: as wide as in flight and as tall as on the ground. */
	public static final EntityDimensions PANICKING_DIMENSIONS = EntityDimensions.scalable(4.0F, 2.5F).withEyeHeight(2.2F);

	/** One in twenty naturally spawned loftwings brings a chick along. */
	public static final float BABY_SPAWN_CHANCE = 0.05F;

	private static final EntityDataAccessor<Holder<LoftwingVariant>> VARIANT = SynchedEntityData.defineId(Loftwing.class, ZeldaEntityDataSerializers.LOFTWING_VARIANT);
	private static final EntityDataAccessor<Byte> LOFTWING_FLAGS = SynchedEntityData.defineId(Loftwing.class, EntityDataSerializers.BYTE);

	private static final int FLYING_FLAG = 1;
	private static final int PANICKING_FLAG = 2;

	public final AnimationState flapAnimationState = new AnimationState();

	public Loftwing(EntityType<? extends Loftwing> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return TamableAnimal.createAnimalAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.MOVEMENT_SPEED, 0.2)
				.add(Attributes.FLYING_SPEED, 0.6)
				.add(Attributes.FOLLOW_RANGE, 32.0)
				.add(Attributes.ATTACK_DAMAGE, 1.0)
				.add(Attributes.STEP_HEIGHT, 1.0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new LoftwingPanicGoal(this, 1.25));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
	}

	// SHAPE

	@Override
	protected EntityDimensions getDefaultDimensions(Pose pose) {
		EntityDimensions dimensions;
		if (this.isPanicking()) {
			dimensions = PANICKING_DIMENSIONS;
		} else if (pose == Pose.FALL_FLYING) {
			dimensions = FLYING_DIMENSIONS;
		} else {
			dimensions = STANDING_DIMENSIONS;
		}
		return dimensions.scale(this.getAgeScale());
	}

	// SOUNDS

	@Override
	protected SoundEvent getAmbientSound() {
		return ZeldaSounds.LOFTWING_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return ZeldaSounds.LOFTWING_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ZeldaSounds.LOFTWING_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(ZeldaSounds.LOFTWING_STEP, 0.3F, 1.0F);
	}

	// BREEDING

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
		Loftwing chick = ZeldaEntityTypes.LOFTWING.create(level, EntitySpawnReason.BREEDING);
		if (chick != null) {
			chick.setVariant(this.getVariant());
		}
		return chick;
	}

	// SPAWNING

	/** Loftwings keep to the sky: they only ever spawn on solid ground above the clouds. */
	public static boolean checkLoftwingSpawnRules(EntityType<Loftwing> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		return pos.getY() >= LoftwingSky.cloudLevel(level, pos) && checkAnimalSpawnRules(type, level, reason, pos, random);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
		LoftwingVariant.pickAtRandom(this.registryAccess().lookupOrThrow(ZeldaRegistries.LOFTWING_VARIANT), this.random).ifPresent(this::setVariant);
		if (groupData == null) {
			groupData = new AgeableMob.AgeableMobGroupData(BABY_SPAWN_CHANCE);
		}
		return super.finalizeSpawn(level, difficulty, reason, groupData);
	}

	// DATA TRACKER

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(LOFTWING_FLAGS, (byte) 0);
		entityData.define(VARIANT, VariantUtils.getDefaultOrAny(this.registryAccess(), LoftwingVariantIds.BLUE));
	}

	private void setLoftwingFlag(int mask, boolean value) {
		byte flags = this.entityData.get(LOFTWING_FLAGS);
		this.entityData.set(LOFTWING_FLAGS, (byte) (value ? flags | mask : flags & ~mask));
	}

	private boolean hasLoftwingFlag(int mask) {
		return (this.entityData.get(LOFTWING_FLAGS) & mask) != 0;
	}

	public boolean isFlying() {
		return this.hasLoftwingFlag(FLYING_FLAG);
	}

	public void setFlying(boolean flying) {
		if (this.isFlying() == flying) {
			return;
		}
		this.setLoftwingFlag(FLYING_FLAG, flying);
		this.setPose(flying ? Pose.FALL_FLYING : Pose.STANDING);
	}

	public boolean isPanicking() {
		return this.hasLoftwingFlag(PANICKING_FLAG);
	}

	public void setPanicking(boolean panicking) {
		this.setLoftwingFlag(PANICKING_FLAG, panicking);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		if (LOFTWING_FLAGS.equals(accessor)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(accessor);
	}

	// VARIANT

	public Holder<LoftwingVariant> getVariant() {
		return this.entityData.get(VARIANT);
	}

	public void setVariant(Holder<LoftwingVariant> variant) {
		this.entityData.set(VARIANT, variant);
	}

	public Identifier getTexture() {
		return this.getVariant().value().assetInfo().texture().texturePath();
	}

	@Override
	protected Component getTypeName() {
		return this.getVariant().value().name().orElseGet(super::getTypeName);
	}

	@Nullable
	@Override
	public <T> T get(DataComponentType<? extends T> type) {
		return type == ZeldaDataComponents.LOFTWING_VARIANT ? castComponentValue((DataComponentType<T>) type, this.getVariant()) : super.get(type);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		this.applyImplicitComponentIfPresent(components, ZeldaDataComponents.LOFTWING_VARIANT);
		super.applyImplicitComponents(components);
	}

	@Override
	protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
		if (type == ZeldaDataComponents.LOFTWING_VARIANT) {
			this.setVariant(castComponentValue(ZeldaDataComponents.LOFTWING_VARIANT, value));
			return true;
		}
		return super.applyImplicitComponent(type, value);
	}

	// NBT DATA

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		VariantUtils.writeVariant(output, this.getVariant());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		VariantUtils.readVariant(input, ZeldaRegistries.LOFTWING_VARIANT).ifPresent(this::setVariant);
	}
}
