package fr.hugman.mubble.test.gametest.support;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shaping the empty structure the tests run in. Fabric hands every {@code @GameTest} an 8×8×8 box of
 * air, so anything that has to stand on something needs a floor built first.
 */
public final class Arena {
    /** Bottom layer of the arena, in structure-relative coordinates. */
    public static final int FLOOR_Y = 0;
    public static final int SIZE = 8;

    private Arena() {
    }

    /** Fills the bottom layer with stone, so that nothing falls out of the test. */
    public static void buildFloor(GameTestHelper helper) {
        buildFloor(helper, Blocks.STONE.defaultBlockState());
    }

    public static void buildFloor(GameTestHelper helper, BlockState state) {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                helper.setBlock(new BlockPos(x, FLOOR_Y, z), state);
            }
        }
    }

    /** A single block to stand on, for the tests that only need one entity to stay put. */
    public static BlockPos footing(GameTestHelper helper, BlockPos standingPos) {
        helper.setBlock(standingPos.below(), Blocks.STONE);
        return standingPos;
    }

    /**
     * Opens the sky above {@code pos}, which is what the weather needs to reach it.
     * <p>
     * The framework caps every arena with a barrier ceiling, so anything asking the level whether it
     * is raining on a position gets a no: the height map sits above the player. The blocks removed
     * here are outside the structure, hence the absolute coordinates.
     */
    public static void openTheSky(GameTestHelper helper, BlockPos pos) {
        var absolute = helper.absolutePos(pos);
        var level = helper.getLevel();

        for (int dy = 1; dy <= SIZE + 4; dy++) {
            level.setBlockAndUpdate(absolute.above(dy), Blocks.AIR.defaultBlockState());
        }
    }
}
