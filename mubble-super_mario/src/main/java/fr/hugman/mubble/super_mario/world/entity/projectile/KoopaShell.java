package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.world.phys.AABBUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

// TODO: tweak with ProjectileUtil for better collision detection (square projection instead of center?)
public abstract class KoopaShell extends Projectile {
    public static final String REBOUNDS_KEY = "rebounds";
    protected static final float TARGET_SPEED = 0.5f;
    protected static final float TARGET_SPEED_ACCELERATION = 0.1f;

    protected static final int OWNER_STOMP_COOLDOWN_TICKS = 20;

    protected int rebounds;
    private int ownerStompCooldown;
    private float previousHorizontalRotation;
    private float horizontalRotation;

    public KoopaShell(EntityType<? extends KoopaShell> type, Level level, int rebounds) {
        super(type, level);
        this.rebounds = rebounds;
        this.ownerStompCooldown = OWNER_STOMP_COOLDOWN_TICKS;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        super.addAdditionalSaveData(view);
        view.putInt(REBOUNDS_KEY, rebounds);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        super.readAdditionalSaveData(view);
        this.rebounds = view.getIntOr(REBOUNDS_KEY, 3);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    public abstract Identifier getTexture();

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide() && this.ownerStompCooldown > 0) {
            this.ownerStompCooldown--;
        }

        boolean isStopped = this.isStopped();

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        this.hitTargetOrDeflectSelf(hitResult);

        this.applyGravity();

        AABB hitBox = this.getBoundingBox().move(this.getDeltaMovement().x > 0 ? 0.01d : -0.01d, 0.0d, this.getDeltaMovement().z > 0 ? 0.01d : -0.01d);

        var prevVelocity = this.getDeltaMovement();
        this.move(MoverType.SELF, prevVelocity);
        var multiplier = AABBUtil.calculateHorizontalBouncingMultiplier(hitBox, AABBUtil.collectPotentialBlockCollisions(this.level(), hitBox));
        if (!isStopped) {
            if (multiplier != null) {
                prevVelocity = prevVelocity.multiply(multiplier);
                this.rebounds--;
                this.playBumpEffects(prevVelocity.reverse());
            }
            this.setDeltaMovement(prevVelocity.x(), this.getDeltaMovement().y(), prevVelocity.z());
            if (this.onGround()) {
                //TODO: make this configurable
                this.targetHorizontalSpeed(TARGET_SPEED, TARGET_SPEED_ACCELERATION);
            }
            this.needsSync = true;
        }

        if (this.level().isClientSide()) {
            this.tickRotation();
        }

        if (this.rebounds <= 0) {
            this.finalHit();
        }
    }

    public void tickRotation() {
        float velocityLength = (float) this.getDeltaMovement().horizontalDistance();
        this.previousHorizontalRotation = this.horizontalRotation;
        this.horizontalRotation = this.previousHorizontalRotation + velocityLength * 0.45f;
    }

    public void targetHorizontalSpeed(float targetSpeed, float acceleration) {
        Vec3 velocity = this.getDeltaMovement();
        if (velocity.x == 0 && velocity.z == 0) {
            return;
        }
        double currentSpeed = velocity.horizontalDistance();
        if (currentSpeed < 1e-10) {
            return;
        }
        double newSpeed;
        if (currentSpeed > targetSpeed) {
            newSpeed = Math.max(currentSpeed - acceleration, targetSpeed);
        } else {
            newSpeed = Math.min(currentSpeed + acceleration, targetSpeed);
        }
        double scale = newSpeed / currentSpeed;
        this.setDeltaMovement(velocity.x() * scale, velocity.y(), velocity.z() * scale);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.rebounds--;
        result.getEntity().hurt(this.damageSources().source(SuperMarioDamageTypeIds.KOOPA_SHELL, this, this.getOwner()), 2.0F);

        var bounce = true;

        if(result.getEntity() instanceof LivingEntity entity) {
            bounce = entity.isAlive();
        }

        if (bounce) {
            // TODO: make this behaviour configurable
            Vec3 multiplier;
            if (Math.abs(this.getDeltaMovement().x) > Math.abs(this.getDeltaMovement().z)) {
                multiplier = new Vec3(-1.0, 1.0, 1.0);
            } else if (Math.abs(this.getDeltaMovement().x) < Math.abs(this.getDeltaMovement().z)) {
                multiplier = new Vec3(1.0, 1.0, -1.0);
            } else {
                multiplier = new Vec3(-1.0, 1.0, -1.0);
            }

            var vel = this.getDeltaMovement().multiply(multiplier);
            this.setDeltaMovement(vel);
            this.needsSync = true;
            this.playBumpEffects(vel.reverse());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (blockHitResult.getDirection().getAxis() != Direction.Axis.Y) {
            super.onHitBlock(blockHitResult);
        }
    }

    public boolean isStopped() {
        return this.getDeltaMovement().horizontalDistance() == 0.0;
    }

    @Override
    public Predicate<? super Entity> getStompableBy() {
        return super.getStompableBy().and(entity -> this.ownerStompCooldown <= 0 || !entity.equals(this.getOwner()));
    }

    /**
     * Triggers when the shell hits for the final time and should be removed.
     */
    protected void finalHit() {
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.remove(RemovalReason.DISCARDED);
        }
        this.playSound(SuperMarioSounds.KOOPA_SHELL_BREAK, 0.4F, 1.0F);
        //TODO: add particles
    }

    @Override
    protected double getDefaultGravity() {
        return 0.08;
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return !this.isSpectator() && !this.isInLava() && this.isAlive() && !this.isStopped();
    }

    protected void playBumpEffects(Vec3 direction) {
        var center = this.getBoundingBox().getCenter();
        this.playSound(SuperMarioSounds.KOOPA_SHELL_HIT_BLOCK, 1.0F, 1.0F);
        for (int l = 0; l < 8; l++) {
            this.level().addParticle(ParticleTypes.CRIT, center.x, center.y, center.z, direction.x + Math.random() - 0.5, Math.random() * 0.1, direction.z + Math.random() - 0.5);
        }
    }

    public float getHorizontalRotation(float tickDelta) {
        return Mth.lerp(tickDelta, previousHorizontalRotation, horizontalRotation);
    }
}
