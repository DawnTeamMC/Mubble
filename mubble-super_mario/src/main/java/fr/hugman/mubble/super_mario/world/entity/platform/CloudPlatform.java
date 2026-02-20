package fr.hugman.mubble.super_mario.world.entity.platform;

import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpKeys;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.world.power_up.PowerUpHolder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

//TODO: spin quickly when just spawned via power-up
public class CloudPlatform extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Boolean> IS_OCCUPIED = SynchedEntityData.defineId(CloudPlatform.class, EntityDataSerializers.BOOLEAN);

    private static final String AGE_KEY = "age";
    private static final String DURATION_KEY = "duration";
    private static final String DURATION_DELAY_KEY = "duration_delay";

    public static final int INFINITE_DURATION = -1;

    private int duration = INFINITE_DURATION;
    private int durationDelay;

    private float scale = 1;
    private float oScale = 1;

    private int lastOccupiedTick = -1;

    @Nullable
    private EntityReference<LivingEntity> owner;

    public CloudPlatform(EntityType<?> type, Level level) {
        super(type, level);
        this.setRequiresPrecisePosition(true);
        this.setDuration(100);
        this.setDurationDelay(100);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public int getDurationDelay() {
        return durationDelay;
    }

    public void setDurationDelay(int durationDelay) {
        this.durationDelay = durationDelay;
    }

    @Nullable
    public LivingEntity getOwner() {
        return EntityReference.getLivingEntity(this.owner, this.level());
    }

    public void setOwner(@Nullable final LivingEntity owner) {
        this.owner = EntityReference.of(owner);
    }

    public boolean isOccupied() {
        return this.entityData.get(IS_OCCUPIED);
    }

    public void setOccupied(boolean occupied) {
        this.entityData.set(IS_OCCUPIED, occupied);
    }

    public int getTicksSinceLastOccupied() {
        return this.tickCount - this.lastOccupiedTick;
    }

    public boolean isShrinking() {
        if(this.duration == INFINITE_DURATION || this.isOccupied()) {
            return false;
        }
        int timeLeft = this.duration - this.tickCount;
        return timeLeft < 40;
    }

    @Override
    public void tick() {
        if(this.firstTick) {
            this.playSound(SuperMarioSounds.CLOUD_PLATFORM_APPEAR.value());
        }

        super.tick();

        this.oScale = this.scale;
        if (isShrinking()) {
            float progress = Math.clamp((this.duration - this.tickCount) / 40f, 0f, 1f);
            this.scale = progress * progress * (3 - 2 * progress);
            this.refreshDimensions();
        }
        else {
            this.scale = 1;
        }

        if(!this.level().isClientSide()) {
            if (this.duration != INFINITE_DURATION && !this.isOccupied()) {
                if(this.tickCount >= this.duration) {
                    this.discard();
                }
            }
            setOccupied(checkOccupied());
        }
    }

    @Override
    public void onRemoval(RemovalReason reason) {
        this.playSound(SuperMarioSounds.CLOUD_PLATFORM_DISAPPEAR.value());
        super.onRemoval(reason);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if(IS_OCCUPIED.equals(accessor)) {
            if(this.isOccupied()) {
                this.refreshDimensions();
            } else {
                this.duration = this.tickCount + this.durationDelay;
            }
            this.lastOccupiedTick = this.tickCount;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(IS_OCCUPIED, false);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.tickCount = input.getIntOr(AGE_KEY, 0);
        this.duration = input.getIntOr(DURATION_KEY, -1);
        this.durationDelay = input.getIntOr(DURATION_DELAY_KEY, -1);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt(AGE_KEY, this.tickCount);
        output.putInt(DURATION_KEY, this.duration);
        output.putInt(DURATION_DELAY_KEY, this.durationDelay);
    }

    @Override
    protected AABB makeBoundingBox(final Vec3 position) {
        if(this.duration == INFINITE_DURATION) {
            return super.makeBoundingBox(position);
        }
        if(this.scale != 1) {
            var dimensions = this.getType().getDimensions();
            return dimensions.scale(this.scale, 1.0f).makeBoundingBox(position);
        }
        return super.makeBoundingBox(position);
    }

    public float getScale(float partialTicks) {
        return this.oScale + (this.scale - this.oScale) * partialTicks;
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity other) {
        // entity must have the cloud power-up
        if(!canOccupy(other)) {
            return false;
        }
        // entity must be on top of the platform
        if(other.getDeltaMovement().y() > 0.0f || other.getY() < this.getY(1.0f)) {
            return false;
        }
        return true;
    }

    private boolean canOccupy(Entity entity) {
        if(entity instanceof Player player && player.isSpectator()) {
            return false;
        }
        // entity must have the cloud power-up
        return entity instanceof PowerUpHolder powerUpHolder
                && powerUpHolder.getPowerUp().isPresent()
                && powerUpHolder.getPowerUp().get().is(SuperMarioPowerUpKeys.CLOUD);
    }

    /**
     * @return true if a player is currently on top of the platform
     */
    private boolean checkOccupied() {
        AABB bb = this.getBoundingBox();
        AABB detectionBox = new AABB(
                bb.minX - 1.0,
                bb.maxY - 1.0E-5F,
                bb.minZ - 1.0,
                bb.maxX + 1.0,
                bb.maxY + 1.0E-5F,
                bb.maxZ + 1.0
        );

        for (Player player : this.level().players()) {
            if (canOccupy(player) && detectionBox.contains(player.position())) {
                return true;
            }
        }

        return false;
    }
}
