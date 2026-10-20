package fr.hugman.mubble.test.gametest.support;

import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlockState;
import fr.hugman.mubble.super_mario.world.level.block.SnakePathBlock;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Building snakes for the tests to take apart.
 * <p>
 * Everything here works in the structure-relative coordinates the tests use, and lays the blocks out
 * the way a player would: a snake is a clump of blocks touching each other, so a test that drops
 * them in place is testing the same world an in-game one would.
 * <p>
 * The blocks are deliberately laid down all facing {@link Direction#DOWN}, into the floor. Nothing
 * reads a snake block's facing except the head, where it points into the path, so a test that passes
 * with every block pointing the wrong way is a test that the shape really does come from which
 * blocks touch.
 */
public final class Snakes {
    private Snakes() {
    }

    /**
     * Lays {@code length} blocks in a straight line, from {@code tail} towards {@code direction}.
     *
     * @return the far end of the line, which is where a path would normally be drawn from
     */
    public static BlockPos build(GameTestHelper helper, Block block, BlockPos tail, Direction direction, int length) {
        BlockPos pos = tail;
        for (int i = 0; i < length; i++) {
            place(helper, block, pos);
            pos = pos.relative(direction);
        }
        return tail.relative(direction, length - 1);
    }

    /** Lays one snake block, pointing nowhere in particular. */
    public static void place(GameTestHelper helper, Block block, BlockPos pos) {
        helper.setBlock(pos, block.defaultBlockState().setValue(SnakeBlock.FACING, Direction.DOWN));
    }

    /**
     * Draws a straight path of {@code length} blocks out of the {@code direction} face of
     * {@code head}, and points the head into it, which is what makes the clump a snake.
     */
    public static void drawPath(GameTestHelper helper, BlockPos head, Direction direction, int length) {
        helper.setBlock(head, helper.getBlockState(head).setValue(SnakeBlock.FACING, direction));
        BlockPos pos = head.relative(direction);
        for (int i = 0; i < length; i++) {
            helper.setBlock(pos, SuperMarioBlocks.SNAKE_PATH.defaultBlockState().setValue(SnakePathBlock.FACING, direction));
            pos = pos.relative(direction);
        }
    }

    /** The state {@code block} would be placed in when the {@code face} of {@code clicked} is clicked. */
    public static BlockState placementStateAgainst(GameTestHelper helper, Block block, BlockPos clicked, Direction face) {
        return block.getStateForPlacement(placeContext(helper, block, clicked, face));
    }

    /** Places {@code block} against the {@code face} of {@code clicked}, the way a player would. */
    public static void placeAgainst(GameTestHelper helper, Block block, BlockPos clicked, Direction face) {
        helper.setBlock(clicked.relative(face), placementStateAgainst(helper, block, clicked, face));
    }

    private static BlockPlaceContext placeContext(GameTestHelper helper, Block block, BlockPos clicked, Direction face) {
        BlockPos absolute = helper.absolutePos(clicked);
        Vec3 hit = Vec3.atCenterOf(absolute).add(face.getStepX() * 0.5D, face.getStepY() * 0.5D, face.getStepZ() * 0.5D);
        return new BlockPlaceContext(helper.getLevel(), null, InteractionHand.MAIN_HAND, new ItemStack(block),
                new BlockHitResult(hit, face, absolute, false));
    }

    /** A creative player with a snake block in hand, which is what makes paths visible and clickable. */
    public static Player holdingSnakeBlock(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.CREATIVE);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(SuperMarioBlocks.SNAKE_BLOCK));
        return player;
    }

    /** Clicks the {@code face} of {@code pos} with a snake block in hand. */
    public static void clickFace(GameTestHelper helper, Player player, BlockPos pos, Direction face) {
        BlockPos absolute = helper.absolutePos(pos);
        Vec3 hit = Vec3.atCenterOf(absolute).add(face.getStepX() * 0.5D, face.getStepY() * 0.5D, face.getStepZ() * 0.5D);
        helper.useBlock(pos, player, new BlockHitResult(hit, face, absolute, false));
    }

    /** Whether {@code pos} holds a snake block in the given state. */
    public static boolean isSnake(GameTestHelper helper, BlockPos pos, SnakeBlockState state) {
        BlockState blockState = helper.getBlockState(pos);
        return blockState.getBlock() instanceof SnakeBlock && blockState.getValue(SnakeBlock.SNAKE_STATE) == state;
    }

    /** Whether {@code pos} holds a path block. */
    public static boolean isPath(GameTestHelper helper, BlockPos pos) {
        return helper.getBlockState(pos).getBlock() instanceof SnakePathBlock;
    }
}
