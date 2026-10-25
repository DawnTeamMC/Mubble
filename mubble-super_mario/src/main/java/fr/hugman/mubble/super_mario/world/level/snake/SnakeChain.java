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
 * What turns a clump into a snake is a route to take. Any block of the clump touching the start of
 * one is the head, and the body is read off from there by stepping to a touching block over and
 * over, carrying on straight wherever it can. A clump touching no route is not a snake yet and has
 * no head to find, which is why stepping on one does nothing.
 * <p>
 * Nothing about that is settled when the blocks are laid down, so a snake that has come apart is
 * not finished with: drop its blocks next to a route that is still standing and they are a snake
 * again.
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
     * The head of the snake {@code pos} belongs to: a block of the clump standing against the start
     * of a route.
     *
     * @return the head, or {@code null} when the clump has no route to take and so is not a snake yet
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
            if (routeFrom(level, current) != null) {
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

    /**
     * Which way out of {@code pos} a route begins, if one does.
     * <p>
     * Only the start of a route counts. A path running along the side of a snake is somebody else's
     * route, or the stretch this one has already walked, and neither is something to set off down.
     */
    @Nullable
    public static Direction routeFrom(BlockGetter level, BlockPos pos) {
        if (!(level.getBlockState(pos).getBlock() instanceof SnakeBlock)) {
            return null;
        }
        for (Direction direction : Direction.values()) {
            if (startsAroute(level, pos.relative(direction))) {
                return direction;
            }
        }
        return null;
    }

    /** Whether {@code pos} is a path block that no other path block leads into. */
    public static boolean startsAroute(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof SnakePathBlock && pathLeadingInto(level, pos) == null;
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
        Direction route = routeFrom(level, head);
        if (route == null) {
            return List.of();
        }
        List<BlockPos> path = new ArrayList<>();
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = head.relative(route);
        while (path.size() < MAX_PATH_LENGTH && level.getBlockState(current).getBlock() instanceof SnakePathBlock && seen.add(current)) {
            path.add(current);
            current = current.relative(level.getBlockState(current).getValue(SnakePathBlock.FACING));
        }
        return path;
    }

    /**
     * The path block leading into the one at {@code pos}, if any.
     * <p>
     * Only a path block counts here. A snake standing against a route block keeps it in the world,
     * but it is not <em>on</em> the route, which is the difference between the start of a route and
     * the middle of one.
     */
    @Nullable
    public static BlockPos pathLeadingInto(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbour = pos.relative(direction);
            BlockState state = level.getBlockState(neighbour);
            if (state.getBlock() instanceof SnakePathBlock && state.getValue(SnakePathBlock.FACING) == direction.getOpposite()) {
                return neighbour;
            }
        }
        return null;
    }

    /**
     * Whether anything still lays claim to the path block at {@code pos}: a path block leading into
     * it, or a snake standing against it that could set off down it.
     * <p>
     * A route block nothing claims has been cut loose and takes itself away, which is how breaking
     * one block of a route clears everything after it. A snake counts because a route is drawn off
     * one and walked by one; a moving end counts for the same reason, or a snake eating its own
     * route would wipe out the rest of it the moment its head left the ground.
     */
    public static boolean isClaimed(BlockGetter level, BlockPos pos) {
        if (pathLeadingInto(level, pos) != null) {
            return true;
        }
        for (Direction direction : Direction.values()) {
            Block neighbour = level.getBlockState(pos.relative(direction)).getBlock();
            if (neighbour instanceof SnakeBlock || neighbour instanceof SnakeMovingBlock) {
                return true;
            }
        }
        return false;
    }

    /** How many path blocks have been drawn up to and including {@code pos}. */
    public static int pathLengthEndingAt(BlockGetter level, BlockPos pos) {
        int length = 0;
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = pos;
        while (level.getBlockState(current).getBlock() instanceof SnakePathBlock && seen.add(current) && length <= MAX_PATH_LENGTH) {
            length++;
            BlockPos previous = pathLeadingInto(level, current);
            if (previous == null) {
                break;
            }
            current = previous;
        }
        return length;
    }
}
