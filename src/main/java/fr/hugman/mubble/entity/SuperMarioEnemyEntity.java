package fr.hugman.mubble.entity;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Represents an enemy from the Super Mario series.
 * They can be killed by being stomped, and can display a custom death animation.
 *
 * @author Hugman
 * @since v4.0.0
 */
abstract public class SuperMarioEnemyEntity extends HostileEntity {
    protected static final TrackedData<Boolean> STOMPED = DataTracker.registerData(SuperMarioEnemyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected SuperMarioEnemyEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STOMPED, false);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.isStomped()) {
            this.getStompDeathAnimationState().startIfNotRunning(this.age);
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (STOMPED.equals(data)) {
            if (this.isStomped() && this.dead) {
                this.getStompDeathAnimationState().startIfNotRunning(this.age);
            }
        }
    }

    public boolean isStomped() {
        return this.dataTracker.get(STOMPED);
    }

    public void setStomped(boolean b) {
        this.dataTracker.set(STOMPED, b);
    }

    @Override
    public void onStompedBy(List<Entity> entities) {
        this.setStomped(true);
        super.onStompedBy(entities);
    }

    abstract public AnimationState getStompDeathAnimationState();
}
