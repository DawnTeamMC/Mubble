package fr.hugman.mubble.super_mario.world.entity.projectile;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.references.SuperMarioDamageTypeKeys;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.attribute.SuperMarioEnvironmentAttributes;
import fr.hugman.mubble.super_mario.world.entity.SuperMarioEntityTypes;
import fr.hugman.mubble.world.attribute.BlockTransform;
import fr.hugman.mubble.world.entity.projectile.Ball;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class Fireball extends Ball {
    private static final ClientAsset.ResourceTexture TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/fireball"));

    public Fireball(EntityType<? extends Fireball> type, Level level) {
        super(type, level);
    }

    public Fireball(Level level, LivingEntity owner) {
        super(SuperMarioEntityTypes.FIREBALL, level, owner);
    }

    public Fireball(double x, double y, double z, Level level) {
        super(SuperMarioEntityTypes.FIREBALL, x, y, z, level);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SuperMarioSounds.FIREBALL_HIT_BLOCK;
    }

    @Override
    protected ParticleOptions getDeathParticle() {
        return ParticleTypes.FLAME;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        float damage = entity.fireImmune() ? 1.0F : 3.0F;

        if (owner instanceof LivingEntity livingEntity) {
            livingEntity.setLastHurtMob(entity);
        }

        if (!entity.fireImmune()) {
            entity.igniteForSeconds(5);
        }
        entity.hurt(this.damageSources().source(SuperMarioDamageTypeKeys.FIREBALL, this, this.getOwner()), damage);
        this.finalHit(SuperMarioSounds.FIREBALL_HIT_ENTITY);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        BlockState state = this.level().getBlockState(pos);
        Direction face = result.getDirection();

        BlockState resultState = null;
        Holder<SoundEvent> resultSound = null;
        var transform = BlockTransform.testList(this.level().environmentAttributes().getValue(SuperMarioEnvironmentAttributes.FIREBALL_MELTS, pos), state.typeHolder());
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
        if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
            this.level().setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, true));
            this.level().gameEvent(this.getOwner(), GameEvent.BLOCK_CHANGE, pos);
            this.level().playSound(null, getX(), getY(), getZ(), SuperMarioSounds.FIREBALL_HIT_BLOCK, SoundSource.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        FlammableBlockRegistry.Entry flammableEntry = FlammableBlockRegistry.getDefaultInstance().get(state.getBlock());
        if (flammableEntry.getBurnOdds() > 0 || flammableEntry.getIgniteOdds() > 0) {
            BlockPos firePos = pos.relative(face);
            if (this.level().isEmptyBlock(firePos) && !this.level().isClientSide()) {
                this.level().setBlockAndUpdate(firePos, BaseFireBlock.getState(this.level(), firePos));
            }
            this.level().playSound(null, getX(), getY(), getZ(), SuperMarioSounds.FIREBALL_HIT_BLOCK, SoundSource.NEUTRAL, 0.5F, 1.0F);
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