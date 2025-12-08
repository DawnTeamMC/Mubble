package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.damage.MubbleDamageTypes;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.util.BoxUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

// TODO: tweak with ProjectileUtil for better collision detection (square projection instead of center?)
public abstract class KoopaShellEntity extends ProjectileEntity {
    public static final String REBOUNDS_KEY = "rebounds";
    protected static final float TARGET_SPEED = 0.5f;
    protected static final float TARGET_SPEED_ACCELERATION = 0.1f;

    protected int rebounds;
    private float previousHorizontalRotation;
    private float horizontalRotation;

    public KoopaShellEntity(EntityType<? extends KoopaShellEntity> entityType, World world, int rebounds) {
        super(entityType, world);
        this.rebounds = rebounds;
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putInt(REBOUNDS_KEY, rebounds);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.rebounds = view.getInt(REBOUNDS_KEY, 3);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    public abstract Identifier getTexture();

    @Override
    public void tick() {
        super.tick();

        boolean isStopped = this.isStopped();

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        this.hitOrDeflect(hitResult);

        this.applyGravity();

        Box hitBox = this.getBoundingBox().offset(this.getVelocity().x > 0 ? 0.01d : -0.01d, 0.0d, this.getVelocity().z > 0 ? 0.01d : -0.01d);

        var prevVelocity = this.getVelocity();
        this.move(MovementType.SELF, prevVelocity);
        var multiplier = BoxUtil.calculateHorizontalBouncingMultiplier(hitBox, BoxUtil.collectPotentialBlockCollisions(this.getEntityWorld(), hitBox));
        if (!isStopped) {
            if (multiplier != null) {
                prevVelocity = prevVelocity.multiply(multiplier);
                this.rebounds--;
                this.playBumpEffects(prevVelocity.negate());
            }
            this.setVelocity(prevVelocity.getX(), this.getVelocity().getY(), prevVelocity.getZ());
            if (this.isOnGround()) {
                //TODO: make this configurable
                this.targetHorizontalSpeed(TARGET_SPEED, TARGET_SPEED_ACCELERATION);
            }
            this.velocityDirty = true;
        }

        if (this.getEntityWorld().isClient()) {
            this.tickRotation();
        }

        if (this.rebounds <= 0) {
            this.finalHit();
        }
    }

    public void tickRotation() {
        float velocityLength = (float) this.getVelocity().horizontalLength();
        this.previousHorizontalRotation = this.horizontalRotation;
        this.horizontalRotation = this.previousHorizontalRotation + velocityLength * 0.45f;
    }

    public void targetHorizontalSpeed(float targetSpeed, float acceleration) {
        Vec3d velocity = this.getVelocity();
        if (velocity.x == 0 && velocity.z == 0) {
            return;
        }
        double currentSpeed = velocity.horizontalLength();
        double scale;
        if (currentSpeed > targetSpeed) {
            scale = Math.min(currentSpeed + acceleration, targetSpeed) / currentSpeed;
        } else {
            scale = Math.max(currentSpeed - acceleration, targetSpeed) / currentSpeed;
        }
        this.setVelocity(velocity.getX() * scale, velocity.getY(), velocity.getZ() * scale);
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        this.rebounds--;
        result.getEntity().serverDamage(this.getDamageSources().create(MubbleDamageTypes.KOOPA_SHELL, this, this.getOwner()), 2.0F);

        var bounce = true;

        if(result.getEntity() instanceof LivingEntity entity) {
            bounce = entity.isAlive();
        }

        if (bounce) {
            // TODO: make this behaviour configurable
            Vec3d multiplier;
            // TODO: this is ugly
            if (Math.abs(this.getVelocity().x) > Math.abs(this.getVelocity().y)) {
                multiplier = new Vec3d(-1.0, 1.0, 1.0);
            } else if (Math.abs(this.getVelocity().x) < Math.abs(this.getVelocity().y)) {
                multiplier = new Vec3d(1.0, 1.0, -1.0);
            } else {
                multiplier = new Vec3d(1.0, 1.0, 1.0);
            }

            var vel = this.getVelocity().multiply(multiplier);
            this.setVelocity(vel);
            this.velocityDirty = true;
            this.playBumpEffects(vel.negate());
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (blockHitResult.getSide().getAxis() != Direction.Axis.Y) {
            super.onBlockHit(blockHitResult);
        }
    }

    public boolean isStopped() {
        return this.getVelocity().horizontalLength() == 0.0;
    }

    /**
     * Triggers when the shell hits for the final time and should be removed.
     */
    protected void finalHit() {
        if (!this.getEntityWorld().isClient()) {
            this.getEntityWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.remove(RemovalReason.DISCARDED);
        }
        this.playSound(MubbleSounds.KOOPA_SHELL_BREAK, 0.4F, 1.0F);
        //TODO: add particles
    }

    @Override
    protected double getGravity() {
        return 0.08;
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return !this.isSpectator() && !this.isInLava() && this.isAlive() && !this.isStopped();
    }

    protected void playBumpEffects(Vec3d direction) {
        var center = this.getBoundingBox().getCenter();
        this.playSound(MubbleSounds.KOOPA_SHELL_HIT_BLOCK, 1.0F, 1.0F);
        for (int l = 0; l < 8; l++) {
            this.getEntityWorld().addParticleClient(ParticleTypes.CRIT, center.x, center.y, center.z, direction.x + Math.random() - 0.5, direction.y + Math.random() - 0.5, direction.z + Math.random() - 0.5);
        }
    }

    public float getHorizontalRotation(float tickDelta) {
        return MathHelper.lerp(tickDelta, previousHorizontalRotation, horizontalRotation);
    }
}
