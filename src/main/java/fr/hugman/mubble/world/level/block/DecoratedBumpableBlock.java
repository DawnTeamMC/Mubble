package fr.hugman.mubble.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.world.level.block.entity.BumpableBlockEntity;
import fr.hugman.mubble.sound.MubbleSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Generic bumpable block with an additional sound effect when bumped.
 * <p>It also launches entities on top of the block when bumped from the bottom.
 *
 * @author haykam
 * @author Hugman
 * @since v4.0.0
 */
public class DecoratedBumpableBlock extends BumpableBlock {
    public static final MapCodec<DecoratedBumpableBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            BlockState.CODEC.fieldOf("default_bumped_state").forGetter((block) -> block.defaultBumpedState),
            propertiesCodec()
    ).apply(instance, DecoratedBumpableBlock::new));

    public DecoratedBumpableBlock(@Nullable BlockState defaultBumpedState, Properties settings) {
        super(defaultBumpedState, settings);
    }

    @Override
    protected MapCodec<? extends DecoratedBumpableBlock> codec() {
        return CODEC;
    }

    @Override
    public void onBumpStart(Level world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        super.onBumpStart(world, pos, state, blockEntity);
        this.playGenericBumpSound(blockEntity);
    }

    @Override
    public void onBumpMiddle(Level world, BlockPos pos, BlockState state, BumpableBlockEntity blockEntity) {
        super.onBumpMiddle(world, pos, state, blockEntity);
        if (blockEntity.getLevel() != null && blockEntity.getBumpDirection() == Direction.UP) {
            this.launchEntitiesOnTop(blockEntity.getLevel(), blockEntity.getBlockPos());
        }
    }

    public void playGenericBumpSound(BumpableBlockEntity entity) {
        Level world = entity.getLevel();
        Vec3 pos = entity.getBlockPos().getCenter();
        if (world != null) {
            world.playSound(null, pos.x(), pos.y(), pos.z(), MubbleSounds.BUMPABLE_BLOCK_BUMP, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * Launches entities on top of the block.
     */
    public void launchEntitiesOnTop(Level world, BlockPos pos) {
        List<Entity> entities = world.getEntities(null, new AABB(pos.above()));
        for (Entity entity : entities) {
            launchEntity(entity);
        }
    }

    public void launchEntity(Entity entity) {
        Vec3 vec3d = entity.getDeltaMovement();
        entity.setDeltaMovement(vec3d.x, 0.3D, vec3d.z);
        entity.needsSync = true;
        // TODO: add a damage type and a gamerule for harming entities
    }
}
