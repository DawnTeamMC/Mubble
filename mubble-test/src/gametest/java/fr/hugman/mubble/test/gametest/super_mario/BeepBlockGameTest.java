package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.BeepBlock;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.test.gametest.support.Arena;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.phys.shapes.CollisionContext;

/**
 * The beep blocks: two of them, out of phase with each other, flipping between solid and a hollow
 * frame on a timer. One of the pair is always solid while the other is not, which is what makes them
 * usable as a moving path.
 */
public class BeepBlockGameTest {
    private static final BlockPos RED = new BlockPos(3, Arena.FLOOR_Y + 1, 3);
    private static final BlockPos BLUE = new BlockPos(5, Arena.FLOOR_Y + 1, 3);

    @GameTest
    public void thetwoBlocksAreNeverInTheSamePhase(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var level = helper.getLevel();

        var red = SuperMarioBlocks.RED_BEEP_BLOCK.getStateAtTime(level);
        var blue = SuperMarioBlocks.BLUE_BEEP_BLOCK.getStateAtTime(level);

        helper.assertTrue(red.getValue(BeepBlock.FRAME) != blue.getValue(BeepBlock.FRAME),
                "the two beep blocks should always be in opposite phases, or the pair is useless");

        helper.succeed();
    }

    @GameTest
    public void asolidPhaseCollidesAndAframeDoesNot(GameTestHelper helper) {
        Arena.buildFloor(helper);

        var solid = SuperMarioBlocks.RED_BEEP_BLOCK.defaultBlockState().setValue(BeepBlock.FRAME, false);
        var frame = SuperMarioBlocks.RED_BEEP_BLOCK.defaultBlockState().setValue(BeepBlock.FRAME, true);

        helper.setBlock(RED, solid);
        helper.setBlock(BLUE, frame);

        var level = helper.getLevel();
        var solidShape = solid.getCollisionShape(level, helper.absolutePos(RED), CollisionContext.empty());
        var frameShape = frame.getCollisionShape(level, helper.absolutePos(BLUE), CollisionContext.empty());

        helper.assertFalse(solidShape.isEmpty(), "the solid phase should be something to stand on");
        helper.assertTrue(frameShape.isEmpty(), "the frame phase should be walked straight through");

        helper.succeed();
    }

    /** The default cooldown is four seconds, so one flip fits inside a test of a few hundred ticks. */
    @GameTest(maxTicks = 300)
    public void ablockFlipsPhaseOnItsOwn(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(RED, SuperMarioBlocks.RED_BEEP_BLOCK);

        boolean startedAsFrame = helper.getBlockState(RED).getValue(BeepBlock.FRAME);

        helper.succeedWhen(() -> helper.assertTrue(helper.getBlockState(RED).getValue(BeepBlock.FRAME) != startedAsFrame,
                "the beep block never flipped phase"));
    }

    @GameTest
    public void placingOneStartsItInPhaseWithTheClock(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var expected = SuperMarioBlocks.RED_BEEP_BLOCK.getStateAtTime(helper.getLevel()).getValue(BeepBlock.FRAME);

        helper.setBlock(RED, SuperMarioBlocks.RED_BEEP_BLOCK);

        helper.assertValueEqual(helper.getBlockState(RED).getValue(BeepBlock.FRAME), expected,
                "a freshly placed beep block should already be in step with the others");

        helper.succeed();
    }

    @GameTest
    public void thephaseOnlyMovesWithTheClock(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var level = helper.getLevel();

        var first = SuperMarioBlocks.RED_BEEP_BLOCK.getStateAtTime(level);
        var second = SuperMarioBlocks.RED_BEEP_BLOCK.getStateAtTime(level);

        helper.assertValueEqual(second.getValue(BeepBlock.FRAME), first.getValue(BeepBlock.FRAME),
                "the phase should be a function of the world time and nothing else");
        helper.assertTrue(first.hasProperty(BeepBlock.FRAME), "a beep block state should carry its phase");

        helper.succeed();
    }
}
