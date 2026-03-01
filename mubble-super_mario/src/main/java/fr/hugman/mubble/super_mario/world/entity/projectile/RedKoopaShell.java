package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.item.SuperMarioItems;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RedKoopaShell extends KoopaShell {
    private static final Identifier TEXTURE = SuperMario.id("textures/entity/red_koopa_shell.png");

    private static final double MAX_TARGET_DISTANCE = 16.0;
    private static final double MAX_TARGET_DISTANCE_SQUARE = MAX_TARGET_DISTANCE * MAX_TARGET_DISTANCE;
    private static final double HOMING_TURN_RATE = 0.25;
    private static final TargetingConditions TARGET_PREDICATE = TargetingConditions.forCombat()
            .range(MAX_TARGET_DISTANCE)
            .ignoreLineOfSight()
            .ignoreInvisibilityTesting()
            .selector((target, w) -> target.attackable());

    private LivingEntity target;

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
    public void tick() {
        this.searchTarget();

        if (this.target != null && (this.target.isSpectator() || this.target.isDeadOrDying())) {
            this.target = null;
        }

        if (this.target != null && !this.level().isClientSide()) {
            Vec3 desiredVelocity = this.target.position().subtract(this.position()).with(Direction.Axis.Y, 0).normalize().scale(TARGET_SPEED);

            Vec3 currentVelocity = this.getDeltaMovement();
            Vec3 currentHorizontal = currentVelocity.multiply(1, 0, 1);
            Vec3 newHorizontalVelocity = currentHorizontal.add(desiredVelocity.subtract(currentHorizontal).scale(HOMING_TURN_RATE));
            if (newHorizontalVelocity.lengthSqr() < 1e-10) {
                newHorizontalVelocity = desiredVelocity;
            } else {
                newHorizontalVelocity = newHorizontalVelocity.normalize().scale(TARGET_SPEED);
            }
            this.setDeltaMovement(newHorizontalVelocity.x(), currentVelocity.y(), newHorizontalVelocity.z());
            this.needsSync = true;
        }

        super.tick();
    }

    @Override
    public void targetHorizontalSpeed(float targetSpeed, float acceleration) {
        if (this.target == null) {
            super.targetHorizontalSpeed(targetSpeed, acceleration);
        }
    }

    private void searchTarget() {
        var world = this.level();
        if (world instanceof ServerLevel serverLevel && this.getOwner() instanceof LivingEntity livingOwner) {
            if (this.target == null || this.target.distanceToSqr(this) > MAX_TARGET_DISTANCE_SQUARE) {
                this.target = serverLevel.getNearestEntity(
                        this.level().getEntitiesOfClass(LivingEntity.class, this.getSearchBox(MAX_TARGET_DISTANCE), livingEntity -> true),
                        TARGET_PREDICATE,
                        livingOwner,
                        this.getX(),
                        this.getEyeY(),
                        this.getZ());
            }
        }
    }

    protected AABB getSearchBox(double distance) {
        return this.getBoundingBox().inflate(distance, distance, distance);
    }

    @Override
    public float maxUpStep() {
        return 1.0f;
    }
}
