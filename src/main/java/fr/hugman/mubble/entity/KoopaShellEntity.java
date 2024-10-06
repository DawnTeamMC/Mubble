package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class KoopaShellEntity extends ProjectileEntity {
    public KoopaShellEntity(EntityType<? extends KoopaShellEntity> entityType, World world) {
        super(entityType, world);
    }

    public KoopaShellEntity(World world, double x, double y, double z) {
        super(MubbleEntityTypes.KOOPA_SHELL, world);
        this.setPosition(x, y, z);
    }

    public KoopaShellEntity(World world, LivingEntity owner) {
        this(world, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
        this.setOwner(owner);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected double getGravity() {
        return 0.08;
    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        this.hitOrDeflect(hitResult);

        this.applyGravity();
        var multiplier = this.calculateBouncingMultiplier();
        var prevVelocity = this.getVelocity();
        this.move(MovementType.SELF, prevVelocity);
        if(multiplier != null) {
            prevVelocity = prevVelocity.multiply(multiplier);
            // TODO: PLAY SOUND
            // TODO: PLAY PARTICLES

        }
        this.setVelocity(prevVelocity.getX(), this.getVelocity().getY(), prevVelocity.getZ());

        this.tickBlockCollision();
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(1.0D, -0.5, 1.0D));
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        result.getEntity().serverDamage(this.getDamageSources().thrown(this, this.getOwner()), 2.0F);
        // TODO: PLAY SOUND
    }

    @Nullable
    public Vec3d calculateBouncingMultiplier() {
        var shellBox = this.getBoundingBox().offset(this.getVelocity().x > 0 ? 0.01d : -0.01d, 0.0d, this.getVelocity().z > 0 ? 0.01d : -0.01d).shrink(0.0d, 0.01d, 0.0d);
        var shellCenter = shellBox.getCenter();
        var radius = (Math.sqrt(2) / 2.0d) * this.getWidth();

        Iterable<BlockPos> iterable = BlockPos.iterate(shellBox);

        double minDistanceX = Double.MAX_VALUE;
        double minDistanceZ = Double.MAX_VALUE;

        for (BlockPos pos : iterable) {
            var boundingBoxes = this.getWorld().getBlockState(pos).getCollisionShape(this.getWorld(), pos).offset(Vec3d.of(pos)).getBoundingBoxes();
            for(Box box : boundingBoxes) {
                double closestX = MathHelper.clamp(shellCenter.getX(), box.minX, box.maxX);
                double closestZ = MathHelper.clamp(shellCenter.getZ(), box.minZ, box.maxZ);
                double distanceX = shellCenter.getX() - closestX;
                double distanceZ = shellCenter.getZ() - closestZ;
                if(Math.sqrt(MathHelper.square(distanceX) + MathHelper.square(distanceZ)) > radius) {
                    continue;
                }
                if(Math.abs(distanceX) < Math.abs(minDistanceX)) {
                    minDistanceX = Math.abs(distanceX);
                }
                if(Math.abs(distanceZ) < Math.abs(minDistanceZ)) {
                    minDistanceZ = Math.abs(distanceZ);
                }
            }
        }
        if(minDistanceX == Double.MAX_VALUE && minDistanceZ == Double.MAX_VALUE) {
            return null;
        }

        if (minDistanceX == minDistanceZ) {
            return new Vec3d(-1.0, 1.0D, -1.0D);
        }
        if (minDistanceX > minDistanceZ) {
            return new Vec3d(-1.0, 1.0D, 1.0D);
        } else {
            return new Vec3d(1.0, 1.0D, -1.0D);
        }
    }
}
