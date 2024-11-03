package fr.hugman.mubble.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public abstract class BallEntity extends ThrownEntity {
    protected int reboundingAmount = 3;

    protected BallEntity(EntityType<? extends BallEntity> type, World world) {
        super(type, world);
    }

    protected BallEntity(EntityType<? extends BallEntity> type, World world, LivingEntity owner) {
        super(type, world);
        this.setOwner(owner);
    }

    protected BallEntity(EntityType<? extends BallEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    protected abstract SoundEvent getDeathSound();

    protected abstract ParticleEffect getDeathParticle();

    @Override
    protected void onCollision(HitResult result) {
        this.reboundingAmount--;
        super.onCollision(result);
        if (this.isAlive() && this.reboundingAmount < 0) {
            this.finalHit();
        }
    }

    protected void finalHit() {
        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.remove(RemovalReason.DISCARDED);
        }
        this.getWorld().playSound(null, getX(), getY(), getZ(), this.getDeathSound(), SoundCategory.NEUTRAL, 0.5F, 1.0F);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("ReboundingAmount", reboundingAmount);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.reboundingAmount = compound.getInt("ReboundingAmount");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleStatus(byte state) {
        if (state == 3) {
            this.spawnDeathParticles();
        }
    }

    protected void spawnDeathParticles() {
        for (int i = 0; i < 8; ++i) {
            float s1 = random.nextFloat() * 0.2F - 0.1F;
            float s2 = random.nextFloat() * 0.2F - 0.1F;
            float s3 = random.nextFloat() * 0.2F - 0.1F;
            this.getEntityWorld().addParticle(this.getDeathParticle(), this.getX(), this.getY(), this.getZ(), s1, s2, s3);
        }
    }
}
