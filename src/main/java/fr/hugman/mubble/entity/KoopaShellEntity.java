package fr.hugman.mubble.entity;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KoopaShellEntity extends ProjectileEntity {
    protected KoopaShellEntity(EntityType<? extends KoopaShellEntity> entityType, World world) {
        super(entityType, world);
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
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        this.updateRotation();
        if (this.isTouchingWater()) {
            for (int i = 0; i < 4; i++) {
                float g = 0.25F;
                this.getWorld().addParticle(ParticleTypes.BUBBLE, d - vec3d.x * g, e - vec3d.y * g, f - vec3d.z * g, vec3d.x, vec3d.y, vec3d.z);
            }
        }

        this.applyGravity();
        this.setPosition(d, e, f);
    }

    @Override
    protected void onCollision(HitResult result) {
        boolean removeOnImpact = true;
        if (result.getType() == HitResult.Type.ENTITY) {
            removeOnImpact = onEntityImpact((EntityHitResult) result);
        } else if (result.getType() == HitResult.Type.BLOCK) {
            removeOnImpact = onBlockImpact((BlockHitResult) result);
        }
        if (removeOnImpact) {
            if (!this.getWorld().isClient) {
                this.getWorld().sendEntityStatus(this, (byte) 3);
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    protected boolean onBlockImpact(BlockHitResult result) {
        Direction face = result.getSide();
        switch (face) {
            case UP -> {
                this.setVelocity(this.getVelocity().multiply(1.0D, -0.5D, 1.0D));
                return false;
            }
            case NORTH, SOUTH -> {
                this.setVelocity(this.getVelocity().multiply(1.0D, 1.0D, -1.0D));
                return false;
            }
            case EAST, WEST -> {
                this.setVelocity(this.getVelocity().multiply(-1.0D, 1.0D, 1.0D));
                return false;
            }
            case null, default -> {
                return false;
            }
        }
    }

    protected boolean onEntityImpact(EntityHitResult result) {
        Entity entity = result.getEntity();
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), 2.0F);
        // TODO: PLAY SOUND
        return true;
    }
}
