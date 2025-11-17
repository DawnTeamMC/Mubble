package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.tag.MubbleBlockTags;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FireballEntity extends BallEntity {
    private static final AssetInfo.TextureAssetInfo TEXTURE = new AssetInfo.TextureAssetInfo(Mubble.id("entity/fireball"));

    public FireballEntity(EntityType<? extends FireballEntity> type, World world) {
        super(type, world);
    }

    public FireballEntity(World world, LivingEntity owner) {
        super(MubbleEntityTypes.FIREBALL, world, owner);
    }

    public FireballEntity(double x, double y, double z, World world) {
        super(MubbleEntityTypes.FIREBALL, x, y, z, world);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MubbleSounds.FIREBALL_HIT_BLOCK;
    }

    @Override
    protected ParticleEffect getDeathParticle() {
        return ParticleTypes.FLAME;
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        float damage = entity.isFireImmune() ? 1.0F : 3.0F;

        if (owner instanceof LivingEntity livingEntity) {
            livingEntity.onAttacking(entity);
        }

        if (!entity.isFireImmune()) {
            entity.setOnFireFor(5);
        }
        this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		entity.serverDamage(this.getDamageSources().thrown(this, this.getOwner()), damage);
        this.finalHit();
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        BlockPos pos = result.getBlockPos();
        BlockState state = this.getEntityWorld().getBlockState(pos);
        Direction face = result.getSide();
        Block resultBlock = null;
        if (state.isIn(MubbleBlockTags.MELTABLE_TO_AIR)) {
            resultBlock = Blocks.AIR;
        } else if (state.isIn(MubbleBlockTags.MELTABLE_TO_ICE)) {
            resultBlock = Blocks.ICE;
        } else if (state.isIn(MubbleBlockTags.MELTABLE_TO_WATER)) {
            resultBlock = Blocks.WATER;
        }
        if (resultBlock != null) {
            if (!this.getEntityWorld().isClient()) {
                if (this.getEntityWorld().getDimension().ultrawarm() || resultBlock instanceof AirBlock) {
                    this.getEntityWorld().removeBlock(pos, false);
                } else {
                    this.getEntityWorld().setBlockState(pos, resultBlock.getDefaultState());
                }
            }
            this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_MELT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        if(CampfireBlock.canBeLit(state) || CandleBlock.canBeLit(state) || CandleCakeBlock.canBeLit(state)) {
            this.getEntityWorld().setBlockState(pos, state.with(CampfireBlock.LIT, true));
            this.getEntityWorld().emitGameEvent(this.getOwner(), GameEvent.BLOCK_CHANGE, pos);
            this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        FlammableBlockRegistry.Entry flammableEntry = FlammableBlockRegistry.getDefaultInstance().get(state.getBlock());
        if (flammableEntry.getBurnChance() > 0 || flammableEntry.getSpreadChance() > 0) {
            BlockPos firePos = pos.offset(face);
            if (this.getEntityWorld().isAir(firePos) && !this.getEntityWorld().isClient()) {
                this.getEntityWorld().setBlockState(firePos, AbstractFireBlock.getState(this.getEntityWorld(), firePos));
            }
            this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        if (face == Direction.UP) {
            Vec3d motion = this.getVelocity().subtract(0.0D, this.getVelocity().y * 1.25D, 0.0D);
            double minY = 0.4D;
            if (motion.y < minY) {
                motion = motion.withAxis(Direction.Axis.Y, minY);
            }
            this.setVelocity(motion);
        } else {
            this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
        }
    }

    @Override
    public AssetInfo.TextureAssetInfo getTexture() {
        return TEXTURE;
    }
}