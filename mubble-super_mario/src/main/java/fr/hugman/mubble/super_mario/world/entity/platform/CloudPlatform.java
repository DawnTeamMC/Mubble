package fr.hugman.mubble.super_mario.world.entity.platform;

import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

//TODO: implement duration
public class CloudPlatform extends Entity implements TraceableEntity {
    @Nullable
    private EntityReference<LivingEntity> owner;

    public CloudPlatform(EntityType<?> type, Level level) {
        super(type, level);
        this.setRequiresPrecisePosition(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        // entity must have the cloud power-up
        if(!(other instanceof PowerUpHolder powerUpHolder)
                || powerUpHolder.getPowerUp().isEmpty()
                || !powerUpHolder.getPowerUp().get().is(SuperMarioPowerUpKeys.CLOUD)) {
            return false;
        }
        // entity must be on top of the platform
        if(other.getDeltaMovement().y() > 0.0f || other.getY() < this.getY(1.0f)) {
            return false;
        }
        return true;
    }

    @Nullable
    public LivingEntity getOwner() {
        return EntityReference.getLivingEntity(this.owner, this.level());
    }

    public void setOwner(@Nullable final LivingEntity owner) {
        this.owner = EntityReference.of(owner);
    }
}
