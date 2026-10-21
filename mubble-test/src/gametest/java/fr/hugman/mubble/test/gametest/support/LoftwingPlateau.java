package fr.hugman.mubble.test.gametest.support;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

/**
 * The ground a loftwing needs to behave like one.
 * <p>
 * Nothing the bird does fits in the eight blocks {@link Arena} shapes: it looks for a perch within
 * twenty-four blocks of itself and rides circles twenty blocks across. So its tests run in a
 * structure of their own, wide enough that every spot the bird considers is ground this class laid
 * down rather than whatever the framework put at the edge of the box.
 */
public final class LoftwingPlateau {
    public static final String STRUCTURE = "mubble-gametest:loftwing_sky";

    /** Matches the structure, which is 96 blocks square and 16 tall. */
    public static final int SIZE = 96;
    public static final int FLOOR_Y = 0;
    /** Keeps one test's plateau out of range of the next one's perch hunt. */
    public static final int PADDING = 32;

    private LoftwingPlateau() {
    }

    /**
     * A flat plateau with a stone lid three blocks over it: a bird under an overhang, in a hollow,
     * or in any of the places it lands where it cannot simply climb back out.
     */
    public static void buildHollow(GameTestHelper helper) {
        buildPlateau(helper);
        fill(helper, 0, SIZE, FLOOR_Y + 4, FLOOR_Y + 4);
    }

    /** A flat plateau over the whole structure. */
    public static void buildPlateau(GameTestHelper helper) {
        fill(helper, 0, SIZE, FLOOR_Y, FLOOR_Y);
    }

    private static void fill(GameTestHelper helper, int from, int to, int fromY, int toY) {
        for (int x = from; x < to; x++) {
            for (int z = from; z < to; z++) {
                for (int y = fromY; y <= toY; y++) {
                    helper.setBlock(new BlockPos(x, y, z), Blocks.STONE);
                }
            }
        }
    }
}
