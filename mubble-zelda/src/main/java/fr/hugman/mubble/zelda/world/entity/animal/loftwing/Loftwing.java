package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.core.component.ZeldaDataComponents;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.network.syncher.ZeldaEntityDataSerializers;
import fr.hugman.mubble.zelda.references.LoftwingVariantIds;
import fr.hugman.mubble.zelda.references.ZeldaDamageTypeIds;
import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import fr.hugman.mubble.zelda.world.item.component.LoftwingHelmetComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The giant bird the people of Skyloft ride. It lives above the clouds and, unlike every other
 * tamable animal, it is the one that picks its owner.
 */
public class Loftwing extends TamableAnimal {
	/** On the ground, wings folded: tall and narrow, with the rider sitting high on its back. */
	public static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.scalable(1.5F, 2.5F).withEyeHeight(2.2F)
			.withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, 2.3F, 0.0F));
	/** In the air, wings spread: flat and wide, the rider lying low over it. */
	public static final EntityDimensions FLYING_DIMENSIONS = EntityDimensions.scalable(4.0F, 1.0F).withEyeHeight(0.7F)
			.withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, 0.9F, 0.0F));
	/** Startled, wings beating: as wide as in flight and as tall as on the ground. */
	public static final EntityDimensions PANICKING_DIMENSIONS = EntityDimensions.scalable(4.0F, 2.5F).withEyeHeight(2.2F);

	/** One in twenty naturally spawned loftwings brings a chick along. */
	public static final float BABY_SPAWN_CHANCE = 0.05F;

	private static final EntityDataAccessor<Holder<LoftwingVariant>> VARIANT = SynchedEntityData.defineId(Loftwing.class, ZeldaEntityDataSerializers.LOFTWING_VARIANT);
	private static final EntityDataAccessor<Byte> LOFTWING_FLAGS = SynchedEntityData.defineId(Loftwing.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Integer> STAMINA = SynchedEntityData.defineId(Loftwing.class, EntityDataSerializers.INT);

	private static final int FLYING_FLAG = 1;
	private static final int PANICKING_FLAG = 2;
	private static final int ROOSTING_FLAG = 4;
	private static final int PREENING_FLAG = 8;

	/** How long a loftwing keeps shaking the water off after a dip. */
	private static final int DRYING_TICKS = 300;

	/** How far a loftwing will size someone up from. */
	private static final double BOND_RANGE = 8.0;
	/** How often it makes up its mind about the people around it. */
	private static final int BOND_INTERVAL = 20;
	/** One meeting in four ends in a bond; the other three end in a refusal that sticks. */
	private static final int BOND_CHANCE = 4;
	/** How many refusals it keeps in mind, oldest forgotten first, so the save data stays bounded. */
	public static final int REJECTED_MEMORY = 32;

	private static final String REJECTED_KEY = "rejected_players";

	/** How far a rider may have drifted and still be put back in the saddle. */
	private static final double CLING_RANGE_SQR = 16.0;

	/** How long a bird keeps flying towards a whistle before it gives up on finding its owner. */
	private static final int CALL_TICKS = 600;

	public final AnimationState flapAnimationState = new AnimationState();
	public final AnimationState roostAnimationState = new AnimationState();
	public final AnimationState preenAnimationState = new AnimationState();

	private int dryingTicks;

	/** Everyone this bird has already turned down. Insertion order is what makes the cap a queue. */
	private final Set<UUID> rejectedPlayers = new LinkedHashSet<>();
	private int bondCooldown;

	private int dashTicks;
	private int dashCooldown;
	private int glideTicks;
	/** Someone who tried to get off mid-flight and is about to find themselves back in the saddle. */
	@Nullable
	private LivingEntity clingingRider;

	/** The owner who last blew the whistle, and how long the bird keeps looking for them. */
	@Nullable
	private Player caller;
	private int callTicks;

	public Loftwing(EntityType<? extends Loftwing> type, Level level) {
		super(type, level);
		this.moveControl = new FlyingMoveControl(this, 10, false);
		// Gear a player fitted comes back when the bird dies, like a horse's saddle.
		this.setDropChance(EquipmentSlot.SADDLE, 1.0F);
		this.setDropChance(EquipmentSlot.HEAD, 1.0F);
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
		this.goalSelector.addGoal(0, new LoftwingAnswerCallGoal(this));
		this.goalSelector.addGoal(1, new LoftwingPanicGoal(this, 1.25));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.1, stack -> stack.is(ZeldaItems.MUSHROOM_SPORES_BOTTLE), false));
		// The errands that take a loftwing below the clouds, in the order it drops everything for them.
		this.goalSelector.addGoal(2, new LoftwingRoostGoal(this));
		this.goalSelector.addGoal(3, new LoftwingFishGoal(this));
		this.goalSelector.addGoal(4, new LoftwingPreenGoal(this));
		this.goalSelector.addGoal(5, new LoftwingCuriosityGoal(this));
		// And what it does the rest of the time, which is most of the time.
		this.goalSelector.addGoal(6, new LoftwingSoarGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

		// It defends itself and nothing else: it never joins a fight its owner picked.
		this.goalSelector.addGoal(2, new LoftwingWingFlapGoal(this));
		this.goalSelector.addGoal(5, new LoftwingLostGoal(this));
		this.goalSelector.addGoal(5, new LoftwingGiftGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
		navigation.setCanOpenDoors(false);
		navigation.setCanFloat(true);
		return navigation;
	}

	/** A bird does not hurt itself landing. Crashing into a wall under a rider is another matter. */
	@Override
	protected void checkFallDamage(double fallDistance, boolean onGround, BlockState state, BlockPos pos) {
	}

	@Override
	protected boolean isFlapping() {
		return this.isFlying();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.isInWater()) {
			this.dryingTicks = DRYING_TICKS;
		} else if (this.dryingTicks > 0) {
			this.dryingTicks--;
		}
		// Left to itself a loftwing has its wings out whenever its feet are off the ground; under a
		// rider, powered flight starts and ends on the jump button instead.
		if (!this.level().isClientSide() && this.getControllingPassenger() == null) {
			this.setFlying(!this.onGround() && !this.isInWater());
		}
	}

	/** Whether the bird is its own master right now, and free to go about its day. */
	public boolean isFreeToRoam() {
		return !this.isOrderedToSit() && !this.isLeashed() && !this.isVehicle() && !this.isPassenger() && !this.isBaby();
	}

	/** Whether this loftwing would fly down to take a closer look at {@code player}. */
	public boolean isCuriousAbout(Player player) {
		return !this.isOwnedBy(player) && !player.isSpectator() && !this.hasRejected(player);
	}

	public boolean isDrying() {
		return this.dryingTicks > 0;
	}

	// GEAR

	/** The belt and the helmet, and nothing else: a loftwing is not a mob that picks up armour. */
	@Override
	public boolean canUseSlot(EquipmentSlot slot) {
		return slot == EquipmentSlot.SADDLE || slot == EquipmentSlot.HEAD;
	}

	@Override
	protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
		return this.canUseSlot(slot);
	}

	/** Whether this bird is wearing the belt that lets its owner ride it. */
	public boolean hasBelt() {
		return !this.getItemBySlot(EquipmentSlot.SADDLE).isEmpty();
	}

	/** What the helmet it wears is worth, or what a bare head is worth when there is none. */
	public LoftwingHelmetComponent getHelmetSettings() {
		LoftwingHelmetComponent helmet = this.getItemBySlot(EquipmentSlot.HEAD).get(ZeldaDataComponents.LOFTWING_HELMET);
		return helmet != null ? helmet : LoftwingHelmetComponent.BAREHEADED;
	}

	// RIDING

	/** Only its owner, and only with the belt on. Anyone else is just a passenger it ignores. */
	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		return this.hasBelt() && this.getFirstPassenger() instanceof LivingEntity rider && this.isOwnedBy(rider) ? rider : null;
	}

	@Override
	public boolean isFlyingVehicle() {
		return this.isFlying();
	}

	@Override
	protected float getRiddenSpeed(Player rider) {
		return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
	}

	/**
	 * On the ground the rider steers like any mount. In the air the bird drives itself: it never
	 * stops going forward, and the only say the rider has is where to point it.
	 */
	@Override
	protected Vec3 getRiddenInput(Player rider, Vec3 travelVector) {
		return this.isFlying() ? Vec3.ZERO : new Vec3(rider.xxa * 0.5F, 0.0, rider.zza);
	}

	@Override
	protected void tickRidden(Player rider, Vec3 travelVector) {
		super.tickRidden(rider, travelVector);
		Input input = rider instanceof ServerPlayer serverRider ? serverRider.getLastClientInput() : Input.EMPTY;

		if (!this.isFlying()) {
			this.tickOnFoot(rider, input);
			return;
		}
		this.aimWithRider(rider);
		this.tickDash(input);
		this.flyForward();
		this.checkForCrash(rider);
	}

	/** Walking under the reins, and the second jump that throws the bird into the air. */
	private void tickOnFoot(Player rider, Input input) {
		this.setRot(rider.getYRot(), rider.getXRot() * 0.5F);
		this.yBodyRot = this.getYRot();
		this.yHeadRot = this.getYRot();
		if (!input.jump()) {
			return;
		}
		if (this.onGround()) {
			this.setJumping(true);
		} else {
			this.setFlying(true);
			this.setNoGravity(true);
			this.playSound(ZeldaSounds.LOFTWING_FLAP, 1.0F, 1.0F);
		}
	}

	/** The bird comes round to where the rider is looking, no faster than its helmet allows. */
	private void aimWithRider(Player rider) {
		float maxTurn = this.getHelmetSettings().maxTurnPerTick();
		float yaw = Mth.approachDegrees(this.getYRot(), rider.getYRot(), maxTurn);
		float pitch = Mth.approachDegrees(this.getXRot(), Mth.clamp(rider.getXRot(), -60.0F, 60.0F), maxTurn);
		if (this.getStamina() <= 0) {
			// Out of wingbeats: it can still glide down, but it cannot pull up any more.
			pitch = Math.max(pitch, 0.0F);
		}
		this.setRot(yaw, pitch);
		this.yBodyRot = yaw;
		this.yHeadRot = yaw;
	}

	private void tickDash(Input input) {
		if (this.dashCooldown > 0) {
			this.dashCooldown--;
		}
		if (this.dashTicks > 0) {
			this.dashTicks--;
			return;
		}
		if (input.shift() && this.dashCooldown <= 0 && this.getStamina() >= LoftwingFlight.DASH_COST * LoftwingFlight.DASH_TICKS) {
			this.dashTicks = LoftwingFlight.DASH_TICKS;
			this.dashCooldown = LoftwingFlight.DASH_COOLDOWN;
			this.playSound(ZeldaSounds.LOFTWING_FLAP, 1.0F, 1.2F);
		}
	}

	/**
	 * Forward, always, along whatever the bird is pointing at. Climbing costs wingbeats and gliding
	 * gives them back, so a rider who keeps pulling up eventually has to level out.
	 */
	private void flyForward() {
		Vec3 heading = this.getLookAngle();
		double speed = this.isDashing() ? LoftwingFlight.DASH_SPEED : LoftwingFlight.CRUISE_SPEED;
		if (heading.y() > 0.05) {
			speed *= LoftwingFlight.CLIMB_FACTOR;
			this.spendStamina(LoftwingFlight.CLIMB_COST);
		} else if (heading.y() < -0.05) {
			speed *= LoftwingFlight.DIVE_FACTOR;
			this.recoverStamina();
		} else {
			this.recoverStamina();
		}
		if (this.isDashing()) {
			this.spendStamina(LoftwingFlight.DASH_COST);
		}
		this.setDeltaMovement(heading.scale(speed));
	}

	public boolean isDashing() {
		return this.dashTicks > 0;
	}

	public int getStamina() {
		return this.entityData.get(STAMINA);
	}

	private void setStamina(int stamina) {
		this.entityData.set(STAMINA, Mth.clamp(stamina, 0, LoftwingFlight.MAX_STAMINA));
	}

	private void spendStamina(int cost) {
		this.glideTicks = 0;
		this.setStamina(this.getStamina() - cost);
	}

	private void recoverStamina() {
		if (++this.glideTicks >= LoftwingFlight.GLIDE_INTERVAL) {
			this.glideTicks = 0;
			this.setStamina(this.getStamina() + LoftwingFlight.GLIDE_GAIN);
		}
	}

	/** Flying into a wall throws the rider clear and hurts them both, the bird less the better its helmet. */
	private void checkForCrash(Player rider) {
		if (!this.horizontalCollision || this.getDeltaMovement().horizontalDistance() < LoftwingFlight.CRASH_SPEED) {
			return;
		}
		if (!(this.level() instanceof ServerLevel level)) {
			return;
		}
		DamageSource source = this.damageSources().source(ZeldaDamageTypeIds.LOFTWING_CRASH, this);
		Vec3 recoil = this.getLookAngle().scale(-LoftwingFlight.CRASH_EJECT_SPEED).add(0.0, LoftwingFlight.CRASH_EJECT_SPEED, 0.0);

		this.playSound(ZeldaSounds.LOFTWING_CRASH, 1.5F, 1.0F);
		this.stopFlying();
		rider.stopRiding();
		this.clingingRider = null;
		rider.push(recoil.x(), recoil.y(), recoil.z());
		rider.hurtServer(level, source, LoftwingFlight.CRASH_DAMAGE_TO_RIDER);

		float padded = LoftwingFlight.CRASH_DAMAGE_TO_LOFTWING - this.getHelmetSettings().crashProtection();
		if (padded > 0.0F) {
			this.hurtServer(level, source, padded);
		}
	}

	private void stopFlying() {
		this.setFlying(false);
		this.setNoGravity(false);
		this.dashTicks = 0;
		this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
	}

	/**
	 * Nobody steps off a loftwing in mid-air. Whoever tries is back on its back the very next tick,
	 * which is what the belt is for.
	 */
	@Override
	protected void removePassenger(Entity passenger) {
		boolean wasFlying = this.isFlying();
		super.removePassenger(passenger);
		if (wasFlying && !this.level().isClientSide() && passenger instanceof LivingEntity rider && rider.isAlive() && this.isOwnedBy(rider)) {
			this.clingingRider = rider;
		}
	}

	// THE BOND

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			return;
		}
		if (this.clingingRider != null) {
			LivingEntity rider = this.clingingRider;
			this.clingingRider = null;
			if (this.isFlying() && rider.isAlive() && rider.distanceToSqr(this) < CLING_RANGE_SQR) {
				rider.startRiding(this);
			}
		}
		boolean ridden = this.getControllingPassenger() != null;
		if (ridden) {
			// Touching down ends powered flight; the rider has to jump again to take off.
			if (this.isFlying() && this.onGround()) {
				this.stopFlying();
			}
		} else if (this.isNoGravity()) {
			// The rider is gone: the bird goes back to flying under its own weight.
			this.setNoGravity(false);
			this.dashTicks = 0;
		}
		if (this.caller != null && --this.callTicks <= 0) {
			this.clearCall();
		}
		if (!this.isTame() && --this.bondCooldown <= 0) {
			this.bondCooldown = BOND_INTERVAL;
			this.lookForSomeoneToBondWith();
		}
	}

	/**
	 * Nobody tames a loftwing. The bird sizes up whoever comes near, and either takes to them on
	 * the spot or turns them down for good: there is no feeding it until it gives in.
	 */
	private void lookForSomeoneToBondWith() {
		for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(BOND_RANGE), this::mayJudge)) {
			if (this.random.nextInt(BOND_CHANCE) == 0) {
				this.bondWith(player);
				return;
			}
			this.reject(player);
		}
	}

	private boolean mayJudge(Player player) {
		return player.isAlive() && !player.isSpectator() && !this.hasRejected(player);
	}

	/**
	 * Whistling is the bird's own call, and it teaches it to the person it picked. Losing the
	 * whistle is no disaster: one can be made again.
	 */
	private void giveWhistle(Player player) {
		if (!player.getInventory().contains(stack -> stack.is(ZeldaItems.LOFTWING_WHISTLE))) {
			player.getInventory().placeItemBackInInventory(new ItemStack(ZeldaItems.LOFTWING_WHISTLE));
		}
	}

	// ANSWERING THE WHISTLE

	/** Whether this bird is in any state to come when called. */
	public boolean answerCall(Player player) {
		if (!this.isOwnedBy(player) || this.isLeashed() || this.isVehicle() || this.isBaby()) {
			return false;
		}
		this.caller = player;
		this.callTicks = CALL_TICKS;
		this.setOrderedToSit(false);
		this.setRoosting(false);
		this.playSound(ZeldaSounds.LOFTWING_CALL, 2.0F, 1.0F);
		return true;
	}

	@Nullable
	public Player getCaller() {
		return this.caller;
	}

	public void clearCall() {
		this.caller = null;
		this.callTicks = 0;
	}

	public void bondWith(Player player) {
		this.tame(player);
		this.giveWhistle(player);
		this.setOrderedToSit(false);
		this.setTarget(null);
		this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
		this.playSound(ZeldaSounds.LOFTWING_BOND, 1.0F, 1.0F);
	}

	/** Turns a player down, and remembers it. The oldest refusal is forgotten to keep the list short. */
	public void reject(Player player) {
		this.rejectedPlayers.add(player.getUUID());
		var refusals = this.rejectedPlayers.iterator();
		while (this.rejectedPlayers.size() > REJECTED_MEMORY && refusals.hasNext()) {
			refusals.next();
			refusals.remove();
		}
		this.level().broadcastEntityEvent(this, EntityEvent.TAMING_FAILED);
	}

	public boolean hasRejected(Player player) {
		return this.rejectedPlayers.contains(player.getUUID());
	}

	/** A loftwing that has lost its owner calls out for them. It never blinks across the world. */
	@Override
	public boolean shouldTryTeleportToOwner() {
		return false;
	}

	/**
	 * Crouch to sit the bird down or stand it up; otherwise climb on, if it is wearing its belt.
	 * Fitting the belt and the helmet goes through the equipment handling of {@code super} first.
	 */
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		InteractionResult result = super.mobInteract(player, hand);
		if (result.consumesAction() || !this.isOwnedBy(player)) {
			return result;
		}
		if (!player.isSecondaryUseActive() && this.hasBelt() && !this.isVehicle() && !this.isBaby()) {
			if (!this.level().isClientSide()) {
				this.setOrderedToSit(false);
				player.startRiding(this);
			}
			return InteractionResult.SUCCESS;
		}
		if (!this.level().isClientSide()) {
			this.setOrderedToSit(!this.isOrderedToSit());
			this.jumping = false;
			this.getNavigation().stop();
			this.setTarget(null);
		}
		return InteractionResult.SUCCESS;
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

	/** Mushroom spores, the way Orielle nurses hers back to health in Skyward Sword. */
	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(ZeldaItems.MUSHROOM_SPORES_BOTTLE);
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
		entityData.define(STAMINA, LoftwingFlight.MAX_STAMINA);
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

	/** Settled on a perch for the night. */
	public boolean isRoosting() {
		return this.hasLoftwingFlag(ROOSTING_FLAG);
	}

	public void setRoosting(boolean roosting) {
		this.setLoftwingFlag(ROOSTING_FLAG, roosting);
	}

	/** Standing still, running its beak through its feathers. */
	public boolean isPreening() {
		return this.hasLoftwingFlag(PREENING_FLAG);
	}

	public void setPreening(boolean preening) {
		this.setLoftwingFlag(PREENING_FLAG, preening);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		if (LOFTWING_FLAGS.equals(accessor)) {
			this.refreshDimensions();
			animate(this.flapAnimationState, this.isFlying(), this.tickCount);
			animate(this.roostAnimationState, this.isRoosting(), this.tickCount);
			animate(this.preenAnimationState, this.isPreening(), this.tickCount);
		}
		super.onSyncedDataUpdated(accessor);
	}

	private static void animate(AnimationState state, boolean running, int tickCount) {
		if (running) {
			state.startIfStopped(tickCount);
		} else {
			state.stop();
		}
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
		if (!this.rejectedPlayers.isEmpty()) {
			output.store(REJECTED_KEY, UUIDUtil.CODEC_LINKED_SET, this.rejectedPlayers);
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		VariantUtils.readVariant(input, ZeldaRegistries.LOFTWING_VARIANT).ifPresent(this::setVariant);
		this.rejectedPlayers.clear();
		input.read(REJECTED_KEY, UUIDUtil.CODEC_LINKED_SET).ifPresent(this.rejectedPlayers::addAll);
	}
}
