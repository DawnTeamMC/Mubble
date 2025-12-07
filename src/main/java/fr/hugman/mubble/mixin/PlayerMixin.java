package fr.hugman.mubble.mixin;

import fr.hugman.mubble.entity.data.MubbleTrackedData;
import fr.hugman.mubble.network.payload.c2s.PowerUpChangePayload;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.PowerUpHolder;
import fr.hugman.mubble.power_up.PowerUpProperties;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public class PlayerMixin implements PowerUpHolder {
    @Unique
    private static final TrackedData<Optional<RegistryEntry<PowerUp>>> POWER_UP = DataTracker.registerData(PlayerEntity.class, MubbleTrackedData.OPTIONAL_POWER_UP);
    private static final TrackedData<PowerUpProperties> POWER_UP_PROPERTIES = DataTracker.registerData(PlayerEntity.class, MubbleTrackedData.POWER_UP_PROPERTIES);

    @Unique
    private static final String POWER_UP_KEY = "power_up";

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void mubble$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(POWER_UP, Optional.empty());
        builder.add(POWER_UP_PROPERTIES, new PowerUpProperties(0, new ArrayList<>()));
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void mubble$writeCustomData(WriteView view, CallbackInfo ci) {
        var this_ = (PlayerEntity) ((Object) this);

		this_.getPowerUp().ifPresent(entry -> view.put(POWER_UP_KEY, PowerUp.ENTRY_CODEC, entry));
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void mubble$readCustomData(ReadView view, CallbackInfo ci) {
        var this_ = (PlayerEntity) (Object) this;
		view.read(POWER_UP_KEY, PowerUp.ENTRY_CODEC).ifPresent(entry -> this_.getDataTracker().set(POWER_UP, Optional.of(entry)));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mubble$tick(CallbackInfo ci) {
        var this_ = (PlayerEntity) (Object) this;
        if(this_.getEntityWorld().isClient()) {
            return;
        }
        this_.getPowerUp().ifPresent(entry -> {
            this.getPowerUpProperties().tick();
            // safe check
            if(this_.age % 20 == 0) {
                this.getPowerUpProperties().doSoftChecks(this_);
            }
            if(this.getPowerUpProperties().checkDirty()) {
                this_.getDataTracker().set(POWER_UP_PROPERTIES, this_.getPowerUpProperties(), true);
            }
        });
    }

    @Override
    public Optional<RegistryEntry<PowerUp>> getPowerUp() {
        var this_ = (PlayerEntity) (Object) this;
        return this_.getDataTracker().get(POWER_UP);
    }

    @Override
    public PowerUpProperties getPowerUpProperties() {
        var this_ = (PlayerEntity) (Object) this;
        return this_.getDataTracker().get(POWER_UP_PROPERTIES);
    }

    @Override
    public void setPowerUp(RegistryEntry<PowerUp> powerUp) {
        var this_ = (PlayerEntity) (Object) this;
        var previous = this_.getPowerUp();
        this_.getDataTracker().set(POWER_UP, Optional.of(powerUp));
        if (this_ instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new PowerUpChangePayload(previous, Optional.of(powerUp)));
        }
        PowerUp.onChange(this_, previous, Optional.of(powerUp));
    }

    @Override
    public void clearPowerUp() {
        var this_ = (PlayerEntity) (Object) this;
        var previous = this_.getPowerUp();
        this_.getDataTracker().set(POWER_UP, Optional.empty());
        if (this_ instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new PowerUpChangePayload(previous, Optional.empty()));
        }
        PowerUp.onChange(this_, previous, Optional.empty());
    }
}
