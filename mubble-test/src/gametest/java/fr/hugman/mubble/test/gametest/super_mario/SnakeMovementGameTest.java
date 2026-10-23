package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlockState;
import fr.hugman.mubble.super_mario.world.level.block.SnakeSpeed;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.snake.MovingSnake;
import fr.hugman.mubble.super_mario.world.level.snake.SnakeManager;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.Snakes;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;

/**
 * A snake actually walking: the body standing still while the head grows and the tail shrinks, what
 * happens when it runs out of path, and the ways it can be broken on the way.
 * <p>
 * The fast variant is used throughout — three ticks a block — so that a whole run fits inside a test.
 */
public class SnakeMovementGameTest {
    private static final BlockPos TAIL = new BlockPos(1, Arena.FLOOR_Y + 1, 1);
    private static final int FAST_STEP = SnakeSpeed.FAST.ticksPerBlock();

    private static MovingSnake start(GameTestHelper helper, BlockPos tail) {
        return SnakeManager.get(helper.getLevel()).start(helper.getLevel(), helper.absolutePos(tail));
    }

    /**
     * Whether the level has forgotten {@code snake}.
     * <p>
     * Every game test runs in the same level, and so shares the one record of running snakes with
     * the tests around it: asking whether the record is empty would be asking about theirs too.
     */
    private static boolean isForgotten(GameTestHelper helper, MovingSnake snake) {
        return SnakeManager.get(helper.getLevel()).snakes().stream().noneMatch(other -> other.id() == snake.id());
    }

    @GameTest
    public void speedsAreTicksPerBlock(GameTestHelper helper) {
        helper.assertTrue(SnakeSpeed.FAST.ticksPerBlock() < SnakeSpeed.NORMAL.ticksPerBlock(),
                "the fast variant should take fewer ticks on a block than the normal one");
        helper.assertTrue(SnakeSpeed.NORMAL.ticksPerBlock() < SnakeSpeed.SLOW.ticksPerBlock(),
                "the slow variant should take more ticks on a block than the normal one");
        helper.assertValueEqual(SuperMarioBlocks.FAST_SNAKE_BLOCK.speed(), SnakeSpeed.FAST, "the fast block should be fast");
        helper.succeed();
    }

    /**
     * The whole point of a snake: the blocks in the middle never move, one appears at the head and
     * one disappears at the tail.
     */
    @GameTest(maxTicks = 60)
    public void thesnakeGrowsAtItsHeadAndShrinksAtItsTail(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 3);
        Snakes.drawPath(helper, head, Direction.SOUTH, 4);
        // The first cell the head grows into. It belongs to the body for three whole steps after
        // that, and should not budge once in it.
        BlockPos grownInto = head.relative(Direction.SOUTH);

        start(helper, TAIL);

        helper.startSequence()
                .thenIdle(FAST_STEP + 1)
                .thenExecuteFor(FAST_STEP, () -> helper.assertTrue(Snakes.isSnake(helper, grownInto, SnakeBlockState.MOVING),
                        "a block in the middle of a snake never moves"))
                .thenWaitUntil(() -> {
                    helper.assertFalse(Snakes.isSnake(helper, TAIL, SnakeBlockState.MOVING), "the tail should have left its first cell");
                    helper.assertTrue(Snakes.isSnake(helper, TAIL.relative(Direction.SOUTH, 5), SnakeBlockState.MOVING),
                            "the head should have grown along the path");
                })
                .thenSucceed();
    }

    /** A snake with no path left stops, trembles, and comes apart from the tail. */
    @GameTest(maxTicks = 140)
    public void attheEndOfThePathItShakesAndFallsApart(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Snakes.drawPath(helper, head, Direction.SOUTH, 1);

        var snake = start(helper, TAIL);

        helper.startSequence()
                .thenIdle(FAST_STEP + 4)
                .thenExecute(() -> helper.assertTrue(
                        helper.getBlockState(TAIL.relative(Direction.SOUTH)).is(SuperMarioBlocks.SNAKE_MOVING_BLOCK),
                        "a snake out of path should be trembling in place"))
                .thenIdle(MovingSnake.SHAKE_TICKS + MovingSnake.FALL_INTERVAL * 4 + 20)
                .thenExecute(() -> {
                    helper.assertTrue(Snakes.isSnake(helper, TAIL.relative(Direction.SOUTH), SnakeBlockState.IDLE)
                                    || Snakes.isSnake(helper, TAIL.relative(Direction.SOUTH, 2), SnakeBlockState.IDLE),
                            "a snake that ran out of path should have landed as ordinary blocks, ready to go again");
                    helper.assertTrue(isForgotten(helper, snake), "the record should be gone with it");
                })
                .thenSucceed();
    }

    /** Mining a block of a running snake breaks it there: the head half carries on, the rest falls. */
    @GameTest(maxTicks = 60)
    public void miningAmovingSnakeBreaksItAtThatBlock(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 3);
        Snakes.drawPath(helper, head, Direction.SOUTH, 3);
        var snake = start(helper, TAIL);

        helper.startSequence()
                // Two blocks back from the head, so that there is a piece behind the break to fall.
                .thenExecute(() -> helper.destroyBlock(TAIL.relative(Direction.SOUTH, 2)))
                .thenWaitUntil(() -> {
                    helper.assertTrue(snake.body().size() < 3, "the snake should be shorter than it was");
                    helper.assertEntityPresent(EntityTypes.FALLING_BLOCK);
                })
                .thenSucceed();
    }

    /**
     * What is left of a route stays standing when the snake walking it is destroyed, so the blocks
     * that come off it can be picked up and sent down the rest.
     * <p>
     * It survives because the head hands the start of the route to the next block as it eats one.
     * Without that, the stretch ahead would lose the only thing pointing at it the moment the head
     * went, and take itself away one block update later.
     */
    @GameTest(maxTicks = 80)
    public void whatIsLeftOfArouteOutlivesItsSnake(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SLOW_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, TAIL, Direction.SOUTH, 4);
        var snake = start(helper, TAIL);

        helper.startSequence()
                // Long enough for the first block of the route to have been eaten, and the next to
                // have taken over as its start.
                .thenIdle(SnakeSpeed.SLOW.ticksPerBlock() + 2)
                .thenExecute(() -> helper.getLevel().destroyBlock(snake.head(), false))
                .thenIdle(6)
                .thenExecute(() -> {
                    helper.assertTrue(isForgotten(helper, snake), "the snake itself should be gone");
                    helper.assertTrue(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH, 3)),
                            "the rest of the route should still be standing");
                    helper.assertTrue(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH, 4)),
                            "all the way to its end");
                })
                .thenSucceed();
    }

    /** And a block dropped back on the spot takes that route up and walks what is left of it. */
    @GameTest(maxTicks = 120)
    public void ablockPutBackCarriesOnDownWhatIsLeft(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.SLOW_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, TAIL, Direction.SOUTH, 4);
        var snake = start(helper, TAIL);
        BlockPos again = TAIL.relative(Direction.SOUTH, 2);

        helper.startSequence()
                .thenIdle(SnakeSpeed.SLOW.ticksPerBlock() + 2)
                .thenExecute(() -> helper.getLevel().destroyBlock(snake.head(), false))
                .thenIdle(6)
                .thenExecute(() -> {
                    // Nothing is pointed anywhere: the block simply stands against what is left.
                    Snakes.place(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, again);
                    helper.assertTrue(SnakeManager.get(helper.getLevel()).start(helper.getLevel(), helper.absolutePos(again)) != null,
                            "a block standing against the rest of a route should be a snake again");
                })
                .thenWaitUntil(() -> helper.assertFalse(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH, 4)),
                        "and should walk the rest of the route"))
                .thenSucceed();
    }

    /**
     * A snake eating its own path must not set off the chain reaction that breaking a path block
     * does, or the whole route would vanish the moment its head left the ground.
     */
    @GameTest(maxTicks = 60)
    public void walkingIntoItsOwnPathDoesNotClearTheRestOfIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 4);

        start(helper, TAIL);

        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> {
                    helper.assertTrue(Snakes.isPath(helper, head.relative(Direction.SOUTH, 2)), "the path ahead should still be there");
                    helper.assertTrue(Snakes.isPath(helper, head.relative(Direction.SOUTH, 4)), "the path ahead should still be there");
                })
                .thenSucceed();
    }

    /** A plain snake eats its path; nothing is left where it has been. */
    @GameTest(maxTicks = 60)
    public void aplainSnakeEatsItsPath(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 3);

        start(helper, TAIL);

        helper.startSequence()
                .thenIdle(FAST_STEP * 2 + 2)
                .thenExecute(() -> helper.assertFalse(Snakes.isPath(helper, TAIL), "a plain snake should leave nothing behind it"))
                .thenSucceed();
    }

    /** A respawnable one lays its path again behind its tail, so the whole route survives the run. */
    @GameTest(maxTicks = 60)
    public void arespawnableSnakeLeavesItsPathBehind(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        helper.setBlock(head, helper.getBlockState(head).setValue(SnakeBlock.RESPAWNABLE, true));
        Snakes.drawPath(helper, head, Direction.SOUTH, 3);

        start(helper, TAIL);

        helper.startSequence()
                .thenIdle(FAST_STEP * 2 + 2)
                .thenExecute(() -> helper.assertTrue(Snakes.isPath(helper, TAIL),
                        "a respawnable snake should leave a path block where it started"))
                .thenSucceed();
    }

    /** Once it is done, a respawnable snake is back where it started, idle and whole. */
    @GameTest(maxTicks = 160)
    public void arespawnableSnakeComesBackWhereItStarted(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        helper.setBlock(head, helper.getBlockState(head).setValue(SnakeBlock.RESPAWNABLE, true));
        Snakes.drawPath(helper, head, Direction.SOUTH, 2);

        start(helper, TAIL);

        helper.succeedWhen(() -> {
            helper.assertTrue(Snakes.isSnake(helper, TAIL, SnakeBlockState.IDLE), "the tail should be back where it started, idle");
            helper.assertTrue(Snakes.isSnake(helper, TAIL.relative(Direction.SOUTH), SnakeBlockState.IDLE),
                    "and so should the rest of the body");
            helper.assertTrue(helper.getBlockState(TAIL).getValue(SnakeBlock.RESPAWNABLE), "a respawned snake is still respawnable");
            helper.assertTrue(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH, 2)), "with its whole route in front of it again");
            helper.assertTrue(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH, 3)), "with its whole route in front of it again");
        });
    }

    /** A respawn with no room is a snake that is simply gone: nothing around it is touched. */
    @GameTest(maxTicks = 160)
    public void arespawnWithNoRoomRemovesTheSnakeForGood(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        helper.setBlock(head, helper.getBlockState(head).setValue(SnakeBlock.RESPAWNABLE, true));
        Snakes.drawPath(helper, head, Direction.SOUTH, 2);

        var snake = start(helper, TAIL);

        helper.startSequence()
                // Dropped in once the snake has left, so that its old spot is taken when it tries to return.
                .thenExecuteAfter(FAST_STEP * 2 + 2, () -> helper.setBlock(TAIL, net.minecraft.world.level.block.Blocks.OBSIDIAN))
                .thenWaitUntil(() -> helper.assertTrue(isForgotten(helper, snake),
                        "the snake should give up rather than break what is in its way"))
                .thenExecute(() -> {
                    helper.assertTrue(helper.getBlockState(TAIL).is(net.minecraft.world.level.block.Blocks.OBSIDIAN),
                            "whatever took its place should be left exactly where it is");
                    helper.assertFalse(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH)),
                            "a snake that cannot come back takes its whole path with it");
                })
                .thenSucceed();
    }

    /**
     * The head shoves whatever is standing where it is about to be, rather than swallowing it.
     * <p>
     * The push is a move and not a velocity, so whatever is pushed keeps the momentum it had — which
     * is the whole reason a snake can be run across without the parkour falling apart. A cow stands
     * in for the player here: the push goes through {@code Entity.move} and does not care what it is
     * shoving, and the only player a game test can make has no connection behind it to keep it where
     * the test put it.
     */
    @GameTest(maxTicks = 60)
    public void thegrowingHeadPushesWhatIsInItsWay(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 3);
        var cow = helper.spawnWithNoFreeWill(EntityTypes.COW, head.relative(Direction.SOUTH));
        double startZ = cow.getZ();

        start(helper, TAIL);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(cow.getZ() > startZ + 0.4D,
                        "a head growing into an occupied cell should shove what is standing there along"))
                .thenSucceed();
    }

    /**
     * A snake rising under something carries it straight up, and never sideways.
     * <p>
     * Getting this wrong is not subtle. Vanilla answers an entity caught inside a solid block by
     * shoving it out of the nearest side and overwriting its horizontal speed as it does, so a head
     * that leaves whatever stands on it even slightly inside itself throws that thing off the snake
     * and takes its momentum with it. The collider therefore has to sit where the block is this
     * tick, not where it is being drawn, which trails by up to a tick.
     */
    @GameTest(maxTicks = 80)
    public void arisingSnakeCarriesWhatStandsOnItStraightUp(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.UP, 1);
        Snakes.drawPath(helper, TAIL, Direction.UP, 3);
        var cow = helper.spawnWithNoFreeWill(EntityTypes.COW, TAIL.above());
        double[] origin = new double[3];

        helper.startSequence()
                // Let it settle onto the block before anything starts moving.
                .thenIdle(4)
                .thenExecute(() -> {
                    origin[0] = cow.getX();
                    origin[1] = cow.getY();
                    origin[2] = cow.getZ();
                    start(helper, TAIL);
                })
                .thenIdle(FAST_STEP * 2 + 2)
                .thenExecute(() -> {
                    helper.assertTrue(cow.getY() > origin[1] + 1.5D,
                            "a rising snake should have carried what stands on it up with it");
                    helper.assertTrue(Math.abs(cow.getX() - origin[0]) < 0.1D && Math.abs(cow.getZ() - origin[2]) < 0.1D,
                            "and should never have shoved it out sideways");
                })
                .thenSucceed();
    }

    /**
     * With nowhere left to be shoved, the thing in the way gets crushed instead of pushed through.
     * <p>
     * A cow rather than a player: the only player a game test can make is in creative, and nothing
     * crushes those.
     */
    @GameTest(maxTicks = 60)
    public void anentityPushedIntoAwallIsCrushed(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.FAST_SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 1);
        BlockPos squeezed = head.relative(Direction.SOUTH);
        for (int dy = 0; dy <= 2; dy++) {
            helper.setBlock(squeezed.relative(Direction.SOUTH).above(dy), Blocks.OBSIDIAN);
        }
        var cow = helper.spawnWithNoFreeWill(EntityTypes.COW, squeezed);
        float health = cow.getHealth();

        start(helper, TAIL);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(cow.getHealth() < health,
                        "an entity with a wall behind it should be crushed, not shoved through"))
                .thenSucceed();
    }

    /** A snake only moves while the cell it is heading for is loaded. */
    @GameTest(maxTicks = 40)
    public void therecordSurvivesAsLongAsTheSnakeDoes(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SLOW_SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Snakes.drawPath(helper, head, Direction.SOUTH, 3);

        var snake = start(helper, TAIL);

        helper.assertTrue(snake != null, "starting a snake should hand back its record");
        helper.assertTrue(SnakeManager.get(helper.getLevel()).snakes().stream().anyMatch(other -> other.id() == snake.id()),
                "the level should have taken the snake on");
        helper.assertValueEqual(snake.speed(), SnakeSpeed.SLOW, "the record should carry the speed of the blocks it is made of");
        helper.succeed();
    }
}
