package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeIds;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.attribute.SuperMarioEnvironmentAttributes;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.super_mario.world.entity.freeze.Freezing;
import fr.hugman.mubble.world.attribute.BlockTransform;
import fr.hugman.mubble.world.entity.projectile.Ball;
import net.minecraft.core.BlockPos;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class Iceball extends Ball {
    private static final ClientAsset.ResourceTexture TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/iceball"));

    public Iceball(EntityType<? extends Iceball> type, Level level) {
        super(type, level);
    }

    public Iceball(Level level, LivingEntity owner) {
        super(SuperMarioEntityTypes.ICEBALL, level, owner);
    }

    public Iceball(double x, double y, double z, Level level) {
        super(SuperMarioEntityTypes.ICEBALL, x, y, z, level);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SuperMarioSounds.ICEBALL_HIT_BLOCK.value();
    }

    @Override
    protected ParticleOptions getDeathParticle() {
        return ParticleTypes.CLOUD;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        float damage = entity instanceof SnowGolem ? 1.0F : 3.0F;
        DamageSource source = this.damageSources().source(SuperMarioDamageTypeIds.ICEBALL, this, owner);

        if (owner instanceof LivingEntity livingEntity) {
            livingEntity.setLastHurtMob(entity);
        }

        entity.hurt(source, damage);
        // snow golems are made of the stuff: an ice ball is no more to them than the hit itself
        if (this.level() instanceof ServerLevel level && !(entity instanceof SnowGolem) && entity instanceof LivingEntity living && living.isAlive()) {
            Freezing.freeze(level, living);
        }
        this.finalHit(SuperMarioSounds.ICEBALL_HIT_ENTITY);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        BlockState state = this.level().getBlockState(pos);
        Direction face = result.getDirection();

        BlockState resultState = null;
        Holder<SoundEvent> resultSound = null;
        var transform = BlockTransform.testList(this.level().environmentAttributes().getValue(SuperMarioEnvironmentAttributes.ICEBALL_FREEZES, pos), state.typeHolder());
        if (transform != null) {
            resultState = transform.result();
            resultSound = transform.sound().orElse(null);
        }

        if (resultState != null) {
            if (!this.level().isClientSide()) {
                if (resultState.getBlock() instanceof AirBlock) {
                    this.level().removeBlock(pos, false);
                } else {
                    this.level().setBlockAndUpdate(pos, resultState);
                }
            }
            this.level().playSound(null, getX(), getY(), getZ(), resultSound, SoundSource.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        if (face == Direction.UP) {
			this.reboundUp();
		} else {
            this.finalHit();
        }
    }

    @Override
    public ClientAsset.ResourceTexture getTexture() {
        return TEXTURE;
    }
}