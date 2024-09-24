package fr.hugman.mubble.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.hitOrDeflect(hitResult);
        }

        this.checkBlockCollision();
        Vec3d vec3d = this.getVelocity();
        double x = this.getX() + vec3d.x;
        double y = this.getY() + vec3d.y;
        double z = this.getZ() + vec3d.z;
        this.updateRotation();
        if (this.isTouchingWater()) {
            for (int i = 0; i < 4; i++) {
                float g = 0.25F;
                this.getWorld().addParticle(ParticleTypes.BUBBLE, x - vec3d.x * g, y - vec3d.y * g, z - vec3d.z * g, vec3d.x, vec3d.y, vec3d.z);
            }
        }

        this.applyGravity();
        this.setPosition(x, y, z);
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        Direction face = result.getSide();
        switch (face) {
            case NORTH, SOUTH -> this.setVelocity(this.getVelocity().multiply(1.0D, 1.0D, -1.0D));
            case EAST, WEST -> this.setVelocity(this.getVelocity().multiply(-1.0D, 1.0D, 1.0D));
            case null, default -> {
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), 2.0F);
        // TODO: PLAY SOUND
    }
}
