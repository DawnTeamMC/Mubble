package fr.hugman.mubble.super_mario.world.level.snake;

import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlockState;
import fr.hugman.mubble.super_mario.world.level.block.SnakeMovingBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakePathBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reading a snake out of the world.
 * <p>
 * A snake is never stored anywhere while it is idle: it only exists as a run of snake blocks whose
 * {@link SnakeBlock#FACING} each point at the next one, tail first, then the head, then the path
 * blocks it will walk. Everything here walks that chain, which is why breaking a block in the middle
 * of an idle snake simply splits it in two without anything having to be told.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class SnakeChain {
    /** Longest a snake body may be, in blocks. */
    public static final int MAX_BODY_LENGTH = 16;
    /** Longest a path may be, in blocks. MarioWiki gives 120 blocks as a snake's maximum travel. */
    public static final int MAX_PATH_LENGTH = 120;

    private SnakeChain() {
    }

    /** Whether {@code state} is a link of a chain made of {@code block} in {@code snakeState}. */
    public static boolean isLink(BlockState state, Block block, SnakeBlockState snakeState) {
        return state.is(block) && state.getValue(SnakeBlock.SNAKE_STATE) == snakeState;
    }

    /** The direction {@code state} points at, or {@code null} if it is not a snake block at all. */
    @Nullable
    public static Direction facingOf(BlockState state) {
        return state.getBlock() instanceof SnakeBlock ? state.getValue(SnakeBlock.FACING) : null;
    }

    /**
     * The block of the same chain pointing at {@code pos}, if there is one.
     * <p>
     * Two snakes may well sit next to each other, so a neighbour only counts when it is the very same
     * block — the same speed, that is — in the same state, and actually facing this way.
     */
    @Nullable
    public static BlockPos predecessor(BlockGetter level, BlockPos pos, Block block, SnakeBlockState snakeState) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbour = pos.relative(direction);
            BlockState state = level.getBlockState(neighbour);
            if (isLink(state, block, snakeState) && state.getValue(SnakeBlock.FACING) == direction.getOpposite()) {
                return neighbour;
            }
        }
        return null;
    }

    /** The block {@code pos} points at, if it carries on the same chain. */
    @Nullable
    public static BlockPos successor(BlockGetter level, BlockPos pos, Block block, SnakeBlockState snakeState) {
        BlockState state = level.getBlockState(pos);
        if (!isLink(state, block, snakeState)) {
            return null;
        }
        BlockPos next = pos.relative(state.getValue(SnakeBlock.FACING));
        return isLink(level.getBlockState(next), block, snakeState) ? next : null;
    }

    /** Walks backwards from {@code pos} to the tail of its snake. Returns {@code pos} if it is the tail. */
    public static BlockPos tailOf(BlockGetter level, BlockPos pos, Block block, SnakeBlockState snakeState) {
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = pos;
        seen.add(current);
        for (int i = 0; i < MAX_BODY_LENGTH; i++) {
            BlockPos previous = predecessor(level, current, block, snakeState);
            // A ring of snake blocks all pointing at each other has no tail: stop where we started.
            if (previous == null || !seen.add(previous)) {
                return current;
            }
            current = previous;
        }
        return current;
    }

    /** Every block of the snake {@code pos} belongs to, tail first and head last. */
    public static List<BlockPos> body(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SnakeBlock)) {
            return List.of();
        }
        return bodyFromTail(level, tailOf(level, pos, state.getBlock(), state.getValue(SnakeBlock.SNAKE_STATE)),
                state.getBlock(), state.getValue(SnakeBlock.SNAKE_STATE));
    }

    /** Every block of the snake starting at {@code tail}, tail first and head last. */
    public static List<BlockPos> bodyFromTail(BlockGetter level, BlockPos tail, Block block, SnakeBlockState snakeState) {
        List<BlockPos> body = new ArrayList<>();
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = tail;
        while (body.size() < MAX_BODY_LENGTH && isLink(level.getBlockState(current), block, snakeState) && seen.add(current)) {
            body.add(current);
            current = current.relative(level.getBlockState(current).getValue(SnakeBlock.FACING));
        }
        return body;
    }

    /** The head of the snake {@code pos} belongs to, or {@code null} if {@code pos} is not a snake block. */
    @Nullable
    public static BlockPos headOf(BlockGetter level, BlockPos pos) {
        List<BlockPos> body = body(level, pos);
        return body.isEmpty() ? null : body.getLast();
    }

    /**
     * The path blocks laid out in front of {@code head}, in the order the snake will walk them.
     * <p>
     * A path that runs back into itself or into a snake block is a dead end, not a loop: the walk
     * simply stops there, and the snake will reach the end of its path when it gets that far.
     */
    public static List<BlockPos> path(BlockGetter level, BlockPos head) {
        BlockState headState = level.getBlockState(head);
        if (!(headState.getBlock() instanceof SnakeBlock)) {
            return List.of();
        }
        List<BlockPos> path = new ArrayList<>();
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = head.relative(headState.getValue(SnakeBlock.FACING));
        while (path.size() < MAX_PATH_LENGTH && level.getBlockState(current).getBlock() instanceof SnakePathBlock && seen.add(current)) {
            path.add(current);
            current = current.relative(level.getBlockState(current).getValue(SnakePathBlock.FACING));
        }
        return path;
    }

    /** The last path block in front of {@code head}, or {@code null} when no path has been drawn yet. */
    @Nullable
    public static BlockPos pathEnd(BlockGetter level, BlockPos head) {
        List<BlockPos> path = path(level, head);
        return path.isEmpty() ? null : path.getLast();
    }

    /**
     * Whatever still points at the path block at {@code pos}: the block before it on the path, the
     * head of the snake about to walk it, or that head caught halfway into its next cell.
     * <p>
     * A path block with nothing pointing at it has been cut loose and takes itself away, which is how
     * breaking one block of a path clears everything after it. The moving block counts for exactly
     * that reason: a snake eating its own path would otherwise wipe out the rest of it the moment its
     * head left the ground.
     */
    @Nullable
    public static BlockPos pathPredecessor(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbour = pos.relative(direction);
            BlockState state = level.getBlockState(neighbour);
            Direction facing = pointsAt(state);
            if (facing == direction.getOpposite()) {
                return neighbour;
            }
        }
        return null;
    }

    /** The way {@code state} links on, whether it is a snake block, a path block or a moving end. */
    @Nullable
    private static Direction pointsAt(BlockState state) {
        if (state.getBlock() instanceof SnakeBlock) {
            return state.getValue(SnakeBlock.FACING);
        }
        if (state.getBlock() instanceof SnakePathBlock) {
            return state.getValue(SnakePathBlock.FACING);
        }
        if (state.getBlock() instanceof SnakeMovingBlock) {
            return state.getValue(SnakeMovingBlock.FACING);
        }
        return null;
    }

    /** How many path blocks have been drawn up to and including {@code pos}. */
    public static int pathLengthEndingAt(BlockGetter level, BlockPos pos) {
        int length = 0;
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = pos;
        while (level.getBlockState(current).getBlock() instanceof SnakePathBlock && seen.add(current) && length <= MAX_PATH_LENGTH) {
            length++;
            BlockPos previous = pathPredecessor(level, current);
            if (previous == null) {
                break;
            }
            current = previous;
        }
        return length;
    }
}
