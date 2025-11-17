package fr.hugman.mubble.entity;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.sound.MubbleSounds;
import fr.hugman.mubble.tag.MubbleBlockTags;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
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

public class IceballEntity extends BallEntity {
    private static final AssetInfo.TextureAssetInfo TEXTURE = new AssetInfo.TextureAssetInfo(Mubble.id("entity/iceball"));

    public IceballEntity(EntityType<? extends IceballEntity> type, World world) {
        super(type, world);
    }

    public IceballEntity(World world, LivingEntity owner) {
        super(MubbleEntityTypes.ICEBALL, world, owner);
    }

    public IceballEntity(double x, double y, double z, World world) {
        super(MubbleEntityTypes.ICEBALL, x, y, z, world);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MubbleSounds.ICEBALL_HIT_BLOCK;
    }

    @Override
    protected ParticleEffect getDeathParticle() {
        return ParticleTypes.CLOUD;
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        Entity owner = this.getOwner();
        float damage = entity instanceof SnowGolemEntity ? 1.0F : 3.0F;

        if (owner instanceof LivingEntity livingEntity) {
            livingEntity.onAttacking(entity);
        }
        if(!this.getEntityWorld().isClient()) {
            if(!(entity instanceof SnowGolemEntity) && entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1));
            }
        }

        this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.ICEBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
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
        if (state.isIn(MubbleBlockTags.FREEZABLE_TO_PACKED_ICE)) {
            resultBlock = Blocks.PACKED_ICE;
        }
        if (resultBlock != null) {
            if (!this.getEntityWorld().isClient()) {
                if (resultBlock instanceof AirBlock) {
                    this.getEntityWorld().removeBlock(pos, false);
                } else {
                    this.getEntityWorld().setBlockState(pos, resultBlock.getDefaultState());
                }
            }
            this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
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
            this.getEntityWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.ICEBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
        }
    }

    @Override
    public AssetInfo.TextureAssetInfo getTexture() {
        return TEXTURE;
    }
}