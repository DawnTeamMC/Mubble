package fr.hugman.mubble.entity;

import fr.hugman.mubble.util.BoxUtil;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

        Box hitBox = this.getBoundingBox().offset(this.getVelocity().x > 0 ? 0.01d : -0.01d, 0.0d, this.getVelocity().z > 0 ? 0.01d : -0.01d);

        var multiplier = BoxUtil.calculateBouncingMultiplier(hitBox, BoxUtil.collectPotentialBlockCollisions(this.getWorld(), hitBox));
        var prevVelocity = this.getVelocity();
        this.move(MovementType.SELF, prevVelocity);
        if (multiplier != null) {
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
}
