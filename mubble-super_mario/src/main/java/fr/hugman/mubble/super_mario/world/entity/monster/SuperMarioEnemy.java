package fr.hugman.mubble.super_mario.world.entity.monster;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
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

    protected SuperMarioEnemy(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(STOMPED, false);
    }

    /**
     * Keeps the stomped flag describing the hit that is landing right now.
     * <p>
     * The flag picks the death sound and swaps the vanilla death animation for the stomp one, and both are
     * read while the killing hit is still being handled, so it has to be set before the damage is dealt
     * rather than when the enemy is jumped on: a stomp that leaves the enemy alive — a weak one, or one from
     * a player whose power-up takes the damage out of it — would otherwise flatten it whenever it died next.
     */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        boolean wasStomped = this.isStomped();
        this.setStomped(source.is(SuperMarioDamageTypeIds.STOMP));
        boolean hurt = super.hurtServer(level, source, amount);
        if (!hurt) {
            // Nothing landed, so nothing about how this enemy dies has changed either.
            this.setStomped(wasStomped);
        }
        return hurt;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.isStomped()) {
            this.getStompDeathAnimationState().startIfStopped(this.tickCount);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (STOMPED.equals(accessor)) {
            if (this.isStomped() && this.dead) {
                this.getStompDeathAnimationState().startIfStopped(this.tickCount);
            }
        }
        super.onSyncedDataUpdated(accessor);
    }

    public boolean isStomped() {
        return this.entityData.get(STOMPED);
    }

    public void setStomped(boolean b) {
        this.entityData.set(STOMPED, b);
    }

    abstract public AnimationState getStompDeathAnimationState();
}
