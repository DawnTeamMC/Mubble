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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reading a snake out of the world.
 * <p>
 * A snake is never stored anywhere while it is idle: it is a clump of snake blocks touching each
 * other, and nothing more. Lay them out however you like — the blocks carry no orientation a player
 * can see, and none that matters until the snake runs.
 * <p>
 * What turns a clump into a snake is the path. The block a path was drawn from is the head, and the
 * body is read off from there by stepping to a touching block over and over, carrying on straight
 * wherever it can. A clump with no path on it is not a snake yet and has no head to find, which is
 * why stepping on one does nothing.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class SnakeChain {
    /** Longest a snake body may be, in blocks. A longer clump simply leaves its far end behind. */
    public static final int MAX_BODY_LENGTH = 16;
    /** Longest a path may be, in blocks. MarioWiki gives 120 blocks as a snake's maximum travel. */
    public static final int MAX_PATH_LENGTH = 120;
    /** How far the hunt for a head is willing to wander through a clump of snake blocks. */
    private static final int MAX_GROUP_SIZE = MAX_BODY_LENGTH * 4;

    private SnakeChain() {
    }

    /** Whether {@code state} belongs to the same snake as a block made of {@code block} in {@code snakeState}. */
    public static boolean isLink(BlockState state, Block block, SnakeBlockState snakeState) {
        return state.is(block) && state.getValue(SnakeBlock.SNAKE_STATE) == snakeState;
    }

    /*========*/
    /*  HEAD  */
    /*========*/

    /**
     * The head of the snake {@code pos} belongs to: the one block of the clump with a path drawn
     * off it.
     *
     * @return the head, or {@code null} when the clump has no path and so is not a snake yet
     */
    @Nullable
    public static BlockPos head(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SnakeBlock)) {
            return null;
        }
        Block block = state.getBlock();
        SnakeBlockState snakeState = state.getValue(SnakeBlock.SNAKE_STATE);

        Set<BlockPos> seen = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        seen.add(pos);
        queue.add(pos);
        while (!queue.isEmpty() && seen.size() <= MAX_GROUP_SIZE) {
            BlockPos current = queue.poll();
            if (startsApath(level, current)) {
                return current;
            }
            for (Direction direction : Direction.values()) {
                BlockPos neighbour = current.relative(direction);
                if (isLink(level.getBlockState(neighbour), block, snakeState) && seen.add(neighbour)) {
                    queue.add(neighbour);
                }
            }
        }
        return null;
    }

    /** Whether the block at {@code pos} is a snake block with a path hanging off the face it points at. */
    public static boolean startsApath(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof SnakeBlock
                && level.getBlockState(pos.relative(state.getValue(SnakeBlock.FACING))).getBlock() instanceof SnakePathBlock;
    }

    /*========*/
    /*  BODY  */
    /*========*/

    /**
     * The chain running back from {@code head} through the blocks touching it, tail first and head
     * last.
     * <p>
     * At every block the walk carries on the way it was going if it can, and only turns when it has
     * to, so a straight line and a line with a bend in it both read the way they look. A clump that
     * is not a line has no one right answer: the walk takes the first chain it finds and whatever it
     * does not reach is simply not part of the snake.
     */
    public static List<BlockPos> bodyFromHead(BlockGetter level, BlockPos head) {
        BlockState state = level.getBlockState(head);
        if (!(state.getBlock() instanceof SnakeBlock)) {
            return List.of();
        }
        Block block = state.getBlock();
        SnakeBlockState snakeState = state.getValue(SnakeBlock.SNAKE_STATE);

        List<BlockPos> body = new ArrayList<>();
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = head;
        Direction carryOn = null;
        while (body.size() < MAX_BODY_LENGTH && seen.add(current)) {
            body.add(current);
            Direction next = step(level, current, carryOn, seen, block, snakeState);
            if (next == null) {
                break;
            }
            carryOn = next;
            current = current.relative(next);
        }
        Collections.reverse(body);
        return body;
    }

    @Nullable
    private static Direction step(BlockGetter level, BlockPos pos, @Nullable Direction carryOn, Set<BlockPos> seen,
                                  Block block, SnakeBlockState snakeState) {
        if (carryOn != null && leadsOn(level, pos.relative(carryOn), seen, block, snakeState)) {
            return carryOn;
        }
        for (Direction direction : Direction.values()) {
            if (leadsOn(level, pos.relative(direction), seen, block, snakeState)) {
                return direction;
            }
        }
        return null;
    }

    private static boolean leadsOn(BlockGetter level, BlockPos pos, Set<BlockPos> seen, Block block, SnakeBlockState snakeState) {
        return !seen.contains(pos) && isLink(level.getBlockState(pos), block, snakeState);
    }

    /** The whole snake {@code pos} belongs to, tail first, or nothing when its clump has no path. */
    public static List<BlockPos> body(BlockGetter level, BlockPos pos) {
        BlockPos head = head(level, pos);
        return head == null ? List.of() : bodyFromHead(level, head);
    }

    /*========*/
    /*  PATH  */
    /*========*/

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
            if (pointsAt(level.getBlockState(neighbour)) == direction.getOpposite()) {
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
