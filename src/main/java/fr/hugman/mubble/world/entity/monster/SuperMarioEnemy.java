package fr.hugman.mubble.world.entity.monster;

import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Represents an enemy from the Super Mario series.
 * They can be killed by being stomped, and can display a custom death animation.
 *
 * @author Hugman
 * @since v4.0.0
 */
abstract public class SuperMarioEnemy extends Monster {
    protected static final EntityDataAccessor<Boolean> STOMPED = SynchedEntityData.defineId(SuperMarioEnemy.class, EntityDataSerializers.BOOLEAN);

    protected SuperMarioEnemy(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STOMPED, false);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (this.isStomped()) {
            this.getStompDeathAnimationState().startIfStopped(this.tickCount);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (STOMPED.equals(data)) {
            if (this.isStomped() && this.dead) {
                this.getStompDeathAnimationState().startIfStopped(this.tickCount);
            }
        }
    }

    public boolean isStomped() {
        return this.entityData.get(STOMPED);
    }

    public void setStomped(boolean b) {
        this.entityData.set(STOMPED, b);
    }

    @Override
    public void onStompedBy(List<Entity> entities) {
        this.setStomped(true);
        super.onStompedBy(entities);
    }

    abstract public AnimationState getStompDeathAnimationState();
}
