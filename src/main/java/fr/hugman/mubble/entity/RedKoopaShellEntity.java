package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.item.MubbleItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// TODO: break on impact
public class RedKoopaShellEntity extends KoopaShellEntity {
    private static final Identifier TEXTURE = Mubble.id("textures/entity/koopa_shell/red.png");

    private static final double MAX_TARGET_DISTANCE = 16.0;
    private static final double MAX_TARGET_DISTANCE_SQUARE = MAX_TARGET_DISTANCE * MAX_TARGET_DISTANCE;
    private static final TargetPredicate TARGET_PREDICATE = TargetPredicate.createAttackable()
            .setBaseMaxDistance(MAX_TARGET_DISTANCE)
            .ignoreVisibility()
            .ignoreDistanceScalingFactor()
            .setPredicate((target, w) -> target.isMobOrPlayer());

    private LivingEntity target;

    public RedKoopaShellEntity(EntityType<? extends RedKoopaShellEntity> entityType, World world) {
        super(entityType, world);
    }

    public RedKoopaShellEntity(World world, double x, double y, double z) {
        super(MubbleEntityTypes.RED_KOOPA_SHELL, world, x, y, z);
    }

    public RedKoopaShellEntity(World world, LivingEntity owner) {
        super(MubbleEntityTypes.RED_KOOPA_SHELL, world, owner);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(MubbleItems.RED_KOOPA_SHELL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public void tick() {
        if (this.age % 20 == 1) {
            this.searchTarget();
        }

        if (this.target != null && (this.target.isSpectator() || this.target.isDead())) {
            this.target = null;
        }

        if (this.target != null && !this.getWorld().isClient) {
            Vec3d currentPosition = this.getPos();
            Vec3d targetPosition = this.target.getPos();
            Vec3d desiredVelocity = targetPosition.subtract(currentPosition).withAxis(Direction.Axis.Y, 0).normalize().multiply(0.5);

            Vec3d currentVelocity = this.getVelocity();
            Vec3d velocityError = desiredVelocity.subtract(currentVelocity);
            double pGain = 0.1;
            double dGain = 0.05;

            Vec3d controlSignal = velocityError.multiply(pGain).add(velocityError.subtract(currentVelocity).multiply(dGain));
            this.setVelocity(currentVelocity.add(controlSignal).normalize().multiply(currentVelocity.length()));
            this.velocityDirty = true;
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
        var world = this.getWorld();
        if (world instanceof ServerWorld serverWorld && this.getOwner() instanceof LivingEntity livingOwner) {
            if (this.target == null || this.target.squaredDistanceTo(this) > MAX_TARGET_DISTANCE_SQUARE) {
                this.target = serverWorld.getClosestEntity(
                        this.getWorld().getEntitiesByClass(LivingEntity.class, this.getSearchBox(MAX_TARGET_DISTANCE), livingEntity -> true),
                        TARGET_PREDICATE,
                        livingOwner,
                        this.getX(),
                        this.getEyeY(),
                        this.getZ());
            }
        }
    }

    protected Box getSearchBox(double distance) {
        return this.getBoundingBox().expand(distance, distance, distance);
    }
}
