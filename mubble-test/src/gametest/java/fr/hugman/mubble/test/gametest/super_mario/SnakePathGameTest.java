package fr.hugman.mubble.test.gametest.super_mario;

import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakePathBlock;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.snake.SnakeChain;
import fr.hugman.mubble.test.gametest.support.Arena;
import fr.hugman.mubble.test.gametest.support.Snakes;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;

/**
 * The route drawn in front of a snake: how it is built, how it comes apart, and the ways in which it
 * is and is not air.
 */
public class SnakePathGameTest {
    private static final BlockPos TAIL = new BlockPos(1, Arena.FLOOR_Y + 1, 1);

    /** Whichever block the path is drawn from becomes the head: the clump has no end until then. */
    @GameTest
    public void clickingAsnakeBlockWithAsnakeBlockDrawsThePath(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var end = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Player player = Snakes.holdingSnakeBlock(helper);

        Snakes.clickFace(helper, player, end, Direction.SOUTH);

        BlockPos drawn = end.relative(Direction.SOUTH);
        helper.assertTrue(Snakes.isPath(helper, drawn), "clicking a snake block should draw a path block, not place one");
        helper.assertBlockProperty(end, SnakeBlock.FACING, Direction.SOUTH);
        helper.assertValueEqual(SnakeChain.head(helper.getLevel(), helper.absolutePos(TAIL)), helper.absolutePos(end),
                "and should make that block the head of the snake");
        helper.succeed();
    }

    @GameTest
    public void apathMayStartOnAnyFace(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var end = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Player player = Snakes.holdingSnakeBlock(helper);

        Snakes.clickFace(helper, player, end, Direction.EAST);

        helper.assertTrue(Snakes.isPath(helper, end.relative(Direction.EAST)), "a path may start on any face");
        helper.assertBlockProperty(end, SnakeBlock.FACING, Direction.EAST);
        helper.succeed();
    }

    /**
     * Once a clump has a path, clicking one of its blocks goes back to placing a snake block against
     * it: a snake has one head, and the path carries on from its own last block.
     */
    @GameTest
    public void asecondPathIsNeverDrawnOffTheSameSnake(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Snakes.drawPath(helper, head, Direction.SOUTH, 1);
        Player player = Snakes.holdingSnakeBlock(helper);

        Snakes.clickFace(helper, player, TAIL, Direction.WEST);

        helper.assertFalse(Snakes.isPath(helper, TAIL.relative(Direction.WEST)),
                "a snake that already has a path should not grow a second one");
        helper.succeed();
    }

    @GameTest
    public void thepathCarriesOnFromItsLastBlock(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Player player = Snakes.holdingSnakeBlock(helper);
        Snakes.clickFace(helper, player, head, Direction.SOUTH);
        BlockPos first = head.relative(Direction.SOUTH);


        Snakes.clickFace(helper, player, first, Direction.EAST);

        helper.assertTrue(Snakes.isPath(helper, first.relative(Direction.EAST)), "the last path block should grow the path");
        helper.assertValueEqual(helper.getBlockState(first).getValue(SnakePathBlock.FACING), Direction.EAST,
                "a bend should point the way it turns");
        helper.succeed();
    }

    @GameTest
    public void anemptyHandDrawsNothing(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        Player player = helper.makeMockPlayer(GameType.CREATIVE);
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        Snakes.clickFace(helper, player, head, Direction.SOUTH);

        helper.assertFalse(Snakes.isPath(helper, head.relative(Direction.SOUTH)), "only a snake block in hand draws a path");
        helper.succeed();
    }

    /**
     * Breaking one block of a path clears everything after it, one block update at a time, the way a
     * chorus plant comes down.
     */
    @GameTest(maxTicks = 40)
    public void breakingApathBlockClearsTheRestOfThePath(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 4);
        BlockPos second = head.relative(Direction.SOUTH, 2);

        helper.destroyBlock(second);

        helper.succeedWhen(() -> {
            helper.assertTrue(Snakes.isPath(helper, head.relative(Direction.SOUTH)), "the block before the break should stay");
            for (int i = 2; i <= 4; i++) {
                helper.assertFalse(Snakes.isPath(helper, head.relative(Direction.SOUTH, i)),
                        "everything after the break should be gone");
            }
        });
    }

    @GameTest(maxTicks = 40)
    public void ablockBuiltIntoApathClearsTheRestOfIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 4);

        helper.setBlock(head.relative(Direction.SOUTH, 2), Blocks.STONE);

        helper.succeedWhen(() -> helper.assertFalse(Snakes.isPath(helper, head.relative(Direction.SOUTH, 3)),
                "a path block replaced by anything should take the rest of the path with it"));
    }

    /**
     * A route that has never been walked belongs to the snake it was drawn off, and goes when that
     * snake does. Only a route a snake has actually set off down outlives it.
     */
    @GameTest(maxTicks = 40)
    public void adrawnRouteGoesWithTheSnakeItWasDrawnFrom(GameTestHelper helper) {
        Arena.buildFloor(helper);
        Snakes.place(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL);
        Player player = Snakes.holdingSnakeBlock(helper);
        Snakes.clickFace(helper, player, TAIL, Direction.SOUTH);
        Snakes.clickFace(helper, player, TAIL.relative(Direction.SOUTH), Direction.SOUTH);

        helper.destroyBlock(TAIL);

        helper.succeedWhen(() -> {
            helper.assertFalse(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH)),
                    "the route should go when the snake it hangs off does");
            helper.assertFalse(Snakes.isPath(helper, TAIL.relative(Direction.SOUTH, 2)), "all of it");
        });
    }

    @GameTest
    public void breakingTheLastPathBlockOnlyRemovesIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 3);

        helper.destroyBlock(head.relative(Direction.SOUTH, 3));

        helper.assertTrue(Snakes.isPath(helper, head.relative(Direction.SOUTH)), "undoing the last step leaves the rest alone");
        helper.assertTrue(Snakes.isPath(helper, head.relative(Direction.SOUTH, 2)), "undoing the last step leaves the rest alone");
        helper.succeed();
    }

    @GameTest
    public void apathBlockIsOnlySolidToWhoeverCanSeeIt(GameTestHelper helper) {
        Arena.buildFloor(helper);
        BlockPos pos = TAIL;
        helper.setBlock(pos, SuperMarioBlocks.SNAKE_PATH);
        var state = helper.getBlockState(pos);
        var level = helper.getLevel();
        var absolute = helper.absolutePos(pos);

        var empty = state.getShape(level, absolute, CollisionContext.empty());
        var holding = state.getShape(level, absolute, CollisionContext.of(Snakes.holdingSnakeBlock(helper)));

        helper.assertTrue(empty.isEmpty(), "a path block should not even be aimable without a snake block in hand");
        helper.assertFalse(holding.isEmpty(), "a path block should be a full cube to whoever is building the snake");
        helper.assertTrue(state.getCollisionShape(level, absolute, CollisionContext.empty()).isEmpty(),
                "nothing ever collides with a path block");
        helper.succeed();
    }

    @GameTest
    public void anythingMayBeBuiltIntoApathBlock(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TAIL, SuperMarioBlocks.SNAKE_PATH);

        helper.setBlock(TAIL, Blocks.STONE);

        helper.assertBlockPresent(Blocks.STONE, TAIL);
        helper.succeed();
    }

    /**
     * Water stops at a path block, the same as it would at any other block. It may waterlog one on
     * the way, exactly as it does a slab, but it never washes it away.
     */
    @GameTest(maxTicks = 40)
    public void waterDoesNotWashApathBlockAway(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 2);
        BlockPos path = head.relative(Direction.SOUTH);
        helper.setBlock(path.above(), Blocks.WATER);

        helper.startSequence()
                .thenIdle(20)
                .thenExecute(() -> {
                    helper.assertTrue(Snakes.isPath(helper, path), "water should not wash a path block away");
                    helper.assertTrue(Snakes.isPath(helper, path.relative(Direction.SOUTH)), "nor the one after it");
                })
                .thenSucceed();
    }

    /** A path block with nothing pointing at it is a leftover, and takes itself away. */
    @GameTest(maxTicks = 40)
    public void apathBlockWithNothingPointingAtItRemovesItself(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TAIL, SuperMarioBlocks.SNAKE_PATH);

        // Any block update next to it is enough to make it notice.
        helper.setBlock(TAIL.above(), Blocks.STONE);

        helper.succeedWhen(() -> helper.assertFalse(Snakes.isPath(helper, TAIL),
                "a path block nothing points at should not stay in the world"));
    }

    @GameTest
    public void apathBlockShrugsOffExplosions(GameTestHelper helper) {
        Arena.buildFloor(helper);
        helper.setBlock(TAIL, SuperMarioBlocks.SNAKE_PATH);

        helper.assertTrue(SuperMarioBlocks.SNAKE_PATH.getExplosionResistance() > 100.0F,
                "an explosion next to a path would be a silent way to break the snake it belongs to");
        helper.succeed();
    }

    @GameTest
    public void thepathIsCountedFromTheHead(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 1);
        Snakes.drawPath(helper, head, Direction.SOUTH, 3);
        var level = helper.getLevel();

        helper.assertValueEqual(SnakeChain.path(level, helper.absolutePos(head)).size(), 3, "the whole path should be read back");
        helper.assertValueEqual(SnakeChain.pathLengthEndingAt(level, helper.absolutePos(head.relative(Direction.SOUTH, 3))), 3,
                "counting backwards from the last block should agree");
        helper.succeed();
    }

    /** A path that runs into the snake it belongs to is a dead end, not a loop. */
    @GameTest
    public void apathRunningIntoTheSnakeStopsThere(GameTestHelper helper) {
        Arena.buildFloor(helper);
        var head = Snakes.build(helper, SuperMarioBlocks.SNAKE_BLOCK, TAIL, Direction.SOUTH, 2);
        helper.setBlock(head.relative(Direction.EAST), SuperMarioBlocks.SNAKE_PATH.defaultBlockState()
                .setValue(SnakePathBlock.FACING, Direction.NORTH));
        helper.setBlock(head, helper.getBlockState(head).setValue(SnakeBlock.FACING, Direction.EAST));

        helper.assertValueEqual(SnakeChain.path(helper.getLevel(), helper.absolutePos(head)).size(), 1,
                "a path pointing back at a snake block should end where it meets it");
        helper.succeed();
    }
}
