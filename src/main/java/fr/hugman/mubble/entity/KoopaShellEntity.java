package fr.hugman.mubble.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class KoopaShellEntity extends ProjectileEntity {
    protected KoopaShellEntity(EntityType<? extends KoopaShellEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    public void tick() {
        this.applyGravity();
        var currentMovement = this.getVelocity();
        this.move(MovementType.SELF, currentMovement);
        this.checkBlockCollision(currentMovement);
    }

    @Override
    protected double getGravity() {
        return 0.08D;
    }

    protected void checkBlockCollision(Vec3d previousMovement) {
        Box shellBox = this.getBoundingBox().expand(1.0E-4, 0, 1.0E-4);
        var minPos = BlockPos.ofFloored(shellBox.minX, shellBox.minY, shellBox.minZ);
        var maxPos = BlockPos.ofFloored(shellBox.maxX, shellBox.maxY, shellBox.maxZ);
        if (this.getWorld().isRegionLoaded(minPos, maxPos)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            double xDiff = 0;
            double zDiff = 0;
            reboundLoop:
            for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
                for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                    for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                        if (!this.isAlive()) {
                            return;
                        }

                        mutable.set(x, y, z);
                        BlockState blockState = this.getWorld().getBlockState(mutable);
                        VoxelShape voxelShape = blockState.getCollisionShape(this.getWorld(), minPos);
                        if (!voxelShape.isEmpty()) {
                            Box collisionBox = null;
                            for (Box box : voxelShape.getBoundingBoxes()) {
                                var offsetBox = box.offset(minPos);
                                if (offsetBox.intersects(shellBox)) {
                                    collisionBox = offsetBox;
                                    break;
                                }
                            }
                            if (collisionBox != null) {
                                if (previousMovement.x > 0.0D && collisionBox.maxX > shellBox.minX) {
                                    xDiff = Math.abs(shellBox.minX - collisionBox.maxX);
                                }
                                if (previousMovement.x < 0.0D && collisionBox.minX < shellBox.maxX) {
                                    xDiff = Math.abs(collisionBox.minX - shellBox.maxX);
                                }
                                if (previousMovement.z > 0.0D && collisionBox.maxZ > shellBox.minZ) {
                                    zDiff = Math.abs(shellBox.minZ - collisionBox.maxZ);
                                }
                                if (previousMovement.z < 0.0D && collisionBox.minZ < shellBox.maxZ) {
                                    zDiff = Math.abs(collisionBox.minZ - shellBox.maxZ);
                                }
                                try {
                                    blockState.onEntityCollision(this.getWorld(), mutable, this);
                                    this.onBlockCollision(blockState);
                                } catch (Throwable var12) {
                                    CrashReport crashReport = CrashReport.create(var12, "Colliding entity with block");
                                    CrashReportSection crashReportSection = crashReport.addElement("Block being collided with");
                                    CrashReportSection.addBlockInfo(crashReportSection, this.getWorld(), mutable, blockState);
                                    throw new CrashException(crashReport);
                                }
                                break reboundLoop;
                            }
                        }
                    }
                }
            }
            if (xDiff > zDiff) {
                this.setVelocity(previousMovement.multiply(-1, 1, 1));
            }
            if (zDiff > xDiff) {
                this.setVelocity(previousMovement.multiply(1, 1, -1));
            }
            //TODO: play sound
        }
    }
}
