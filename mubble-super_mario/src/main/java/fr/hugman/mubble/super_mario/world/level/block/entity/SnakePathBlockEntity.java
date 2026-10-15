package fr.hugman.mubble.super_mario.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A marker on every path block, carrying nothing at all.
 * <p>
 * Its only job is to get the block drawn. A path block is only visible to a player holding a snake
 * block, and a chunk mesh is baked once and reused for everyone, so the model has to be submitted
 * per frame instead — which is what a block entity renderer is. See {@code SnakePathBlockRenderer}
 * for why this beats walking the world ourselves.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class SnakePathBlockEntity extends BlockEntity {
    public SnakePathBlockEntity(BlockPos pos, BlockState state) {
        super(SuperMarioBlockEntityTypes.SNAKE_PATH, pos, state);
    }
}
