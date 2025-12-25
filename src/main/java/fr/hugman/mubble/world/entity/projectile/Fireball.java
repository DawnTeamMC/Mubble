package fr.hugman.mubble.world.entity.projectile;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.sounds.MubbleSounds;
import fr.hugman.mubble.world.attribute.BlockTransform;
import fr.hugman.mubble.world.attribute.MubbleEnvironmentAttributes;
import fr.hugman.mubble.world.entity.MubbleEntityTypes;
import fr.hugman.mubble.references.MubbleDamageTypeKeys;
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
import net.minecraft.world.phys.Vec3;

public class Fireball extends Ball {
    private static final ClientAsset.ResourceTexture TEXTURE = new ClientAsset.ResourceTexture(Mubble.id("entity/fireball"));

    public Fireball(EntityType<? extends Fireball> type, Level level) {
        super(type, level);
    }

    public Fireball(Level level, LivingEntity owner) {
        super(MubbleEntityTypes.FIREBALL, level, owner);
    }

    public Fireball(double x, double y, double z, Level level) {
        super(MubbleEntityTypes.FIREBALL, x, y, z, level);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MubbleSounds.FIREBALL_HIT_BLOCK;
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
        this.level().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_ENTITY, SoundSource.NEUTRAL, 0.5F, 1.0F);
        entity.hurt(this.damageSources().source(MubbleDamageTypeKeys.FIREBALL, this, this.getOwner()), damage);
        this.finalHit();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        BlockState state = this.level().getBlockState(pos);
        Direction face = result.getDirection();

        BlockState resultState = null;
        Holder<SoundEvent> resultSound = null;
        var transform = BlockTransform.testList(this.level().environmentAttributes().getValue(MubbleEnvironmentAttributes.FIREBALL_MELTS, pos), state.typeHolder());
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
            this.level().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundSource.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        FlammableBlockRegistry.Entry flammableEntry = FlammableBlockRegistry.getDefaultInstance().get(state.getBlock());
        if (flammableEntry.getBurnChance() > 0 || flammableEntry.getSpreadChance() > 0) {
            BlockPos firePos = pos.relative(face);
            if (this.level().isEmptyBlock(firePos) && !this.level().isClientSide()) {
                this.level().setBlockAndUpdate(firePos, BaseFireBlock.getState(this.level(), firePos));
            }
            this.level().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundSource.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        if (face == Direction.UP) {
            Vec3 motion = this.getDeltaMovement().subtract(0.0D, this.getDeltaMovement().y * 1.25D, 0.0D);
            double minY = 0.4D;
            if (motion.y < minY) {
                motion = motion.with(Direction.Axis.Y, minY);
            }
            this.setDeltaMovement(motion);
        } else {
            this.finalHit();
        }
    }

    @Override
    public ClientAsset.ResourceTexture getTexture() {
        return TEXTURE;
    }
}