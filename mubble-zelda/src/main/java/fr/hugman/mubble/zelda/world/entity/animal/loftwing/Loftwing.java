package fr.hugman.mubble.zelda.world.entity.animal.loftwing;

import fr.hugman.mubble.zelda.core.component.ZeldaDataComponents;
import fr.hugman.mubble.zelda.core.registries.ZeldaRegistries;
import fr.hugman.mubble.zelda.network.syncher.ZeldaEntityDataSerializers;
import fr.hugman.mubble.zelda.references.LoftwingVariantIds;
import fr.hugman.mubble.zelda.references.ZeldaDamageTypeIds;
import fr.hugman.mubble.zelda.sounds.ZeldaSounds;
import fr.hugman.mubble.zelda.world.entity.ZeldaEntityTypes;
import fr.hugman.mubble.zelda.world.item.ZeldaItems;
import fr.hugman.mubble.zelda.world.level.block.ZeldaBlocks;
import fr.hugman.mubble.zelda.world.item.component.LoftwingHelmetComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
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
	/** How fast the bird is going along its own nose, in blocks a tick. The state a dive fills and a climb spends. */
	private double flightSpeed;
	/** What is left of the last wingbeat's lift, in blocks a tick. */
	private double flapLift;
	private int flapCooldown;
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
		// While somebody is steering, nothing else turns the bird's head: the look goals and the
		// rider were both writing its pitch in the same tick, and the bird flew wherever the loser
		// of that race pointed it.
		this.lookControl = new LookControl(this) {
			@Override
			public void tick() {
				if (Loftwing.this.getControllingPassenger() == null) {
					super.tick();
				}
			}
		};
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
		// Chicks are too young to soar, so without these they would stand still their whole childhood.
		this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.1));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8));
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

	/**
	 * Whether the bird is its own master right now, and free to go about its day.
	 * <p>
	 * Chicks count. They hatch and spawn above the clouds like everyone else, so a chick that could
	 * only walk would be stranded on the peak it was born on; what it cannot do is carry a rider or
	 * answer a whistle.
	 */
	public boolean isFreeToRoam() {
		return !this.isOrderedToSit() && !this.isLeashed() && !this.isVehicle() && !this.isPassenger();
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

	/** A rider may sprint on a loftwing's back, since the sprint key is what asks it for a dash. */
	@Override
	public boolean canSprint() {
		return true;
	}

	/**
	 * On the ground the rider steers like any mount. In the air the bird drives itself: it never
	 * stops going forward, and the rider's say is where to point it, when to beat its wings and when
	 * to dash.
	 * <p>
	 * The rider's keys are read through {@link LivingEntity#isJumping()} and
	 * {@link Entity#isSprinting()} rather than through the server's copy of the input packet: a mount
	 * with a player aboard is simulated by that player's client, where no {@code ServerPlayer}
	 * exists, so anything read off one would be empty exactly where it matters.
	 */
	@Override
	protected Vec3 getRiddenInput(Player rider, Vec3 travelVector) {
		return this.isFlying() ? Vec3.ZERO : new Vec3(rider.xxa * 0.5F, 0.0, rider.zza);
	}

	@Override
	protected void tickRidden(Player rider, Vec3 travelVector) {
		super.tickRidden(rider, travelVector);
		if (this.flapCooldown > 0) {
			this.flapCooldown--;
		}
		if (!this.isFlying()) {
			this.tickOnFoot(rider);
			return;
		}
		this.aimWithRider(rider);
		this.tickDash(rider);
		this.tickWingbeats(rider);
		this.flyForward();
		this.checkForCrash(rider);
	}

	/**
	 * Walking under the reins, and the wingbeat that takes the bird off the ground.
	 * <p>
	 * Standing is also how a tired bird catches its breath. Without that, one that came down on an
	 * empty reserve could never beat its wings again, and nothing else gets a loftwing airborne.
	 */
	private void tickOnFoot(Player rider) {
		this.setRot(rider.getYRot(), rider.getXRot() * 0.5F);
		this.yBodyRot = this.getYRot();
		this.yHeadRot = this.getYRot();
		if (rider.isJumping() && this.canFlap()) {
			this.takeOff();
		} else {
			this.recoverStamina();
		}
	}

	/** A loftwing leaves the ground the way it gains height in the air: straight up, on its wings. */
	private void takeOff() {
		this.setFlying(true);
		this.flightSpeed = LoftwingFlight.MIN_SPEED;
		this.flap();
	}

	/**
	 * The bird comes round to where the rider is looking, and takes its time about it: the body and
	 * the nose have rates of their own, and speed widens a turn rather than tightening it, so one
	 * that has just dived out of the clouds banks in a long arc.
	 * <p>
	 * What the helmet allows is still the ceiling on both. Nothing here ever moves the nose on its
	 * own, whatever is left in the reserve: a loftwing does not stall, and the pitch is the rider's
	 * from the moment they leave the ground to the moment they are back on it.
	 */
	private void aimWithRider(Player rider) {
		float ceiling = this.getHelmetSettings().maxTurnPerTick();
		double overGlide = Math.max(0.0, this.flightSpeed - LoftwingFlight.MIN_SPEED);
		float yawRate = Math.min(ceiling, (float) (LoftwingFlight.YAW_DEGREES_PER_TICK / (1.0 + overGlide / LoftwingFlight.YAW_WIDENING_SPEED)));
		float pitchRate = Math.min(ceiling, LoftwingFlight.PITCH_DEGREES_PER_TICK);

		float wantedPitch = Mth.clamp(rider.getXRot(), -LoftwingFlight.MAX_CLIMB_PITCH, LoftwingFlight.MAX_DIVE_PITCH);
		float yaw = Mth.approachDegrees(this.getYRot(), rider.getYRot(), yawRate);
		float pitch = Mth.approachDegrees(this.getXRot(), wantedPitch, pitchRate);
		this.setRot(yaw, pitch);
		this.yBodyRot = yaw;
		this.yHeadRot = yaw;
	}

	/** The dash, on the sprint key: crouching is how vanilla gets off a mount, so it cannot be that. */
	private void tickDash(Player rider) {
		if (this.dashCooldown > 0) {
			this.dashCooldown--;
		}
		if (this.dashTicks > 0) {
			this.dashTicks--;
			this.spendStamina(LoftwingFlight.DASH_COST);
			return;
		}
		if (rider.isSprinting() && this.dashCooldown <= 0 && this.getStamina() >= LoftwingFlight.DASH_COST * LoftwingFlight.DASH_TICKS) {
			this.dashTicks = LoftwingFlight.DASH_TICKS;
			this.dashCooldown = LoftwingFlight.DASH_COOLDOWN;
			this.playSound(ZeldaSounds.LOFTWING_FLAP, 1.0F, 1.2F);
		}
	}

	/**
	 * The wingbeat, on the jump key: height at once, a little speed with it, for a slice of the
	 * reserve. It is the only thing that puts energy back into the flight model, which makes it both
	 * what lets a loftwing climb without end and the thing the rider runs out of.
	 * <p>
	 * A bird only catches its breath once the key is let go, rather than between one beat and the
	 * next: paying it back across the pause in the rhythm came to all but the whole of what a beat
	 * costs, and a rider could climb for ever on a reserve that never went down.
	 */
	private void tickWingbeats(Player rider) {
		if (rider.isJumping()) {
			if (this.canFlap()) {
				this.flap();
			}
		} else if (!this.isDashing()) {
			this.recoverStamina();
		}
	}

	private boolean canFlap() {
		return this.flapCooldown <= 0 && this.getStamina() >= LoftwingFlight.FLAP_COST;
	}

	private void flap() {
		this.flapLift = LoftwingFlight.FLAP_LIFT;
		this.flightSpeed = Math.min(this.flightSpeed + LoftwingFlight.FLAP_SPEED_GAIN, LoftwingFlight.MAX_SPEED);
		// Felt on the tick it happens, and only upwards: a beat buys height without taking the rider
		// off the heading they were already on, the way pulling the stick down does in Skyward Sword.
		Vec3 delta = this.getDeltaMovement();
		this.setDeltaMovement(delta.x(), LoftwingFlight.FLAP_LIFT, delta.z());
		this.spendStamina(LoftwingFlight.FLAP_COST);
		this.flapCooldown = LoftwingFlight.FLAP_INTERVAL;
		this.playSound(ZeldaSounds.LOFTWING_FLAP, 1.0F, 1.0F);
	}

	/**
	 * Forward along the bird's own nose, at the speed it has built up. Gravity works on that speed
	 * and not on the bird, so a dive spends height to buy speed and a climb spends the speed back:
	 * a rider who dives first climbs far higher than one who pulls up out of a glide, and keeps
	 * climbing until the dive is paid out.
	 * <p>
	 * The bird then leans onto the velocity it wants instead of being put on it. That velocity used
	 * to be assigned outright, which gave a bird that turned on a pin and carried nothing into a
	 * climb.
	 */
	private void flyForward() {
		// The y of a unit heading is the sine of the angle it climbs at, and so the share of gravity
		// that works along the bird's own nose.
		double climb = this.getLookAngle().y();
		this.flightSpeed -= LoftwingFlight.GRAVITY * climb + LoftwingFlight.DRAG * this.flightSpeed * this.flightSpeed;
		if (this.isDashing()) {
			this.flightSpeed += LoftwingFlight.DASH_THRUST;
		}
		this.flightSpeed = Mth.clamp(this.flightSpeed, LoftwingFlight.MIN_SPEED, LoftwingFlight.MAX_SPEED);

		this.setDeltaMovement(this.getDeltaMovement().lerp(this.getFlightVelocity(), LoftwingFlight.HEADING_INERTIA));
		this.flapLift *= LoftwingFlight.FLAP_LIFT_DECAY;
	}

	/**
	 * Where the flight model is taking the bird this tick: along its nose at the speed it holds, less
	 * the height a glide always costs, plus whatever is left of the last wingbeat.
	 * <p>
	 * This is the model's own answer, and the bird's velocity is no place to read it back from: under
	 * a rider that velocity is the business of the rider's client, and the server holds a zero.
	 */
	public Vec3 getFlightVelocity() {
		return this.getLookAngle().scale(this.flightSpeed).add(0.0, this.flapLift - LoftwingFlight.GLIDE_SINK, 0.0);
	}

	/** How fast the bird is going along its own nose, in blocks a tick. */
	public double getFlightSpeed() {
		return this.flightSpeed;
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
		this.dashTicks = 0;
		this.flightSpeed = 0.0;
		this.flapLift = 0.0;
		this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
	}

	/**
	 * Under a rider in powered flight the bird moves by the velocity {@link #flyForward()} just set,
	 * with nothing of the world's applied on top: the flight model brings its own gravity and its own
	 * drag.
	 * <p>
	 * This deliberately does not go through {@code setNoGravity}: that flag belongs to
	 * {@link FlyingMoveControl}, which turns it back off on every tick it is not steering, and the
	 * two fought each other into a bird that hopped on the spot.
	 */
	@Override
	public void travel(Vec3 travelVector) {
		if (this.isFlying() && this.getControllingPassenger() != null) {
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.calculateEntityAnimation(true);
			return;
		}
		super.travel(travelVector);
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
		// Touching down ends powered flight; the rider has to jump again to take off.
		if (this.isFlying() && this.onGround() && this.getControllingPassenger() != null) {
			this.stopFlying();
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
	 * <p>
	 * Anything the bird can wear is handed straight back to vanilla. {@code Player.interactOn} asks
	 * the entity first and only reaches its equip-on-interact handling if the entity did not take
	 * the interaction, so consuming it here would make the belt impossible to put on at all — and
	 * with no belt there is no riding.
	 */
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		InteractionResult result = super.mobInteract(player, hand);
		if (result.consumesAction() || !this.isOwnedBy(player)) {
			return result;
		}
		if (this.isGearForMe(player.getItemInHand(hand))) {
			return InteractionResult.PASS;
		}
		if (!player.isSecondaryUseActive() && this.hasBelt() && !this.isVehicle() && !this.isBaby()) {
			if (!this.level().isClientSide()) {
				this.setOrderedToSit(false);
				player.startRiding(this);
			}
			return InteractionResult.SUCCESS;
		}
		// Sitting is something you ask of a bird standing in front of you. Asking it of one on the
		// wing would stop everything steering it and drop it out of the sky.
		if (this.isFlying()) {
			return InteractionResult.PASS;
		}
		if (!this.level().isClientSide()) {
			this.setOrderedToSit(!this.isOrderedToSit());
			this.jumping = false;
			this.getNavigation().stop();
			this.setTarget(null);
		}
		return InteractionResult.SUCCESS;
	}

	/** Whether {@code stack} is a piece of gear vanilla would put on this bird for a right click. */
	private boolean isGearForMe(ItemStack stack) {
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		return equippable != null
				&& equippable.equipOnInteract()
				&& equippable.canBeEquippedBy(this.getType().builtInRegistryHolder())
				&& this.canUseSlot(equippable.slot());
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

	/**
	 * Loftwings are birds: a pair does not produce a chick on the spot, it leaves an egg behind,
	 * and the egg takes the best part of a month to come to anything.
	 */
	@Override
	public void spawnChildFromBreeding(ServerLevel level, Animal mate) {
		BlockPos nest = this.blockPosition();
		if (level.getBlockState(nest).canBeReplaced() && ZeldaBlocks.LOFTWING_EGG.defaultBlockState().canSurvive(level, nest)) {
			level.setBlockAndUpdate(nest, ZeldaBlocks.LOFTWING_EGG.defaultBlockState());
			level.playSound(null, nest, ZeldaSounds.LOFTWING_EGG_CRACK, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
		this.finalizeSpawnChildFromBreeding(level, mate, null);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
		// Never called in practice: the pair lays an egg instead. Here for anything that breeds
		// loftwings through another path, such as a command or another mod.
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
