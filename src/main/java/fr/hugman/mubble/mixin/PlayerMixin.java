package fr.hugman.mubble.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.entity.data.MubbleTrackedData;
import fr.hugman.mubble.network.payload.c2s.PowerUpChangePayload;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.PowerUpHolder;
import fr.hugman.mubble.power_up.PowerUpProperties;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public class PlayerMixin implements PowerUpHolder {
    @Unique
    private static final TrackedData<Optional<RegistryEntry<PowerUp>>> POWER_UP = DataTracker.registerData(PlayerEntity.class, MubbleTrackedData.OPTIONAL_POWER_UP);

    @Unique
    private static final String POWER_UP_KEY = "power_up";

    @Unique
    private static final MapCodec<RegistryEntry<PowerUp>> POWER_UP_MAP_CODEC = PowerUp.ENTRY_CODEC.fieldOf(POWER_UP_KEY);

    @Unique
    private static final Codec<RegistryEntry<PowerUp>> POWER_UP_ENTRY_CODEC = POWER_UP_MAP_CODEC.codec();

    @Unique
    private final PowerUpProperties powerUpProperties = new PowerUpProperties();

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void mubble$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(POWER_UP, Optional.empty());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void mubble$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var this_ = (PlayerEntity) ((Object) this);
        this_.getPowerUp().ifPresent(entry -> POWER_UP_ENTRY_CODEC.encodeStart(this_.getRegistryManager().getOps(NbtOps.INSTANCE), entry).ifSuccess(nbtElement -> nbt.copyFrom((NbtCompound) nbtElement)));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void mubble$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        var this_ = (PlayerEntity) (Object) this;
        POWER_UP_ENTRY_CODEC.parse(this_.getRegistryManager().getOps(NbtOps.INSTANCE), nbt).ifSuccess(entry -> this_.getDataTracker().set(POWER_UP, Optional.of(entry)));
    }

    @Override
    public Optional<RegistryEntry<PowerUp>> getPowerUp() {
        var this_ = (PlayerEntity) (Object) this;
        return this_.getDataTracker().get(POWER_UP);
    }


    @Override
    public PowerUpProperties getPowerUpProperties() {
        return this.powerUpProperties;
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
