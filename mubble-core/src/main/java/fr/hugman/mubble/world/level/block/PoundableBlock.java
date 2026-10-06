package fr.hugman.mubble.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A block that reacts to being pounded from above, as Mario's ground pound does to the blocks he
 * lands on. Whatever pounds calls this on the server, once per block it lands on; whatever block
 * cares implements it, without either knowing of the other.
 */
public interface PoundableBlock {
    /**
     * Called on the server when {@code entity} pounds the block at {@code pos} from above.
     */
    void onPounded(Level level, BlockState state, BlockPos pos, Entity entity);
}
