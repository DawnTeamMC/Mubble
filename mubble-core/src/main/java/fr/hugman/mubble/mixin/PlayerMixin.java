package fr.hugman.mubble.mixin;

import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpChangePayload;
import fr.hugman.mubble.tags.MubblePowerUpTags;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.WaterRunner;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(Player.class)
public class PlayerMixin implements PowerUpHolder, WaterRunner {
    @Unique
    private static final EntityDataAccessor<Optional<PowerUpProperties>> POWER_UP_PROPERTIES = SynchedEntityData.defineId(Player.class, MubbleEntityDataSerializers.POWER_UP_PROPERTIES);
    @Unique
    private static final EntityDataAccessor<Optional<Holder<PowerUp>>> POWER_UP = SynchedEntityData.defineId(Player.class, MubbleEntityDataSerializers.OPTIONAL_POWER_UP);

    @Unique
    private static final String POWER_UP_KEY = "power_up";
    @Unique
    private static final String POWER_UP_PROPERTIES_KEY = "power_up_properties";

    /** Whether the sprint the player is on started on the ground, see {@link #mubble$updateRunningOnWater}. */
    @Unique
    private boolean mubble$runningOnWater;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void mubble$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(POWER_UP, Optional.empty());
        builder.define(POWER_UP_PROPERTIES, Optional.empty());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void mubble$writeCustomData(ValueOutput view, CallbackInfo ci) {
        var this_ = (Player) ((Object) this);

		this_.getPowerUp().ifPresent(entry -> view.store(POWER_UP_KEY, PowerUp.CODEC, entry));
        view.storeNullable(POWER_UP_PROPERTIES_KEY, PowerUpProperties.CODEC, this_.getPowerUpProperties());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void mubble$readCustomData(ValueInput view, CallbackInfo ci) {
        var this_ = (Player) (Object) this;
		view.read(POWER_UP_KEY, PowerUp.CODEC).ifPresent(entry -> this_.getEntityData().set(POWER_UP, Optional.of(entry)));
        view.read(POWER_UP_PROPERTIES_KEY, PowerUpProperties.CODEC).ifPresent(properties -> this_.getEntityData().set(POWER_UP_PROPERTIES, Optional.of(properties)));
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
     * Keeps track of whether the sprint the player is on can carry them over water.
     * <p>
     * Holding a power-up tagged {@code mubble:can_run_on_water} only opens the door: the sprint has
     * to have started on the ground and out of the water, and it is over as soon as the player runs
     * into a wall or goes under. Leaving the ground is neither, so a jump keeps the run going and the
     * player lands back on the surface.
     * <p>
     * A wall counts the way it does for a vanilla sprint, minor collisions aside: brushing past a
     * corner, or the step up the surface of the water sometimes is, should not drop anyone in.
     * <p>
     * This runs on both sides: the collision shape has to answer the same on the client that predicts
     * the movement and on the server that validates it, and both know everything the answer needs.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void mubble$updateRunningOnWater(CallbackInfo ci) {
        var this_ = (Player) (Object) this;

        var allowed = this_.getPowerUp().map(entry -> entry.is(MubblePowerUpTags.CAN_RUN_ON_WATER)).orElse(false);
        if (!allowed || !this_.isSprinting() || (this_.horizontalCollision && !this_.minorHorizontalCollision)) {
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

    @Override
    public boolean isRunningOnWater() {
        return this.mubble$runningOnWater;
    }

    @Override
    public void setRunningOnWater(boolean runningOnWater) {
        this.mubble$runningOnWater = runningOnWater;
    }
}
