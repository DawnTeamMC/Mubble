package fr.hugman.mubble.entity;

import fr.hugman.mubble.block.MubbleBlockTags;
import fr.hugman.mubble.sound.MubbleSounds;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FireballEntity extends BallEntity {
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
    protected double getGravity() {
        return 0.08;
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

        entity.serverDamage(this.getDamageSources().thrown(this, this.getOwner()), damage);
        if (!entity.isFireImmune()) {
            entity.setOnFireFor(5);
        }
        this.getWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_ENTITY, SoundCategory.NEUTRAL, 0.5F, 1.0F);
        this.finalHit();
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        BlockPos pos = result.getBlockPos();
        BlockState state = this.getWorld().getBlockState(pos);
        Direction face = result.getSide();
        //AbstractFireBlock fire = (AbstractFireBlock) Blocks.FIRE;
        Block resultBlock = null;
        if (state.isIn(MubbleBlockTags.MELTABLE_TO_AIR)) {
            resultBlock = Blocks.AIR;
        } else if (state.isIn(MubbleBlockTags.MELTABLE_TO_ICE)) {
            resultBlock = Blocks.ICE;
        } else if (state.isIn(MubbleBlockTags.MELTABLE_TO_WATER)) {
            resultBlock = Blocks.WATER;
        }
        if (resultBlock != null) {
            if (!this.getWorld().isClient) {
                if (this.getWorld().getDimension().ultrawarm() || resultBlock instanceof AirBlock) {
                    this.getWorld().removeBlock(pos, false);
                } else {
                    this.getWorld().setBlockState(pos, resultBlock.getDefaultState());
                    //this.getWorld().updateNeighborsAlways(pos, resultBlock);
                }
            }
            this.getWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_MELTABLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
            return;
        }
        if (state.isIn(BlockTags.CAMPFIRES, (abstractBlockState) -> abstractBlockState.contains(CampfireBlock.LIT) && abstractBlockState.contains(CampfireBlock.WATERLOGGED))) {
            if (!state.get(CampfireBlock.LIT) && !state.get(CampfireBlock.WATERLOGGED)) {
                if (!this.getWorld().isClient) {
                    this.getWorld().setBlockState(pos, state.with(CampfireBlock.LIT, true));
                }
                this.getWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
                this.finalHit();
                return;
            }
        }
        FlammableBlockRegistry.Entry flammableEntry = FlammableBlockRegistry.getDefaultInstance().get(state.getBlock());
        if (flammableEntry.getBurnChance() > 0 || flammableEntry.getSpreadChance() > 0) {
            BlockPos firePos = pos.offset(face);
            if (this.getWorld().isAir(firePos) && !this.getWorld().isClient) {
                this.getWorld().setBlockState(firePos, AbstractFireBlock.getState(this.getWorld(), firePos));
            }
            this.getWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
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
            this.getWorld().playSound(null, getX(), getY(), getZ(), MubbleSounds.FIREBALL_HIT_BLOCK, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            this.finalHit();
        }
    }

    public double getSpeed() {
        return this.getVelocity().length();
    }
}