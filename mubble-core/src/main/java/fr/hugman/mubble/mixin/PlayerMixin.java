package fr.hugman.mubble.mixin;

import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpChangePayload;
import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.Fluttering;
import fr.hugman.mubble.world.entity.WaterRunner;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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
public class PlayerMixin implements PowerUpHolder, WaterRunner, Fluttering {
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

    @Unique
    private static final String POWER_UP_KEY = "power_up";
    @Unique
    private static final String POWER_UP_PROPERTIES_KEY = "power_up_properties";
    @Unique
    private static final String FLUTTER_TICKS_KEY = "flutter_ticks";
    @Unique
    private static final String FLUTTER_SPENT_KEY = "flutter_spent";

    /** How many particles a fluttering player leaves around their feet every tick. */
    @Unique
    private static final int FLUTTER_PARTICLES = 2;
    /** How far those scatter around the feet, as a share of the width of the player. */
    @Unique
    private static final double FLUTTER_PARTICLE_SPREAD = 0.8D;
    /** How fast they sink, so that they read as being left behind by someone going up. */
    @Unique
    private static final double FLUTTER_PARTICLE_FALL = -0.05D;

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

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void mubble$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(POWER_UP, Optional.empty());
        builder.define(POWER_UP_PROPERTIES, Optional.empty());
        builder.define(FLUTTERING, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void mubble$writeCustomData(ValueOutput view, CallbackInfo ci) {
        var this_ = (Player) ((Object) this);

		this_.getPowerUp().ifPresent(entry -> view.store(POWER_UP_KEY, PowerUp.CODEC, entry));
        view.storeNullable(POWER_UP_PROPERTIES_KEY, PowerUpProperties.CODEC, this_.getPowerUpProperties());
        view.putInt(FLUTTER_TICKS_KEY, this.mubble$fluttering ? this.mubble$flutterTicks : -1);
        view.putBoolean(FLUTTER_SPENT_KEY, this.mubble$flutterSpent);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void mubble$readCustomData(ValueInput view, CallbackInfo ci) {
        var this_ = (Player) (Object) this;
		view.read(POWER_UP_KEY, PowerUp.CODEC).ifPresent(entry -> this_.getEntityData().set(POWER_UP, Optional.of(entry)));
        view.read(POWER_UP_PROPERTIES_KEY, PowerUpProperties.CODEC).ifPresent(properties -> this_.getEntityData().set(POWER_UP_PROPERTIES, Optional.of(properties)));
        // A flutter is written as the ticks it had run, and as -1 when there was none going on at all.
        int flutterTicks = view.getIntOr(FLUTTER_TICKS_KEY, -1);
        this.mubble$fluttering = flutterTicks >= 0;
        this.mubble$flutterTicks = Math.max(0, flutterTicks);
        this.mubble$flutterSpent = view.getBooleanOr(FLUTTER_SPENT_KEY, false);
        this.mubble$syncFluttering(this_);
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
     * Runs the flutter: past the peak of a jump, a player still leaning on the jump key rises again for a
     * moment instead of falling.
     * <p>
     * This sits at the head of {@code aiStep} because the lift is written straight into the movement of the
     * tick, which {@code travel()} then spends a little further down the very same call.
     * <p>
     * Both sides run it, each for the player it is in charge of: the client so that the movement it predicts
     * for itself actually goes up, the server so that it knows what the movement it is being sent is supposed
     * to look like. Neither waits on the other, and they agree because they read the same jump key, the same
     * ground and the same power-up.
     */
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void mubble$tickFlutter(CallbackInfo ci) {
        var this_ = (Player) (Object) this;
        this.mubble$flutterParticles(this_);

        // Landing is what hands the next jump its flutter back, and the only thing that does.
        if (this_.onGround()) {
            this.mubble$flutterSpent = false;
            this.mubble$endFlutter(this_);
            return;
        }

        var ability = this.getFlutterAbility();
        if (ability.isEmpty()) {
            // The form can be lost in mid-air, and the flutter goes with it.
            this.mubble$endFlutter(this_);
            return;
        }
        FlutterAbility flutter = ability.get();

        boolean jumpHeld = this_.isJumpKeyHeld();
        if (this.mubble$fluttering) {
            // A released key cannot be leaned on again: that jump is done fluttering.
            if (!jumpHeld || this.mubble$flutterTicks >= flutter.duration() || mubble$flutterCutShort(this_)) {
                this.mubble$endFlutter(this_);
                return;
            }
        } else {
            if (this.mubble$flutterSpent || !jumpHeld || mubble$flutterCutShort(this_)) {
                return;
            }
            // Nothing changes on the way up: the flutter waits for the player to start coming back down.
            if (this_.getKnownMovement().y() >= 0.0D) {
                return;
            }
            this.mubble$fluttering = true;
            this.mubble$flutterTicks = 0;
            this.mubble$flutterSpent = true;
            this.mubble$syncFluttering(this_);
        }

        Vec3 movement = this_.getDeltaMovement();
        this_.setDeltaMovement(movement.x(), flutter.liftAt(this.mubble$flutterTicks), movement.z());
        this.mubble$flutterTicks++;
    }

    /**
     * The states a flutter cannot carry on through, landing aside: they are all ways of being held by
     * something other than the air.
     */
    @Unique
    private static boolean mubble$flutterCutShort(Player player) {
        return player.isInWater() || player.onClimbable() || player.isFallFlying() || player.isPassenger();
    }

    @Unique
    private void mubble$endFlutter(Player player) {
        if (!this.mubble$fluttering) {
            return;
        }
        this.mubble$fluttering = false;
        this.mubble$flutterTicks = 0;
        this.mubble$syncFluttering(player);
    }

    /**
     * Tells the other clients about the flutter, which is all they get: they have no business simulating
     * someone else's keys, they only draw what the flutter looks like.
     */
    @Unique
    private void mubble$syncFluttering(Player player) {
        if (!player.level().isClientSide()) {
            player.getEntityData().set(FLUTTERING, this.mubble$fluttering);
        }
    }

    /**
     * Leaves the trail of a flutter around the feet of the player.
     * <p>
     * Every client draws it for every player it can see fluttering, rather than the server broadcasting it:
     * the player fluttering right here should not have to wait on a round trip to see their own leaves.
     * It hangs off {@code isFluttering()} rather than off the flutter tick right below, which only ever
     * runs for the one player this side is in charge of.
     */
    @Unique
    private void mubble$flutterParticles(Player player) {
        if (!player.level().isClientSide() || !player.isFluttering()) {
            return;
        }
        var particle = this.getFlutterAbility().flatMap(FlutterAbility::particle);
        if (particle.isEmpty()) {
            return;
        }
        double spread = player.getBbWidth() * FLUTTER_PARTICLE_SPREAD;
        for (int i = 0; i < FLUTTER_PARTICLES; i++) {
            player.level().addParticle(particle.get(),
                    player.getRandomX(spread), player.getY(), player.getRandomZ(spread),
                    0.0D, FLUTTER_PARTICLE_FALL, 0.0D);
        }
    }

    /**
     * The server is told the jump key of every player it runs, tick after tick, by the input packets they
     * send. A client only ever knows its own, which {@code LocalPlayerMixin} answers with: the players it
     * merely watches never start a flutter of their own, they are shown the one the server tells them about.
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
    public boolean isFluttering() {
        var this_ = (Player) (Object) this;
        // The two are the same truth seen from two places: the side simulating the player writes the field,
        // and the clients watching someone else only ever get the flag.
        return this.mubble$fluttering || this_.getEntityData().get(FLUTTERING);
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
