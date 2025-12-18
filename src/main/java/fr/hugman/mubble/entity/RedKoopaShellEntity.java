package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.MubbleItems;
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

public class RedKoopaShellEntity extends KoopaShellEntity {
    private static final Identifier TEXTURE = Mubble.id("textures/entity/red_koopa_shell.png");

    private static final double MAX_TARGET_DISTANCE = 16.0;
    private static final double MAX_TARGET_DISTANCE_SQUARE = MAX_TARGET_DISTANCE * MAX_TARGET_DISTANCE;
    private static final TargetingConditions TARGET_PREDICATE = TargetingConditions.forCombat()
            .range(MAX_TARGET_DISTANCE)
            .ignoreLineOfSight()
            .ignoreInvisibilityTesting()
            .selector((target, w) -> target.attackable());

    private LivingEntity target;

    public RedKoopaShellEntity(EntityType<? extends RedKoopaShellEntity> entityType, Level world) {
        super(entityType, world, 1);
    }

    public RedKoopaShellEntity(Level world, double x, double y, double z) {
        this(MubbleEntityTypes.RED_KOOPA_SHELL, world);
        this.setPos(x, y, z);
    }

    public RedKoopaShellEntity(Level world, LivingEntity owner) {
        this(world, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
        this.setOwner(owner);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(MubbleItems.RED_KOOPA_SHELL);
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
            Vec3 currentPosition = this.position();
            Vec3 targetPosition = this.target.position();
            Vec3 desiredVelocity = targetPosition.subtract(currentPosition).with(Direction.Axis.Y, 0).normalize().scale(0.5);

            Vec3 currentVelocity = this.getDeltaMovement();
            Vec3 velocityError = desiredVelocity.subtract(currentVelocity);
            double pGain = 0.1;
            double dGain = 0.05;

            Vec3 controlSignal = velocityError.scale(pGain).add(velocityError.subtract(currentVelocity).scale(dGain));
            this.setDeltaMovement(currentVelocity.add(controlSignal).normalize().scale(currentVelocity.length()));
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
        if (world instanceof ServerLevel serverWorld && this.getOwner() instanceof LivingEntity livingOwner) {
            if (this.target == null || this.target.distanceToSqr(this) > MAX_TARGET_DISTANCE_SQUARE) {
                this.target = serverWorld.getNearestEntity(
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
}
