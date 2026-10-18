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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

/**
 * The shape a snake takes and the two ways of setting one off.
 * <p>
 * A snake is a clump of blocks touching each other with a path drawn off one of them; nothing about
 * the way they were placed decides its shape. The walking itself lives in
 * {@link SnakeMovementGameTest}.
 */
public class SnakeBlockGameTest {
    private static final BlockPos TAIL = new BlockPos(1, Arena.FLOOR_Y + 1, 1);

    @GameTest
    public void afreshBlockIsIdleAndCarriesNoShape(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TAIL, Blocks.STONE);

        var state = Snakes.placementStateAgainst(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.UP);

        helper.assertValueEqual(state.getValue(SnakeBlock.SNAKE_STATE), SnakeBlockState.IDLE, "a fresh snake block should be idle");
        helper.assertFalse(state.getValue(SnakeBlock.RESPAWNABLE), "and should not come back on its own");
        helper.succeed();
    }

    /**
     * The body is every block touching the head, however they were laid down. Every block here
     * points into the floor, so a chain read off the facings would be one block long.
     */
    @GameTest
    public void thebodyIsEveryBlockTouchingTheHead(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 4);
        Snakes.drawPath(helper, head, Direction.SOUTH, 1);

        var body = SnakeChain.bodyFromHead(helper.getLevel(), helper.absolutePos(head));

        helper.assertValueEqual(body.size(), 4, "every block of the line should belong to the snake");
        helper.assertValueEqual(body.getFirst(), helper.absolutePos(TAIL), "the far end from the head is the tail");
        helper.assertValueEqual(body.getLast(), helper.absolutePos(head), "and the head is where the path was drawn");
        helper.succeed();
    }

    /** A bend is read exactly the way it looks, because the walk only turns when it has to. */
    @GameTest
    public void thebodyFollowsAbend(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 3);
        BlockPos corner = TAIL.relative(Direction.SOUTH, 2);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, corner.relative(Direction.EAST), Direction.EAST, 2);
        BlockPos head = corner.relative(Direction.EAST, 2);
        Snakes.drawPath(helper, head, Direction.EAST, 1);

        var body = SnakeChain.bodyFromHead(helper.getLevel(), helper.absolutePos(head));

        helper.assertValueEqual(body.size(), 5, "a bent snake is still one snake");
        helper.assertValueEqual(body.getFirst(), helper.absolutePos(TAIL), "and it still ends where the line does");
        helper.succeed();
    }

    @GameTest
    public void speedsNeverMix(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Snakes.place(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL.relative(Direction.NORTH));
        Snakes.drawPath(helper, head, Direction.SOUTH, 1);

        var body = SnakeChain.bodyFromHead(helper.getLevel(), helper.absolutePos(head));

        helper.assertValueEqual(body.size(), 2, "a block of another speed touching the snake is not part of it");
        helper.succeed();
    }

    @GameTest
    public void asnakeStopsAtSixteenBlocks(GameTestHelper helper) {
        Arena.buildFloor(helper);
        // Folded into two rows, since a straight run of this length does not fit in the arena.
        var corner = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 6);
        var bend = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, corner.relative(Direction.EAST), Direction.EAST, 6);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, bend.relative(Direction.NORTH), Direction.NORTH, 6);
        Snakes.drawPath(helper, head, Direction.NORTH, 1);

        var body = SnakeChain.bodyFromHead(helper.getLevel(), helper.absolutePos(head));

        helper.assertValueEqual(body.size(), SnakeChain.MAX_BODY_LENGTH, "a snake should stop reading at its maximum length");
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

    /*============*/
    /*  STARTING  */
    /*============*/

    @GameTest
    public void aplayerSetsAnIdleSnakeOff(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Snakes.drawPath(helper, head, Direction.SOUTH, 2);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        SuperMarioBlocks.SNAKE_BLOCK.stepOn(helper.getLevel(), helper.absolutePos(TAIL), helper.getBlockState(TAIL), player);

        helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.MOVING), "stepping on a snake should set it off");
        helper.succeed();
    }

    /**
     * A clump with nowhere to go is not a snake yet. Walking over one while laying it out used to
     * set it off, only for it to shake and drop to pieces on the spot.
     */
    @GameTest
    public void asnakeWithNoPathDoesNotStart(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 3);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);

        SuperMarioBlocks.SNAKE_BLOCK.stepOn(helper.getLevel(), helper.absolutePos(TAIL), helper.getBlockState(TAIL), player);
        helper.setBlock(TAIL.above(), Blocks.REDSTONE_BLOCK);

        helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE),
                "a clump with no path drawn on it should stay exactly where it is");
        helper.succeed();
    }

    @GameTest
    public void aspectatorDoesNot(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Snakes.drawPath(helper, head, Direction.SOUTH, 2);
        var spectator = helper.makeMockPlayer(GameType.SPECTATOR);

        SuperMarioBlocks.SNAKE_BLOCK.stepOn(helper.getLevel(), helper.absolutePos(TAIL), helper.getBlockState(TAIL), spectator);

        helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE), "a spectator walks over a snake without waking it");
        helper.succeed();
    }

    @GameTest
    public void redstoneSetsAnIdleSnakeOff(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Snakes.drawPath(helper, head, Direction.SOUTH, 2);

        helper.setBlock(TAIL.above(), Blocks.REDSTONE_BLOCK);

        helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.MOVING), "a redstone signal should set a snake off");
        helper.succeed();
    }

    /**
     * The two-pulse sequence a knocked-out block answers to: the first brings it back as a snake one
     * block long, and the second starts it — if by then it has somewhere to go.
     */
    @GameTest(maxTicks = 60)
    public void thefirstPulseBringsAdeactivatedBlockBack(GameTestHelper helper) {
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
                .thenExecuteAfter(4, () -> helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE),
                        "and with no path drawn, the second should leave it alone"))
                .thenSucceed();
    }

    @GameTest(maxTicks = 60)
    public void thesecondPulseStartsAwokenBlockThatHasApath(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TAIL, SuperMarioBlocks.SNAKE_BLOCK.defaultBlockState()
                .setValue(SnakeBlock.FACING, Direction.SOUTH)
                .setValue(SnakeBlock.SNAKE_STATE, SnakeBlockState.DEACTIVATED));
        Snakes.drawPath(helper, TAIL, Direction.SOUTH, 3);

        helper.startSequence()
                .thenExecute(() -> helper.setBlock(TAIL.above(), Blocks.REDSTONE_BLOCK))
                .thenExecuteAfter(2, () -> helper.setBlock(TAIL.above(), Blocks.AIR))
                .thenExecuteAfter(2, () -> helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE),
                        "the first pulse should wake a deactivated block into an idle snake"))
                .thenExecute(() -> helper.setBlock(TAIL.above(), Blocks.REDSTONE_BLOCK))
                .thenExecuteAfter(4, () -> helper.assertFalse(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE),
                        "and the second should send it on its way"))
                .thenSucceed();
    }

    @GameTest
    public void asnakeTakesItsRespawnableFlagFromItsHead(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 3);
        Snakes.drawPath(helper, head, Direction.SOUTH, 2);
        helper.setBlock(head, helper.getBlockState(head).setValue(SnakeBlock.RESPAWNABLE, true));

        SnakeManager.get(helper.getLevel()).start(helper.getLevel(), helper.absolutePos(TAIL));

        helper.assertBlockProperty(TAIL, SnakeBlock.RESPAWNABLE, true);
        helper.assertBlockProperty(TAIL.relative(Direction.SOUTH), SnakeBlock.RESPAWNABLE, true);
        helper.succeed();
    }

    @GameTest
    public void amovingBlockIsNeverSomethingAplayerHolds(GameTestHelper helper) {
        helper.assertTrue(SuperMarioBlocks.SNAKE_MOVING_BLOCK instanceof SnakeMovingBlock, "the moving block should be its own kind");
        helper.assertTrue(SuperMarioBlocks.SNAKE_MOVING_BLOCK.asItem() == Items.AIR,
                "the moving block stands in for a snake block and should never be an item of its own");
        helper.succeed();
    }
}
