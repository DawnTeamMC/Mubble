package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RedKoopaShell extends KoopaShell {
    private static final Identifier TEXTURE = SuperMario.id("textures/entity/red_koopa_shell.png");

    private static final double MAX_TARGET_DISTANCE = 16.0;
    private static final double MAX_TARGET_DISTANCE_SQUARE = MAX_TARGET_DISTANCE * MAX_TARGET_DISTANCE;
    /**
     * Ticks between two searches for a target. Looking around is by far the most expensive thing the shell
     * does, and a target one tick out of date is close enough for something moving at most half a block per
     * tick.
     */
    private static final int TARGET_SEARCH_INTERVAL = 5;
    /**
     * Largest angle, in degrees, the shell may turn by in a single tick.
     * <p>
     * A capped turn rate gives the shell a fixed turn radius -- its speed divided by the rate in radians,
     * so a block and a half here -- which is what keeps it homing in instead of orbiting.
     */
    private static final float TURN_RATE = 20.0f;
    /**
     * Distance to the target under which the shell stops steering and simply charges ahead.
     * <p>
     * No turn radius is tight enough to correct a course this late, so trying to would only make the shell
     * curve around its target and miss it.
     */
    private static final double LOCK_ON_DISTANCE = 1.5;
    private static final double LOCK_ON_DISTANCE_SQUARE = LOCK_ON_DISTANCE * LOCK_ON_DISTANCE;
    /** Height the shell climbs over on its own, so that terrain does not stop it a block into its course. */
    private static final float STEP_HEIGHT = 1.0f;

    private static final TargetingConditions TARGET_PREDICATE = TargetingConditions.forCombat()
            .range(MAX_TARGET_DISTANCE)
            .ignoreLineOfSight()
            .ignoreInvisibilityTesting()
            .selector((target, w) -> target.attackable());

    @Nullable
    private LivingEntity target;
    private int targetSearchCooldown;

    public RedKoopaShell(EntityType<? extends RedKoopaShell> entityType, Level level) {
        super(entityType, level, 1);
    }

    public RedKoopaShell(Level level, double x, double y, double z) {
        this(SuperMarioEntityTypes.RED_KOOPA_SHELL, level);
        this.setPos(x, y, z);
    }

    public RedKoopaShell(Level level, LivingEntity owner) {
        this(level, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
        this.setOwner(owner);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(SuperMarioItems.RED_KOOPA_SHELL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public float maxUpStep() {
        return STEP_HEIGHT;
    }

    @Override
    public void tick() {
        // homing only happens on the server, which is the only side that knows about the target
        if (!this.level().isClientSide()) {
            this.updateTarget();
            if (this.target != null) {
                this.steerTowards(this.target);
            }
        }

        super.tick();
    }

    /**
     * Steers the shell towards the given entity, turning by at most {@link #TURN_RATE} degrees.
     * <p>
     * Rotating the velocity by a capped angle is what makes this reliable: unlike interpolating towards the
     * wanted velocity, it turns just as fast whatever the angle to cover, and it cannot stall when the shell
     * happens to be running exactly away from its target.
     */
    private void steerTowards(LivingEntity target) {
        if (this.distanceToSqr(target) < LOCK_ON_DISTANCE_SQUARE) {
            return;
        }
        var toTarget = target.position().subtract(this.position());
        if (!hasHorizontalDirection(toTarget)) {
            // the target sits straight above or below the shell: there is nowhere to steer towards
            return;
        }

        var velocity = this.getDeltaMovement();
        float wantedAngle = angleOf(toTarget);
        float angle = hasHorizontalDirection(velocity)
                ? approachAngle(angleOf(velocity), wantedAngle, TURN_RATE)
                : wantedAngle;

        double radians = Math.toRadians(angle);
        this.setDeltaMovement(Math.cos(radians) * TARGET_SPEED, velocity.y(), Math.sin(radians) * TARGET_SPEED);
        this.needsSync = true;
    }

    /**
     * Drops the current target once it is gone or out of reach, and looks for a new one every
     * {@link #TARGET_SEARCH_INTERVAL} ticks while the shell has none.
     */
    private void updateTarget() {
        if (this.target != null && !this.isValidTarget(this.target)) {
            this.target = null;
        }
        if (this.target != null) {
            return;
        }
        if (this.targetSearchCooldown > 0) {
            this.targetSearchCooldown--;
            return;
        }
        this.targetSearchCooldown = TARGET_SEARCH_INTERVAL;
        this.target = this.findTarget();
    }

    private boolean isValidTarget(LivingEntity target) {
        return target.isAlive() && !target.isSpectator() && target.distanceToSqr(this) <= MAX_TARGET_DISTANCE_SQUARE;
    }

    @Nullable
    private LivingEntity findTarget() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        // an owner-less shell, thrown by a dispenser for instance, still homes in: it just has no one to spare
        Entity owner = this.getOwner();
        var candidates = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getSearchBox(MAX_TARGET_DISTANCE), candidate -> candidate != owner);
        return serverLevel.getNearestEntity(
                candidates,
                TARGET_PREDICATE,
                owner instanceof LivingEntity livingOwner ? livingOwner : null,
                this.getX(),
                this.getEyeY(),
                this.getZ());
    }

    protected AABB getSearchBox(double distance) {
        return this.getBoundingBox().inflate(distance, distance, distance);
    }

    /**
     * @return the angle of the horizontal part of the given vector, in degrees
     */
    private static float angleOf(Vec3 vec) {
        return (float) Math.toDegrees(Math.atan2(vec.z(), vec.x()));
    }

    /**
     * Moves an angle towards another one by at most {@code maxDelta} degrees, taking the shortest way around.
     */
    private static float approachAngle(float from, float to, float maxDelta) {
        return from + Mth.clamp(Mth.wrapDegrees(to - from), -maxDelta, maxDelta);
    }
}
