package fr.hugman.mubble.entity;

import fr.hugman.mubble.entity.damage.MubbleDamageTypeKeys;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.util.BoxUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class KoopaShellEntity extends ProjectileEntity {
    private static final float TARGET_SPEED = 0.5f;
    private float previousHorizontalRotation;
    private float horizontalRotation;

    public KoopaShellEntity(EntityType<? extends KoopaShellEntity> entityType, World world) {
        super(entityType, world);
    }

    public KoopaShellEntity(EntityType<? extends KoopaShellEntity> entityType, World world, double x, double y, double z) {
        this(entityType, world);
        this.setPosition(x, y, z);
    }

    public KoopaShellEntity(EntityType<? extends KoopaShellEntity> entityType, World world, LivingEntity owner) {
        this(entityType, world, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
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
    public boolean shouldSpawnSprintingParticles() {
        return !this.isSpectator() && !this.isInLava() && this.isAlive();
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
            this.playSound(MubbleSounds.KOOPA_SHELL_HIT_BLOCK, 1.0F, 1.0F);
            // TODO: spawn particles

        }
        this.setVelocity(prevVelocity.getX(), this.getVelocity().getY(), prevVelocity.getZ());

        if (this.isOnGround()) {
            //TODO: make this changeable
            this.targetSpeed(TARGET_SPEED);
        }

        if (this.getWorld().isClient) {
            this.tickRotation();
        }
    }

    public void tickRotation() {
        float velocityLength = (float) this.getVelocity().horizontalLength();
        this.previousHorizontalRotation = this.horizontalRotation;
        this.horizontalRotation = this.previousHorizontalRotation + velocityLength * 0.35f;
    }

    public void targetSpeed(float targetSpeed) {
        Vec3d velocity = this.getVelocity();
        double currentSpeed = Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());

        if (currentSpeed < targetSpeed) {
            double scale = Math.min(currentSpeed + 0.01, targetSpeed) / currentSpeed;
            this.setVelocity(velocity.getX() * scale, velocity.getY(), velocity.getZ() * scale);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        result.getEntity().serverDamage(this.getDamageSources().create(MubbleDamageTypeKeys.KOOPA_SHELL, this, this.getOwner()), 2.0F);
        // TODO: play sound
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (blockHitResult.getSide().getAxis() != Direction.Axis.Y) {
            super.onBlockHit(blockHitResult);
        }
    }

    public abstract Identifier getTexture();

    public float getHorizontalRotation() {
        return this.horizontalRotation;
    }

    public float getPreviousHorizontalRotation() {
        return this.previousHorizontalRotation;
    }
}
