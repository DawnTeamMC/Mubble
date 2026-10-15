package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlockState;
import fr.hugman.mubble.super_mario.world.level.block.SnakeMovingBlock;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.snake.SnakeChain;
import fr.hugman.mubble.super_mario.world.level.snake.SnakeManager;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.Snakes;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

/**
 * The chain a snake is made of, and the two ways of setting one off.
 * <p>
 * Everything here is about idle snakes: how blocks join one, where they refuse to, and what wakes a
 * knocked-out one back up. The walking itself lives in {@link SnakeMovementGameTest}.
 */
public class SnakeBlockGameTest {
    private static final BlockPos TAIL = new BlockPos(1, Arena.FLOOR_Y + 1, 1);

    @GameTest
    public void afreshBlockPointsAwayFromWhatItWasPlacedOn(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TAIL, Blocks.STONE);

        var state = Snakes.placementStateAgainst(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.UP);

        helper.assertValueEqual(state.getValue(SnakeBlock.FACING), Direction.UP,
                "a snake block placed on its own should point away from the face it was put against");
        helper.assertValueEqual(state.getValue(SnakeBlock.SNAKE_STATE), SnakeBlockState.IDLE, "a fresh snake block should be idle");
        helper.succeed();
    }

    @GameTest
    public void ablockOnTheTailsBackJoinsTheSnake(GameTestHelper helper) {
        Arena.buildFloor(helper);
        // A snake of two pointing south: its tail is the northern block, and its back faces north.
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);

        var state = Snakes.placementStateAgainst(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.NORTH);

        helper.assertValueEqual(state.getValue(SnakeBlock.FACING), Direction.SOUTH,
                "a block placed on the back of the tail should point at the tail it lengthens");
        helper.succeed();
    }

    @GameTest
    public void ablockOnAnyOtherFaceStartsItsOwnSnake(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);

        var state = Snakes.placementStateAgainst(helper, SuperMarioBlocks.SNAKE_BLOCK, head, Direction.EAST);

        helper.assertValueEqual(state.getValue(SnakeBlock.FACING), Direction.EAST,
                "only the back of the tail lengthens a snake; every other face starts a new one");
        helper.succeed();
    }

    @GameTest
    public void speedsNeverMix(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);

        var state = Snakes.placementStateAgainst(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.NORTH);

        helper.assertValueEqual(state.getValue(SnakeBlock.FACING), Direction.NORTH,
                "a fast block on the back of a normal tail should start a snake of its own, not join it");
        helper.succeed();
    }

    @GameTest
    public void asnakeStopsGrowingAtSixteen(GameTestHelper helper) {
        Arena.buildFloor(helper);
        // Folded into two rows, since a straight run of sixteen does not fit in the arena.
        BlockPos pos = TAIL;
        for (int i = 0; i < SnakeChain.MAX_BODY_LENGTH; i++) {
            helper.setBlock(pos, SuperMarioBlocks.SNAKE_BLOCK.defaultBlockState().setValue(SnakeBlock.FACING, Direction.SOUTH));
            pos = pos.relative(Direction.SOUTH);
        }
        helper.assertValueEqual(SnakeChain.body(helper.getLevel(), helper.absolutePos(TAIL)).size(), SnakeChain.MAX_BODY_LENGTH,
                "the chain should stop reading at the maximum length");

        var state = Snakes.placementStateAgainst(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.NORTH);

        helper.assertValueEqual(state.getValue(SnakeBlock.FACING), Direction.NORTH,
                "a seventeenth block should start its own snake rather than lengthen a full one");
        helper.succeed();
    }

    @GameTest
    public void breakingTheMiddleOfAnIdleSnakeOnlyRemovesThatBlock(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 3);
        BlockPos middle = TAIL.relative(Direction.SOUTH);

        helper.destroyBlock(middle);

        helper.assertBlockNotPresent(SuperMarioBlocks.SNAKE_BLOCK, middle);
        helper.assertBlockPresent(SuperMarioBlocks.SNAKE_BLOCK, TAIL);
        helper.assertBlockPresent(SuperMarioBlocks.SNAKE_BLOCK, TAIL.relative(Direction.SOUTH, 2));
        helper.succeed();
    }

    @GameTest
    public void aplayerSetsAnIdleSnakeOff(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        SuperMarioBlocks.SNAKE_BLOCK.stepOn(helper.getLevel(), helper.absolutePos(TAIL), helper.getBlockState(TAIL), player);

        helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.MOVING), "stepping on a snake should set it off");
        helper.succeed();
    }

    @GameTest
    public void aspectatorDoesNot(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        var spectator = helper.makeMockPlayer(GameType.SPECTATOR);

        SuperMarioBlocks.SNAKE_BLOCK.stepOn(helper.getLevel(), helper.absolutePos(TAIL), helper.getBlockState(TAIL), spectator);

        helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE), "a spectator walks over a snake without waking it");
        helper.succeed();
    }

    @GameTest
    public void redstoneSetsAnIdleSnakeOff(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);

        helper.setBlock(TAIL.above(), Blocks.REDSTONE_BLOCK);

        helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.MOVING), "a redstone signal should set a snake off");
        helper.succeed();
    }

    /**
     * The two-pulse sequence a knocked-out block answers to: the first brings it back as a snake one
     * block long, and only the second starts it.
     */
    @GameTest(maxTicks = 60)
    public void twoPulsesBringADeactivatedBlockBack(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TAIL, SuperMarioBlocks.SNAKE_BLOCK.defaultBlockState()
                .setValue(SnakeBlock.FACING, Direction.SOUTH)
                .setValue(SnakeBlock.SNAKE_STATE, SnakeBlockState.DEACTIVATED));

        helper.startSequence()
                .thenExecute(() -> helper.setBlock(TAIL.above(), Blocks.REDSTONE_BLOCK))
                .thenExecuteAfter(2, () -> helper.setBlock(TAIL.above(), Blocks.AIR))
                .thenExecuteAfter(2, () -> helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE),
                        "the first pulse should wake a deactivated block into an idle snake"))
                .thenExecute(() -> helper.setBlock(TAIL.above(), Blocks.REDSTONE_BLOCK))
                .thenExecuteAfter(4, () -> helper.assertFalse(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE),
                        "the second pulse should start it: with no path it goes straight into its shake"))
                .thenExecute(() -> helper.assertBlockPresent(SuperMarioBlocks.SNAKE_MOVING_BLOCK, TAIL))
                .thenSucceed();
    }

    @GameTest
    public void asnakeTakesItsRespawnableFlagFromItsHead(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 3);
        helper.setBlock(head, helper.getBlockState(head).setValue(SnakeBlock.RESPAWNABLE, true));

        SnakeManager.get(helper.getLevel()).start(helper.getLevel(), helper.absolutePos(TAIL));

        helper.assertBlockProperty(TAIL, SnakeBlock.RESPAWNABLE, true);
        helper.assertBlockProperty(TAIL.relative(Direction.SOUTH), SnakeBlock.RESPAWNABLE, true);
        helper.succeed();
    }

    @GameTest
    public void amovingBlockIsNeverSomethingAplayerHolds(GameTestHelper helper) {
        helper.assertTrue(SuperMarioBlocks.SNAKE_MOVING_BLOCK instanceof SnakeMovingBlock, "the moving block should be its own kind");
        helper.assertTrue(SuperMarioBlocks.SNAKE_MOVING_BLOCK.asItem() == net.minecraft.world.item.Items.AIR,
                "the moving block stands in for a snake block and should never be an item of its own");
        helper.succeed();
    }
}
