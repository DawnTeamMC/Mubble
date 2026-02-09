package fr.hugman.mubble.mixin;

import fr.hugman.mubble.network.syncher.MubbleEntityDataSerializers;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpChangePayload;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.world.entity.item.collectible.CollectibleEntity;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(Player.class)
public class PlayerMixin implements PowerUpHolder {
    @Unique
    private static final EntityDataAccessor<Optional<Holder<PowerUp>>> POWER_UP = SynchedEntityData.defineId(Player.class, MubbleEntityDataSerializers.OPTIONAL_POWER_UP);
    private static final EntityDataAccessor<PowerUpProperties> POWER_UP_PROPERTIES = SynchedEntityData.defineId(Player.class, MubbleEntityDataSerializers.POWER_UP_PROPERTIES);

    @Unique
    private static final String POWER_UP_KEY = "power_up";

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void mubble$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(POWER_UP, Optional.empty());
        builder.define(POWER_UP_PROPERTIES, new PowerUpProperties(0, new ArrayList<>()));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void mubble$writeCustomData(ValueOutput view, CallbackInfo ci) {
        var this_ = (Player) ((Object) this);

		this_.getPowerUp().ifPresent(entry -> view.store(POWER_UP_KEY, PowerUp.CODEC, entry));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void mubble$readCustomData(ValueInput view, CallbackInfo ci) {
        var this_ = (Player) (Object) this;
		view.read(POWER_UP_KEY, PowerUp.CODEC).ifPresent(entry -> this_.getEntityData().set(POWER_UP, Optional.of(entry)));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mubble$tick(CallbackInfo ci) {
        var this_ = (Player) (Object) this;
        if(this_.level().isClientSide()) {
            return;
        }
        this_.getPowerUp().ifPresent(entry -> {
            this.getPowerUpProperties().tick();
            // safe check
            if(this_.tickCount % 20 == 0) {
                this.getPowerUpProperties().doSoftChecks(this_);
            }
            if(this.getPowerUpProperties().checkDirty()) {
                this_.getEntityData().set(POWER_UP_PROPERTIES, this_.getPowerUpProperties(), true);
            }
        });
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
    public PowerUpProperties getPowerUpProperties() {
        var this_ = (Player) (Object) this;
        return this_.getEntityData().get(POWER_UP_PROPERTIES);
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
}
