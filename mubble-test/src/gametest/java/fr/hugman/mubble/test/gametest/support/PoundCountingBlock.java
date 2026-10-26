package fr.hugman.mubble.test.gametest.support;

import fr.hugman.mubble.world.level.block.PoundableBlock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A block that only counts how many times it was pounded, so that a test can tell one pound from two:
 * the blocks of Super Mario cannot, as they ignore a pound while they still bump from the last one.
 * Counted by position, so that tests running side by side do not see each other's pounds.
 */
public final class PoundCountingBlock extends Block implements PoundableBlock {
    private static final Map<BlockPos, Integer> POUNDS = new ConcurrentHashMap<>();

    public PoundCountingBlock(Properties properties) {
        super(properties);
    }

    public static int pounds(BlockPos pos) {
        return POUNDS.getOrDefault(pos, 0);
    }

    @Override
    public void onPounded(Level level, BlockState state, BlockPos pos, Entity entity) {
        POUNDS.merge(pos, 1, Integer::sum);
    }
}
