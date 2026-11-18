package fr.hugman.mubble.mixin;

import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpChangePayload;
import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.Drilling;
import fr.hugman.mubble.world.entity.Floating;
import fr.hugman.mubble.world.entity.Fluttering;
import fr.hugman.mubble.world.entity.JumpKeyHolder;
import fr.hugman.mubble.world.entity.Launching;
import fr.hugman.mubble.world.entity.WaterRunner;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import fr.hugman.mubble.world.power_up.ability.DrillAbility;
import fr.hugman.mubble.world.power_up.ability.FloatAbility;
import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import fr.hugman.mubble.world.power_up.ability.LaunchAbility;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(Player.class)
public class PlayerMixin implements PowerUpHolder, WaterRunner, JumpKeyHolder, Fluttering, Floating, Launching, Drilling {
    @Unique
    private static final EntityDataAccessor<Optional<PowerUpProperties>> POWER_UP_PROPERTIES = SynchedEntityData.defineId(Player.class, MubbleEntityDataSerializers.POWER_UP_PROPERTIES);
    @Unique
    private static final EntityDataAccessor<Optional<Holder<PowerUp>>> POWER_UP = SynchedEntityData.defineId(Player.class, MubbleEntityDataSerializers.OPTIONAL_POWER_UP);
    /**
     * Only ever written by the server, and only so that the other clients can tell. The flutter of the
     * player a side is in charge of is simulated there rather than waited for, see {@link #mubble$tickFlutter}.
     */
    @Unique
    private static final EntityDataAccessor<Boolean> FLUTTERING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    /** The other half of the same story, see {@link #mubble$tickAirMoves}. */
    @Unique
    private static final EntityDataAccessor<Boolean> FLOATING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> LAUNCHING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> SLOW_FALLING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> DRILLING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private static final String POWER_UP_KEY = "power_up";
    @Unique
    private static final String POWER_UP_PROPERTIES_KEY = "power_up_properties";
    @Unique
    private static final String FLUTTER_TICKS_KEY = "flutter_ticks";
    @Unique
    private static final String FLUTTER_SPENT_KEY = "flutter_spent";
    @Unique
    private static final String LAUNCH_SPENT_KEY = "launch_spent";
    @Unique
    private static final String LAUNCHING_KEY = "launching";
    @Unique
    private static final String SLOW_FALLING_KEY = "slow_falling";
    @Unique
    private static final String DRILLING_KEY = "drilling";

    /** How many ticks a launch waits to be seen rising before its peak can be called, movement packets lagging. */
    @Unique
    private static final int LAUNCH_RISE_GRACE = 5;
    @Unique
    private static final int AIR_MOVE_BURST_PARTICLES = 12;
    @Unique
    private static final double AIR_MOVE_BURST_SPEED = 0.2D;

    /** How many particles a player on a mid-air move leaves around their feet every tick. */
    @Unique
    private static final int AIR_MOVE_PARTICLES = 2;
    /** How far those scatter around the feet, as a share of the width of the player. */
    @Unique
    private static final double AIR_MOVE_PARTICLE_SPREAD = 0.8D;
    /** How fast they sink, so that they read as being left behind rather than carried along. */
    @Unique
    private static final double AIR_MOVE_PARTICLE_FALL = -0.05D;

    /** How far a player runs between two splashes, in blocks. Vanilla footsteps land every 1.7 or so. */
    @Unique
    private static final double SPLASH_STEP = 0.8D;
    /** How many droplets one splash is made of. */
    @Unique
    private static final int SPLASH_PARTICLES = 5;
    /** How many of those go up in a spray instead of back in a trail. */
    @Unique
    private static final int SPLASH_RISING_PARTICLES = 2;
    /** The share of the player's speed the droplets keep, thrown back the way the foot came from. */
    @Unique
    private static final double SPLASH_KICK = 0.5D;
    /** How wide the droplets scatter on top of that, in blocks per tick. */
    @Unique
    private static final double SPLASH_SPREAD = 0.1D;

    /** Whether the sprint the player is on started on the ground, see {@link #mubble$tickRunOnWater}. */
    @Unique
    private boolean mubble$runningOnWater;
    /** How far the player has left to go before the next splash, see {@link #mubble$splashOnTheSurface}. */
    @Unique
    private double mubble$distanceToNextSplash;

    /** Whether a flutter is going on, as simulated by this side, see {@link #mubble$tickFlutter}. */
    @Unique
    private boolean mubble$fluttering;
    /** How many ticks the flutter under way has already run, which is what the lift ramps up over. */
    @Unique
    private int mubble$flutterTicks;
    /** Whether the jump the player is on has already spent its flutter. */
    @Unique
    private boolean mubble$flutterSpent;
    /** Whether the player is being held to a float, as simulated by this side. */
    @Unique
    private boolean mubble$floating;

    @Unique
    private boolean mubble$jumpKeyWasHeld;
    @Unique
    private boolean mubble$sneakKeyWasHeld;
    /** Ticks left for a second tap of the jump key to count as a double tap. */
    @Unique
    private int mubble$doubleTapTicks;
    @Unique
    private boolean mubble$launchSpent;
    @Unique
    private boolean mubble$launching;
    @Unique
    private int mubble$launchTicks;
    @Unique
    private boolean mubble$launchRose;
    @Unique
    private boolean mubble$slowFalling;
    @Unique
    private boolean mubble$drilling;
    /** What a client last drew a burst for, the launch taking off and the drill landing. */
    @Unique
    private boolean mubble$wasLaunching;
    @Unique
    private boolean mubble$wasDrilling;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void mubble$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(POWER_UP, Optional.empty());
        builder.define(POWER_UP_PROPERTIES, Optional.empty());
        builder.define(FLUTTERING, false);
        builder.define(FLOATING, false);
        builder.define(LAUNCHING, false);
        builder.define(SLOW_FALLING, false);
        builder.define(DRILLING, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void mubble$writeCustomData(ValueOutput view, CallbackInfo ci) {
        var this_ = (Player) ((Object) this);

		this_.getPowerUp().ifPresent(entry -> view.store(POWER_UP_KEY, PowerUp.CODEC, entry));
        view.storeNullable(POWER_UP_PROPERTIES_KEY, PowerUpProperties.CODEC, this_.getPowerUpProperties());
        view.putInt(FLUTTER_TICKS_KEY, this.mubble$fluttering ? this.mubble$flutterTicks : -1);
        view.putBoolean(FLUTTER_SPENT_KEY, this.mubble$flutterSpent);
        view.putBoolean(LAUNCH_SPENT_KEY, this.mubble$launchSpent);
        view.putBoolean(LAUNCHING_KEY, this.mubble$launching);
        view.putBoolean(SLOW_FALLING_KEY, this.mubble$slowFalling);
        view.putBoolean(DRILLING_KEY, this.mubble$drilling);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void mubble$readCustomData(ValueInput view, CallbackInfo ci) {
        var this_ = (Player) (Object) this;
		view.read(POWER_UP_KEY, PowerUp.CODEC).ifPresent(entry -> this_.getEntityData().set(POWER_UP, Optional.of(entry)));
        view.read(POWER_UP_PROPERTIES_KEY, PowerUpProperties.CODEC).ifPresent(properties -> this_.getEntityData().set(POWER_UP_PROPERTIES, Optional.of(properties)));
        // A flutter is written as the ticks it had run, and as -1 when there was none going on at all.
        int flutterTicks = view.getIntOr(FLUTTER_TICKS_KEY, -1);
        this.mubble$flutterSpent = view.getBooleanOr(FLUTTER_SPENT_KEY, false);
        this.mubble$setFluttering(this_, flutterTicks >= 0);
        this.mubble$flutterTicks = Math.max(0, flutterTicks);
        this.mubble$launchSpent = view.getBooleanOr(LAUNCH_SPENT_KEY, false);
        this.mubble$setLaunching(this_, view.getBooleanOr(LAUNCHING_KEY, false));
        this.mubble$setSlowFalling(this_, view.getBooleanOr(SLOW_FALLING_KEY, false));
        this.mubble$setDrilling(this_, view.getBooleanOr(DRILLING_KEY, false));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mubble$tick(CallbackInfo ci) {
        var this_ = (Player) (Object) this;
        if(this_.level().isClientSide()) {
            return;
        }
        this_.getPowerUp().ifPresent(entry -> {
            BlockPos pos = this_.blockPosition();
            if(entry.is(MubblePowerUpTags.LOST_TO_RAIN) && (this_.level().isRainingAt(pos) || this_.level().isRainingAt(BlockPos.containing(pos.getX(), this_.getBoundingBox().maxY, pos.getZ())))) {
                this_.clearPowerUp();
            }
            if(entry.is(MubblePowerUpTags.LOST_TO_WATER) && this_.isInWater()) {
                this_.clearPowerUp();
            }
            var properties = this.getPowerUpProperties();
            if(properties != null) {
                properties.tick();
                // safe check
                if(this_.tickCount % 20 == 0) {
                    properties.doSoftChecks(this_);
                }
                if(properties.checkDirty()) {
                    this_.getEntityData().set(POWER_UP_PROPERTIES, Optional.ofNullable(this_.getPowerUpProperties()), true);
                }
            }
        });
    }

    /**
     * Keeps track of whether the sprint the player is on can carry them over water, and leaves the
     * splashes of it behind.
     * <p>
     * Holding a power-up tagged {@code mubble:can_run_on_water} only opens the door: the sprint has
     * to have started on the ground and out of the water, and it is over as soon as the player
     * slows to a walk, runs into a wall or goes under. Leaving the ground is none of those, so a
     * jump keeps the run going and the player lands back on the surface.
     * <p>
     * Sneaking and using an item are what slowing down means here: the game keeps the sprint on
     * through either, but both cut the pace down to a walk, and nobody crosses a pond at that
     * speed. They only count with the ground under the player, though: in mid-air neither of them
     * slows anyone down, so neither is worth dropping a jumping runner in the water over.
     * <p>
     * A wall counts the way it does for a vanilla sprint, minor collisions aside: brushing past a
     * corner, or the step up the surface of the water sometimes is, should not drop anyone in.
     * <p>
     * This runs on both sides: the collision shape has to answer the same on the client that predicts
     * the movement and on the server that validates it, and both know everything the answer needs.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void mubble$tickRunOnWater(CallbackInfo ci) {
        var this_ = (Player) (Object) this;

        var allowed = this_.getPowerUp().map(entry -> entry.is(MubblePowerUpTags.CAN_RUN_ON_WATER)).orElse(false);
        var slowedToAWalk = this_.onGround() && (this_.isShiftKeyDown() || this_.isUsingItem());
        if (!allowed || !this_.isSprinting() || slowedToAWalk || (this_.horizontalCollision && !this_.minorHorizontalCollision)) {
            this.mubble$runningOnWater = false;
        } else if (this.mubble$runningOnWater) {
            // the surface carries the runner, so being dunked under it means the run is over
            if (this_.isUnderWater()) {
                this.mubble$runningOnWater = false;
            }
        } else {
            // swimming and jumping out of the water is not a start: the sprint has to come from land
            this.mubble$runningOnWater = this_.onGround() && !this_.isInWater();
        }

        this.mubble$splashOnTheSurface(this_);
    }

    /**
     * Leaves a splash behind every footstep of a run on the water.
     * <p>
     * The rhythm is a distance rather than a delay, so the trail keeps its spacing whatever the run
     * is worth, and each splash lands where the foot did, a tick back: most of its droplets thrown
     * the way it came from, a couple of them straight up. Only the ticks actually spent on the
     * surface count, so a jump leaves the trail where it was until the landing picks it back up.
     * <p>
     * Called on both sides like the rest of the run, and that is where it stops being shared:
     * {@code addParticle} does nothing on a server, so the effect only ever shows up on a client.
     */
    @Unique
    private void mubble$splashOnTheSurface(Player player) {
        if (!this.mubble$runningOnWater || !player.onGround()) {
            // the next footfall back on the surface splashes right away
            this.mubble$distanceToNextSplash = 0.0D;
            return;
        }

        var pos = player.getOnPos();
        var fluid = player.level().getFluidState(pos);
        if (!fluid.is(FluidTags.WATER)) {
            // the shore a run carries on over has nothing to splash
            this.mubble$distanceToNextSplash = 0.0D;
            return;
        }

        double movedX = player.getX() - player.xOld;
        double movedZ = player.getZ() - player.zOld;
        this.mubble$distanceToNextSplash -= Math.sqrt(movedX * movedX + movedZ * movedZ);
        if (this.mubble$distanceToNextSplash > 0.0D) {
            return;
        }
        this.mubble$distanceToNextSplash = SPLASH_STEP;

        var level = player.level();
        var random = player.getRandom();
        double surface = pos.getY() + fluid.getHeight(level, pos);
        double width = player.getBbWidth();
        for (int i = 0; i < SPLASH_PARTICLES; i++) {
            double x = player.xOld + (random.nextDouble() - 0.5D) * width;
            double z = player.zOld + (random.nextDouble() - 0.5D) * width;
            if (i < SPLASH_RISING_PARTICLES) {
                // a vertical speed of its own is the taller hop, straight up and barely sideways
                level.addParticle(ParticleTypes.SPLASH, x, surface, z, 0.0D, 1.0D, 0.0D);
            } else {
                // no vertical speed at all is the other one: the direction given, and a short hop
                level.addParticle(ParticleTypes.SPLASH, x, surface, z,
                        -movedX * SPLASH_KICK + (random.nextDouble() - 0.5D) * SPLASH_SPREAD,
                        0.0D,
                        -movedZ * SPLASH_KICK + (random.nextDouble() - 0.5D) * SPLASH_SPREAD);
            }
        }
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void mubble$aiStep(CallbackInfo ci) {
        var this_ = (Player) (Object) this;

        if (this_.getHealth() > 0.0F && !this_.isSpectator()) {
            AABB collectArea;
            if (this_.isPassenger() && !this_.getVehicle().isRemoved()) {
                collectArea = this_.getBoundingBox().minmax(this_.getVehicle().getBoundingBox());
            } else {
                collectArea = this_.getBoundingBox();
            }

            List<CollectibleEntity> collectibles = this_.level().getEntities(MubbleEntityTypes.COLLECTIBLE, collectArea, _ -> true);

            for (var collectible : collectibles) {
                if (!collectible.isRemoved()) {
                    collectible.collect(this_);
                }
            }
        }

        var powerUp = this_.getPowerUp();
        if(powerUp.isPresent()) {
            var particle = powerUp.get().value().cosmectics().particle();
            particle.ifPresent(particleOptions -> this_.level().addParticle(particleOptions, this_.getRandomX(0.6), this_.getRandomY(), this_.getRandomZ(0.6), 0.0, 0.0, 0.0));
        }
    }

    @Override
    public Optional<Holder<PowerUp>> getPowerUp() {
        var this_ = (Player) (Object) this;
        return this_.getEntityData().get(POWER_UP);
    }

    @Override
    @Nullable
    public PowerUpProperties getPowerUpProperties() {
        var this_ = (Player) (Object) this;
        return this_.getEntityData().get(POWER_UP_PROPERTIES).orElse(null);
    }

    @Override
    public void setPowerUpProperties(PowerUpProperties properties) {
        var this_ = (Player) (Object) this;
        this_.getEntityData().set(POWER_UP_PROPERTIES, Optional.ofNullable(properties));
    }

    @Override
    public void setPowerUp(Holder<PowerUp> powerUp) {
        var this_ = (Player) (Object) this;
        var previous = this_.getPowerUp();
        this_.getEntityData().set(POWER_UP, Optional.of(powerUp));
        if (this_ instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new PowerUpChangePayload(previous, Optional.of(powerUp)));
        }
        PowerUp.onChange(this_, previous, Optional.of(powerUp));
    }

    @Override
    public void clearPowerUp() {
        var this_ = (Player) (Object) this;
        var previous = this_.getPowerUp();
        this_.getEntityData().set(POWER_UP, Optional.empty());
        if (this_ instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new PowerUpChangePayload(previous, Optional.empty()));
        }
        PowerUp.onChange(this_, previous, Optional.empty());
    }

    /**
     * Runs the two halves of a jump held on: the flutter climbing past its peak, and the float bringing the
     * holder back down at a walking pace once nothing is lifting them any more.
     * <p>
     * They are checked in that order and never both in the same tick, since a holder still being pushed up
     * has no fall for the float to slow. Which of the two a power-up grants is up to it: a form with only a
     * float floats down from wherever its plain jump took it.
     * <p>
     * This sits at the head of {@code aiStep} because both write straight into the movement of the tick,
     * which {@code travel()} then spends a little further down the very same call.
     * <p>
     * Both sides run it, each for the player it is in charge of: the client so that the movement it predicts
     * for itself actually goes where it should, the server so that it knows what the movement it is being
     * sent is supposed to look like. Neither waits on the other, and they agree because they read the same
     * jump key, the same ground and the same power-up.
     */
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void mubble$tickAirMoves(CallbackInfo ci) {
        var this_ = (Player) (Object) this;
        this.mubble$airMoveParticles(this_);
        this.mubble$tickLaunchAndDrill(this_);

        // Landing is what hands the next jump its flutter back, and the only thing that does.
        if (this_.onGround()) {
            this.mubble$flutterSpent = false;
            this.mubble$endAirMoves(this_);
            return;
        }
        // Anything holding the player up other than the air takes both abilities away with it, and so does
        // letting go of the key. The flutter never comes back for that jump; the float does, on the next press.
        if (mubble$heldBySomethingElse(this_) || !this_.isJumpKeyHeld()) {
            this.mubble$endAirMoves(this_);
            return;
        }

        if (this.mubble$tickFlutter(this_)) {
            // Still being pushed up, so there is no fall to slow down yet.
            this.mubble$setFloating(this_, false);
            return;
        }
        this.mubble$tickFloat(this_);
    }

    /**
     * The climb: starts once the player is past the peak of their jump, and builds for as long as it lasts.
     *
     * @return whether the flutter took this tick's movement for itself
     */
    @Unique
    private boolean mubble$tickFlutter(Player player) {
        var ability = this.getFlutterAbility();
        if (ability.isEmpty()) {
            // The form can be lost in mid-air, and the flutter goes with it.
            this.mubble$setFluttering(player, false);
            return false;
        }
        FlutterAbility flutter = ability.get();

        if (this.mubble$fluttering) {
            if (this.mubble$flutterTicks >= flutter.duration()) {
                this.mubble$setFluttering(player, false);
                return false;
            }
        } else {
            // Nothing changes on the way up: the flutter waits for the player to start coming back down.
            if (this.mubble$flutterSpent || player.getKnownMovement().y() >= 0.0D) {
                return false;
            }
            this.mubble$flutterTicks = 0;
            this.mubble$flutterSpent = true;
            this.mubble$setFluttering(player, true);
        }

        Vec3 movement = player.getDeltaMovement();
        player.setDeltaMovement(movement.x(), flutter.liftAt(this.mubble$flutterTicks), movement.z());
        this.mubble$flutterTicks++;
        return true;
    }

    /**
     * The descent: holds the player to a walking pace on the way down, and spares them most of what the fall
     * would otherwise be worth.
     * <p>
     * It only ever slows a fall: a player still climbing under their own steam is left alone, and one already
     * coming down slower than the float is never sped up to it.
     */
    @Unique
    private void mubble$tickFloat(Player player) {
        var ability = this.getFloatAbility();
        this.mubble$setFloating(player, ability.isPresent() && mubble$applyFloat(player, ability.get()));
    }

    /**
     * @return whether the player was falling, and so held to the float
     */
    @Unique
    private static boolean mubble$applyFloat(Player player, FloatAbility floating) {
        if (player.getKnownMovement().y() >= 0.0D) {
            return false;
        }
        Vec3 movement = player.getDeltaMovement();
        if (movement.y() < -floating.speed()) {
            player.setDeltaMovement(movement.x(), -floating.speed(), movement.z());
        }
        // The fall damage of a tick spent floating is written off here rather than at the landing: the player
        // is falling the whole time, so this is the only place that knows the fall was a float and not a drop.
        player.fallDistance = Math.max(0.0D, player.fallDistance - floating.fallForgivenPerTick());
        return true;
    }

    /**
     * The launch on a double tap of the jump key, the slow fall from its peak, and the drill on a fresh press
     * of sneak in the air. Run like the flutter, by each side for the player it is in charge of, from the same
     * keys: the server only tells the other clients through the synced flags.
     */
    @Unique
    private void mubble$tickLaunchAndDrill(Player player) {
        boolean jumpHeld = player.isJumpKeyHeld();
        boolean jumpPressed = jumpHeld && !this.mubble$jumpKeyWasHeld;
        this.mubble$jumpKeyWasHeld = jumpHeld;
        boolean sneakHeld = player.isShiftKeyDown();
        boolean sneakPressed = sneakHeld && !this.mubble$sneakKeyWasHeld;
        this.mubble$sneakKeyWasHeld = sneakHeld;
        if (this.mubble$doubleTapTicks > 0) {
            this.mubble$doubleTapTicks--;
        }

        var launch = this.getLaunchAbility();
        var drill = this.getDrillAbility();
        if (launch.isEmpty()) {
            this.mubble$setLaunching(player, false);
            this.mubble$setSlowFalling(player, false);
        }
        if (drill.isEmpty()) {
            this.mubble$setDrilling(player, false);
        }

        boolean held = player.onGround() || mubble$heldBySomethingElse(player) || player.getAbilities().flying;
        if (held) {
            if (this.mubble$drilling && player.onGround()) {
                drill.flatMap(DrillAbility::landSound).ifPresent(sound -> mubble$playSound(player, sound));
            }
            this.mubble$launchSpent = false;
            this.mubble$setLaunching(player, false);
            this.mubble$setSlowFalling(player, false);
            this.mubble$setDrilling(player, false);
        }

        if (jumpPressed && launch.isPresent()) {
            if (this.mubble$doubleTapTicks > 0) {
                this.mubble$doubleTapTicks = 0;
                if (this.mubble$tryLaunch(player, launch.get())) {
                    return;
                }
            } else {
                this.mubble$doubleTapTicks = launch.get().doubleTapWindow();
            }
        }
        if (held) {
            return;
        }

        if (sneakPressed && drill.isPresent()) {
            this.mubble$setLaunching(player, false);
            this.mubble$setSlowFalling(player, false);
            this.mubble$setDrilling(player, true);
        }
        if (this.mubble$drilling) {
            DrillAbility drilling = drill.get();
            Vec3 movement = player.getDeltaMovement();
            player.setDeltaMovement(movement.x() * drilling.control(), -drilling.speed(), movement.z() * drilling.control());
            player.fallDistance = Math.max(0.0D, player.fallDistance - drilling.fallForgivenPerTick());
            return;
        }

        if (this.mubble$launching) {
            double climb = player.getKnownMovement().y();
            this.mubble$launchRose |= climb > 0.0D;
            this.mubble$launchTicks++;
            if (climb > 0.0D || (!this.mubble$launchRose && this.mubble$launchTicks <= LAUNCH_RISE_GRACE)) {
                return;
            }
            this.mubble$setLaunching(player, false);
            this.mubble$setSlowFalling(player, launch.get().slowFall().isPresent());
        }
        if (this.mubble$slowFalling) {
            var slowFall = launch.flatMap(LaunchAbility::slowFall);
            if (sneakHeld || slowFall.isEmpty()) {
                this.mubble$setSlowFalling(player, false);
            } else {
                mubble$applyFloat(player, slowFall.get());
            }
        }
    }

    /**
     * Boosts the player straight up on the very tick of the second tap, from the ground or from the air.
     * A player who may fly keeps the double tap for vanilla's flight toggle.
     */
    @Unique
    private boolean mubble$tryLaunch(Player player, LaunchAbility launch) {
        if (this.mubble$launchSpent || player.getAbilities().mayfly || mubble$heldBySomethingElse(player)) {
            return false;
        }
        Vec3 movement = player.getDeltaMovement();
        player.setDeltaMovement(movement.x(), launch.takeOffSpeed(player.getGravity()), movement.z());
        // Off the ground already, so that the vanilla jump of this very tick does not take the boost back.
        player.setOnGround(false);
        this.mubble$launchSpent = true;
        this.mubble$launchTicks = 0;
        this.mubble$launchRose = false;
        this.mubble$setDrilling(player, false);
        this.mubble$setSlowFalling(player, false);
        this.mubble$setLaunching(player, true);
        launch.sound().ifPresent(sound -> mubble$playSound(player, sound));
        return true;
    }

    /** Heard at once by the player making the move, their client predicting it, and sent to everyone else. */
    @Unique
    private static void mubble$playSound(Player player, Holder<SoundEvent> sound) {
        player.level().playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Unique
    private void mubble$setLaunching(Player player, boolean launching) {
        this.mubble$launching = launching;
        this.mubble$sync(player, LAUNCHING, launching);
    }

    @Unique
    private void mubble$setSlowFalling(Player player, boolean slowFalling) {
        this.mubble$slowFalling = slowFalling;
        this.mubble$sync(player, SLOW_FALLING, slowFalling);
    }

    @Unique
    private void mubble$setDrilling(Player player, boolean drilling) {
        this.mubble$drilling = drilling;
        this.mubble$sync(player, DRILLING, drilling);
    }

    /**
     * The states a mid-air move cannot carry on through, landing aside: they are all ways of being held by
     * something other than the air.
     */
    @Unique
    private static boolean mubble$heldBySomethingElse(Player player) {
        return player.isInWater() || player.onClimbable() || player.isFallFlying() || player.isPassenger();
    }

    @Unique
    private void mubble$endAirMoves(Player player) {
        this.mubble$setFluttering(player, false);
        this.mubble$setFloating(player, false);
    }

    @Unique
    private void mubble$setFluttering(Player player, boolean fluttering) {
        if (this.mubble$fluttering == fluttering) {
            return;
        }
        this.mubble$fluttering = fluttering;
        if (!fluttering) {
            this.mubble$flutterTicks = 0;
        }
        this.mubble$sync(player, FLUTTERING, fluttering);
    }

    @Unique
    private void mubble$setFloating(Player player, boolean floating) {
        if (this.mubble$floating == floating) {
            return;
        }
        this.mubble$floating = floating;
        this.mubble$sync(player, FLOATING, floating);
    }

    /**
     * Tells the other clients about a mid-air move, which is all they get: they have no business simulating
     * someone else's keys, they only draw what the move looks like.
     */
    @Unique
    private void mubble$sync(Player player, EntityDataAccessor<Boolean> accessor, boolean value) {
        if (!player.level().isClientSide()) {
            player.getEntityData().set(accessor, value);
        }
    }

    /**
     * Leaves the trail of a mid-air move around the feet of the player.
     * <p>
     * Every client draws it for every player it can see on one, rather than the server broadcasting it: the
     * player fluttering right here should not have to wait on a round trip to see their own leaves. It hangs
     * off the two states rather than off the tick right above, which only ever runs for the one player this
     * side is in charge of.
     */
    @Unique
    private void mubble$airMoveParticles(Player player) {
        if (!player.level().isClientSide()) {
            return;
        }
        this.mubble$burstParticles(player);
        Optional<ParticleOptions> particle = Optional.empty();
        double y = player.getY();
        if (player.isDrilling()) {
            particle = this.getDrillAbility().flatMap(DrillAbility::particle);
        } else if (player.isSlowFalling()) {
            particle = this.getLaunchAbility().flatMap(LaunchAbility::slowFall).flatMap(FloatAbility::particle);
            y += player.getBbHeight();
        } else if (player.isFluttering()) {
            particle = this.getFlutterAbility().flatMap(FlutterAbility::particle);
        } else if (player.isFloating()) {
            particle = this.getFloatAbility().flatMap(FloatAbility::particle);
        }
        if (particle.isEmpty()) {
            return;
        }
        double spread = player.getBbWidth() * AIR_MOVE_PARTICLE_SPREAD;
        for (int i = 0; i < AIR_MOVE_PARTICLES; i++) {
            player.level().addParticle(particle.get(),
                    player.getRandomX(spread), y, player.getRandomZ(spread),
                    0.0D, AIR_MOVE_PARTICLE_FALL, 0.0D);
        }
    }

    /** A ring thrown out from the feet when a launch takes off and when a drill hits the ground. */
    @Unique
    private void mubble$burstParticles(Player player) {
        boolean launching = player.isLaunching();
        boolean drilling = player.isDrilling();
        Optional<ParticleOptions> particle = Optional.empty();
        if (launching && !this.mubble$wasLaunching) {
            particle = this.getLaunchAbility().flatMap(LaunchAbility::particle);
        } else if (!drilling && this.mubble$wasDrilling && player.onGround()) {
            particle = this.getDrillAbility().flatMap(DrillAbility::particle);
        }
        this.mubble$wasLaunching = launching;
        this.mubble$wasDrilling = drilling;
        if (particle.isEmpty()) {
            return;
        }
        for (int i = 0; i < AIR_MOVE_BURST_PARTICLES; i++) {
            double angle = i * Math.PI * 2.0D / AIR_MOVE_BURST_PARTICLES;
            player.level().addParticle(particle.get(), player.getX(), player.getY(), player.getZ(),
                    Math.cos(angle) * AIR_MOVE_BURST_SPEED, 0.0D, Math.sin(angle) * AIR_MOVE_BURST_SPEED);
        }
    }

    /**
     * The server is told the jump key of every player it runs, tick after tick, by the input packets they
     * send. A client only ever knows its own, which {@code LocalPlayerMixin} answers with: the players it
     * merely watches never start a move of their own, they are shown the one the server tells them about.
     */
    @Override
    public boolean isJumpKeyHeld() {
        var this_ = (Player) (Object) this;
        return this_ instanceof ServerPlayer serverPlayer && serverPlayer.getLastClientInput().jump();
    }

    @Override
    public Optional<FlutterAbility> getFlutterAbility() {
        var this_ = (Player) (Object) this;
        return this_.getPowerUp().flatMap(powerUp -> powerUp.value().abilities().flutter());
    }

    @Override
    public Optional<FloatAbility> getFloatAbility() {
        var this_ = (Player) (Object) this;
        return this_.getPowerUp().flatMap(powerUp -> powerUp.value().abilities().floating());
    }

    @Override
    public Optional<LaunchAbility> getLaunchAbility() {
        var this_ = (Player) (Object) this;
        return this_.getPowerUp().flatMap(powerUp -> powerUp.value().abilities().launch());
    }

    @Override
    public Optional<DrillAbility> getDrillAbility() {
        var this_ = (Player) (Object) this;
        return this_.getPowerUp().flatMap(powerUp -> powerUp.value().abilities().drill());
    }

    @Override
    public boolean isLaunching() {
        var this_ = (Player) (Object) this;
        return this.mubble$launching || this_.getEntityData().get(LAUNCHING);
    }

    @Override
    public boolean isSlowFalling() {
        var this_ = (Player) (Object) this;
        return this.mubble$slowFalling || this_.getEntityData().get(SLOW_FALLING);
    }

    @Override
    public boolean hasLaunched() {
        return this.mubble$launchSpent;
    }

    @Override
    public boolean isDrilling() {
        var this_ = (Player) (Object) this;
        return this.mubble$drilling || this_.getEntityData().get(DRILLING);
    }

    @Override
    public boolean isFluttering() {
        var this_ = (Player) (Object) this;
        // The two are the same truth seen from two places: the side simulating the player writes the field,
        // and the clients watching someone else only ever get the flag.
        return this.mubble$fluttering || this_.getEntityData().get(FLUTTERING);
    }

    @Override
    public boolean isFloating() {
        var this_ = (Player) (Object) this;
        return this.mubble$floating || this_.getEntityData().get(FLOATING);
    }

    @Override
    public int getFlutterTicks() {
        return this.mubble$fluttering ? this.mubble$flutterTicks : 0;
    }

    @Override
    public boolean hasFluttered() {
        return this.mubble$flutterSpent;
    }

    @Override
    public boolean isRunningOnWater() {
        return this.mubble$runningOnWater;
    }

    @Override
    public void setRunningOnWater(boolean runningOnWater) {
        this.mubble$runningOnWater = runningOnWater;
    }
}
