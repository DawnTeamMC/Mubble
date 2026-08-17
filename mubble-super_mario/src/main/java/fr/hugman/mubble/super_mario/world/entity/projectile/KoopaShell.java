package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

// TODO: tweak with ProjectileUtil for better collision detection (square projection instead of center?)
public abstract class KoopaShell extends Projectile {
    public static final String REBOUNDS_KEY = "rebounds";

    /** Horizontal speed a sliding shell settles on, in blocks per tick. */
    public static final float TARGET_SPEED = 0.5f;
    /** How much of {@link #TARGET_SPEED} a grounded shell may gain or lose in a single tick. */
    protected static final float TARGET_SPEED_ACCELERATION = 0.1f;
    /**
     * Ticks during which a freshly thrown or kicked shell cannot be stomped.
     * <p>
     * Without it, throwing a shell while airborne means immediately landing on it, which stops it right at
     * the thrower's feet.
     */
    protected static final int STOMP_IMMUNITY_TICKS = 10;
    /** Horizontal lengths below this are treated as zero, as directions cannot be derived from them. */
    protected static final double HORIZONTAL_EPSILON = 1.0e-4;
    /**
     * How far the distance actually travelled may differ from the requested one before counting as a
     * collision. Mirrors the tolerance vanilla itself uses to flag horizontal collisions.
     */
    private static final double COLLISION_TOLERANCE = 1.0e-4;

    private static final float FULL_TURN = (float) (Math.PI * 2.0);
    /** Radians the shell model spins by per block travelled. */
    private static final float SPIN_PER_BLOCK = 0.45f;
    private static final int BUMP_PARTICLE_COUNT = 8;
    private static final double BUMP_PARTICLE_SPEED = 0.25;
    private static final double BUMP_PARTICLE_SPREAD = 0.1;

    protected int rebounds;
    private int stompImmunity;
    private float previousHorizontalRotation;
    private float horizontalRotation;

    public KoopaShell(EntityType<? extends KoopaShell> type, Level level, int rebounds) {
        super(type, level);
        this.rebounds = rebounds;
        this.stompImmunity = STOMP_IMMUNITY_TICKS;
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

        if (this.stompImmunity > 0) {
            this.stompImmunity--;
        }

        // a shell at a standstill only falls: it neither bounces nor tries to reach its cruising speed
        boolean wasStopped = this.isStopped();

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        this.hitTargetOrDeflectSelf(hitResult);
        if (this.isRemoved()) {
            return;
        }

        this.applyGravity();

        var requestedMovement = this.getDeltaMovement();
        var previousPosition = this.position();
        this.move(MoverType.SELF, requestedMovement);

        if (!wasStopped) {
            this.bounceOffWalls(requestedMovement, this.position().subtract(previousPosition));
            if (this.isRemoved()) {
                return;
            }
            if (this.onGround()) {
                //TODO: make this configurable
                this.targetHorizontalSpeed(TARGET_SPEED, TARGET_SPEED_ACCELERATION);
            }
        }

        if (this.level().isClientSide()) {
            this.tickRotation();
        }
    }

    /**
     * Reflects the shell's horizontal velocity on every axis the last {@link #move} call was blocked on.
     * <p>
     * Comparing the requested movement against the distance actually travelled lets the vanilla collision
     * code decide what a wall is: anything the shell can step over (see {@link #maxUpStep()}) or slide past
     * is not one, and both axes can be reflected at once when the shell runs into a corner.
     *
     * @param requestedMovement the movement handed over to {@link #move}
     * @param actualMovement    the movement that ended up being applied
     */
    private void bounceOffWalls(Vec3 requestedMovement, Vec3 actualMovement) {
        boolean blockedX = Math.abs(requestedMovement.x() - actualMovement.x()) > COLLISION_TOLERANCE;
        boolean blockedZ = Math.abs(requestedMovement.z() - actualMovement.z()) > COLLISION_TOLERANCE;
        if (!blockedX && !blockedZ) {
            return;
        }

        // TODO: make this behaviour configurable
        var bounced = new Vec3(
                blockedX ? -requestedMovement.x() : requestedMovement.x(),
                this.getDeltaMovement().y(),
                blockedZ ? -requestedMovement.z() : requestedMovement.z());
        this.setDeltaMovement(bounced);
        this.needsSync = true;
        this.playBumpEffects(bounced);
        this.consumeRebound();
    }

    public void tickRotation() {
        float spin = (float) this.getDeltaMovement().horizontalDistance() * SPIN_PER_BLOCK;
        this.previousHorizontalRotation = this.horizontalRotation;
        this.horizontalRotation = this.previousHorizontalRotation + spin;
        // keep the angle small enough for floats to stay precise, without breaking the interpolation
        if (this.horizontalRotation >= FULL_TURN) {
            this.previousHorizontalRotation -= FULL_TURN;
            this.horizontalRotation -= FULL_TURN;
        }
    }

    /**
     * Accelerates or decelerates the shell towards the given horizontal speed, leaving its direction and
     * vertical velocity untouched.
     *
     * @param targetSpeed  the horizontal speed to approach, in blocks per tick
     * @param acceleration the largest horizontal speed change allowed for this tick
     */
    public void targetHorizontalSpeed(float targetSpeed, float acceleration) {
        var velocity = this.getDeltaMovement();
        if (!hasHorizontalDirection(velocity)) {
            return;
        }
        double currentSpeed = velocity.horizontalDistance();
        double newSpeed = currentSpeed > targetSpeed
                ? Math.max(currentSpeed - acceleration, targetSpeed)
                : Math.min(currentSpeed + acceleration, targetSpeed);
        double scale = newSpeed / currentSpeed;
        this.setDeltaMovement(velocity.x() * scale, velocity.y(), velocity.z() * scale);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        var entity = result.getEntity();
        entity.hurt(this.damageSources().source(SuperMarioDamageTypeIds.KOOPA_SHELL, this, this.getOwner()), 2.0F);

        // TODO: make this behaviour configurable
        if (!(entity instanceof LivingEntity living) || living.isAlive()) {
            // reflecting on the axis between both entities makes glancing hits deflect instead of turning back
            var bounced = reflectHorizontally(this.getDeltaMovement(), horizontalDirection(this.position().subtract(entity.position())));
            this.setDeltaMovement(bounced);
            this.needsSync = true;
            this.playBumpEffects(bounced);
        }

        this.consumeRebound();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (blockHitResult.getDirection().getAxis() != Direction.Axis.Y) {
            super.onHitBlock(blockHitResult);
        }
    }

    public boolean isStopped() {
        return !hasHorizontalDirection(this.getDeltaMovement());
    }

    @Override
    public boolean canBeStomped() {
        return super.canBeStomped() && this.stompImmunity <= 0;
    }

    /**
     * Makes the shell immune to stomping for the next {@link #STOMP_IMMUNITY_TICKS} ticks, so that whoever
     * just sent it on its way cannot stop it on the spot.
     */
    protected void grantStompImmunity() {
        this.stompImmunity = STOMP_IMMUNITY_TICKS;
    }

    /**
     * Spends one of the shell's remaining rebounds, breaking it once none are left.
     * <p>
     * Rebounds are only tracked on the server: the client keeps bouncing the shell around until the removal
     * is synced to it, which avoids it breaking twice or breaking on a rebound the server never saw.
     */
    protected void consumeRebound() {
        if (this.level().isClientSide()) {
            return;
        }
        this.rebounds--;
        if (this.rebounds <= 0) {
            this.finalHit();
        }
    }

    /**
     * Triggers when the shell hits for the final time and should be removed.
     */
    protected void finalHit() {
        if (this.level().isClientSide()) {
            return;
        }
        this.playSound(SuperMarioSounds.KOOPA_SHELL_BREAK, 0.4F, 1.0F);
        this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
        this.remove(RemovalReason.DISCARDED);
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

    /**
     * Plays the sound and particles of the shell bumping into something.
     * <p>
     * The sound is only played from the server, which broadcasts it to everyone in range, while the sparks
     * are only spawned on the client, where particles exist in the first place.
     *
     * @param direction the direction the sparks fly towards; only its horizontal part is used
     */
    protected void playBumpEffects(Vec3 direction) {
        if (!this.level().isClientSide()) {
            this.playSound(SuperMarioSounds.KOOPA_SHELL_HIT_BLOCK, 1.0F, 1.0F);
            return;
        }
        var center = this.getBoundingBox().getCenter();
        var spray = horizontalDirection(direction).scale(BUMP_PARTICLE_SPEED);
        for (int i = 0; i < BUMP_PARTICLE_COUNT; i++) {
            this.level().addParticle(ParticleTypes.CRIT, center.x, center.y, center.z,
                    spray.x + this.spread(),
                    this.spread(),
                    spray.z + this.spread());
        }
    }

    private double spread() {
        return (this.random.nextDouble() - 0.5) * BUMP_PARTICLE_SPREAD;
    }

    public float getHorizontalRotation(float tickDelta) {
        return Mth.lerp(tickDelta, previousHorizontalRotation, horizontalRotation);
    }

    /**
     * @return whether the given vector reaches far enough horizontally for a direction to be derived from it
     */
    protected static boolean hasHorizontalDirection(Vec3 vec) {
        return vec.horizontalDistance() >= HORIZONTAL_EPSILON;
    }

    /**
     * @return the horizontal part of the given vector, normalized, or {@link Vec3#ZERO} when it is too short
     * to point anywhere
     */
    protected static Vec3 horizontalDirection(Vec3 vec) {
        var horizontal = vec.multiply(1.0d, 0.0d, 1.0d);
        return hasHorizontalDirection(horizontal) ? horizontal.normalize() : Vec3.ZERO;
    }

    /**
     * Reflects a velocity on a vertical plane, keeping its speed.
     *
     * @param velocity the velocity to reflect
     * @param normal   the horizontal normal of the plane, normalized; {@link Vec3#ZERO} reverses the
     *                 horizontal velocity instead
     * @return the reflected velocity, with its vertical component untouched
     */
    protected static Vec3 reflectHorizontally(Vec3 velocity, Vec3 normal) {
        if (!hasHorizontalDirection(normal)) {
            return new Vec3(-velocity.x(), velocity.y(), -velocity.z());
        }
        var horizontal = velocity.multiply(1.0d, 0.0d, 1.0d);
        double approach = horizontal.dot(normal);
        if (approach >= 0.0d) {
            // already heading away from the obstacle, reflecting would send the shell back into it
            return velocity;
        }
        var reflected = horizontal.subtract(normal.scale(2.0d * approach));
        return new Vec3(reflected.x(), velocity.y(), reflected.z());
    }

    /**
     * @return a random horizontal unit vector, used as a last resort when no meaningful direction can be
     * derived from the situation
     */
    protected Vec3 randomHorizontalDirection() {
        double angle = this.random.nextDouble() * FULL_TURN;
        return new Vec3(Math.cos(angle), 0.0d, Math.sin(angle));
    }
}
