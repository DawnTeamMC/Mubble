package fr.hugman.mubble.super_mario.world.level.snake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlockState;
import fr.hugman.mubble.super_mario.world.level.block.SnakeMovement;
import fr.hugman.mubble.super_mario.world.level.block.SnakeMovingBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakePathBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeSpeed;
import fr.hugman.mubble.super_mario.world.level.block.SuperMarioBlocks;
import fr.hugman.mubble.super_mario.world.level.block.entity.SnakeMovingBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * One snake that has been set off, and everything needed to keep it walking across a restart.
 * <p>
 * The body is held here rather than read back out of the world every tick, because a running snake
 * is not a plain chain any more: its two ends spend most of their time as
 * {@link SnakeMovingBlockEntity moving blocks} rather than snake blocks. Keeping the list also gives
 * the one place where the world is checked against what the snake believes — {@link #validate} — and
 * that single check is what catches a block being mined, blown up, pushed by a piston or replaced by
 * a command, all of which split the snake the same way.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class MovingSnake {
    /** How long the whole snake trembles at the end of its path, before it starts to fall. */
    public static final int SHAKE_TICKS = SharedConstants.TICKS_PER_SECOND * 2;
    /** Ticks between two blocks letting go, once the snake starts coming apart. */
    public static final int FALL_INTERVAL = 3;

    public static final Codec<MovingSnake> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(snake -> snake.id),
            SnakeSpeed.CODEC.fieldOf("speed").forGetter(snake -> snake.speed),
            Codec.BOOL.fieldOf("respawnable").forGetter(snake -> snake.respawnable),
            BlockPos.CODEC.listOf().fieldOf("body").forGetter(snake -> snake.body),
            BlockPos.CODEC.listOf().fieldOf("start_body").forGetter(snake -> snake.startBody),
            Direction.CODEC.fieldOf("start_facing").forGetter(snake -> snake.startFacing),
            SnakePhase.CODEC.fieldOf("phase").forGetter(snake -> snake.phase),
            Codec.INT.fieldOf("phase_ticks").forGetter(snake -> snake.phaseTicks),
            BlockPos.CODEC.optionalFieldOf("moving_tail").forGetter(snake -> snake.movingTail)
    ).apply(instance, MovingSnake::new));

    private final int id;
    private final SnakeSpeed speed;
    private final boolean respawnable;
    /** Every block of the snake, tail first and head last. */
    private List<BlockPos> body;
    /** Where the body stood when the snake was set off, for a respawnable snake to come back to. */
    private final List<BlockPos> startBody;
    /** Which way the head pointed when the snake was set off: the first step of its path. */
    private final Direction startFacing;
    private SnakePhase phase;
    private int phaseTicks;
    /** The cell the tail is sliding out of, while a step is under way. */
    private Optional<BlockPos> movingTail;

    public MovingSnake(int id, SnakeSpeed speed, boolean respawnable, List<BlockPos> body, List<BlockPos> startBody,
                       Direction startFacing, SnakePhase phase, int phaseTicks, Optional<BlockPos> movingTail) {
        this.id = id;
        this.speed = speed;
        this.respawnable = respawnable;
        this.body = new ArrayList<>(body);
        this.startBody = List.copyOf(startBody);
        this.startFacing = startFacing;
        this.phase = phase;
        this.phaseTicks = phaseTicks;
        this.movingTail = movingTail;
    }

    public int id() {
        return this.id;
    }

    public SnakeSpeed speed() {
        return this.speed;
    }

    public boolean respawnable() {
        return this.respawnable;
    }

    public List<BlockPos> body() {
        return List.copyOf(this.body);
    }

    public SnakePhase phase() {
        return this.phase;
    }

    /** Where the head is right now, or {@code null} once the snake has nothing left. */
    @Nullable
    public BlockPos head() {
        return this.body.isEmpty() ? null : this.body.getLast();
    }

    /*===========*/
    /*  TICKING  */
    /*===========*/

    /**
     * Advances the snake by one tick.
     *
     * @return whether the snake is still around afterwards
     */
    public boolean tick(ServerLevel level) {
        if (!this.validate(level)) {
            return false;
        }
        return switch (this.phase) {
            case MOVING -> this.tickMoving(level);
            case SHAKING -> this.tickShaking(level);
            case FALLING -> this.tickFalling(level);
        };
    }

    private boolean tickMoving(ServerLevel level) {
        if (this.movingTail.isEmpty()) {
            return this.beginStep(level);
        }
        if (++this.phaseTicks < this.speed.ticksPerBlock()) {
            return true;
        }
        this.finishStep(level);
        // Straight into the next one, so that the snake never stands still between two blocks.
        return this.beginStep(level);
    }

    /**
     * Hands the next cell of the path to a growing head and lets the tail start sliding out of the
     * cell it is leaving.
     *
     * @return whether the snake is still around afterwards
     */
    private boolean beginStep(ServerLevel level) {
        BlockPos head = this.head();
        if (head == null) {
            return this.finish(level);
        }
        BlockState headState = level.getBlockState(head);
        if (!(headState.getBlock() instanceof SnakeBlock)) {
            return true;
        }

        Direction direction = headState.getValue(SnakeBlock.FACING);
        BlockPos next = head.relative(direction);
        // A snake whose next block has drifted out of the loaded world waits there until it is back.
        if (!level.isLoaded(next)) {
            return true;
        }
        BlockState nextState = level.getBlockState(next);
        if (!(nextState.getBlock() instanceof SnakePathBlock)) {
            return this.beginShake(level);
        }
        Direction nextFacing = nextState.getValue(SnakePathBlock.FACING);

        BlockPos tail = this.body.getFirst();
        Direction tailDirection = this.body.size() > 1 ? directionBetween(tail, this.body.get(1)) : direction;
        // A snake one block long is its own head and tail: the growing head already covers the whole
        // slide, so the block it leaves behind carries nothing to draw.
        BlockState tailMoved = this.body.size() > 1 ? level.getBlockState(tail) : Blocks.AIR.defaultBlockState();
        BlockState leftBehind = this.leftBehind(tailDirection, tail.equals(this.startBody.getFirst()));

        this.placeMovingBlock(level, next, headState.setValue(SnakeBlock.FACING, nextFacing),
                headState.setValue(SnakeBlock.FACING, nextFacing), direction, nextFacing, SnakeMovement.HEAD);
        this.placeMovingBlock(level, tail, tailMoved, leftBehind, tailDirection, tailDirection, SnakeMovement.TAIL);

        this.body.removeFirst();
        this.body.add(next);
        this.movingTail = Optional.of(tail);
        this.phaseTicks = 0;
        return true;
    }

    /** Swaps both ends of the step for the blocks they were heading towards. */
    private void finishStep(ServerLevel level) {
        this.movingTail.ifPresent(pos -> placeFinalState(level, pos));
        BlockPos head = this.head();
        if (head != null) {
            placeFinalState(level, head);
        }
        this.movingTail = Optional.empty();
        this.phaseTicks = 0;
    }

    /**
     * What the tail leaves in the cell it has just left: nothing at all, or — for a respawnable snake
     * — the path block it walked in on, so that the whole route is still there to come back to.
     */
    private BlockState leftBehind(Direction tailDirection, boolean isStart) {
        if (!this.respawnable) {
            return Blocks.AIR.defaultBlockState();
        }
        return SuperMarioBlocks.SNAKE_PATH.defaultBlockState()
                .setValue(SnakePathBlock.FACING, tailDirection)
                .setValue(SnakePathBlock.ROOT, isStart);
    }

    private void placeMovingBlock(ServerLevel level, BlockPos pos, BlockState moved, BlockState finalState,
                                  Direction direction, Direction link, SnakeMovement movement) {
        BlockState blockState = SuperMarioBlocks.SNAKE_MOVING_BLOCK.defaultBlockState().setValue(SnakeMovingBlock.FACING, link);
        level.setBlock(pos, blockState, Block.UPDATE_ALL);
        level.setBlockEntity(new SnakeMovingBlockEntity(pos, blockState, moved, finalState, direction, movement,
                this.speed.ticksPerBlock(), this.id));
    }

    private static void placeFinalState(ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof SnakeMovingBlockEntity moving) {
            moving.place(level, pos);
        }
    }

    /*===================*/
    /*  END OF THE PATH  */
    /*===================*/

    /** Freezes the whole snake where it stands and has it tremble for a couple of seconds. */
    private boolean beginShake(ServerLevel level) {
        this.cancelStep(level);
        this.phase = SnakePhase.SHAKING;
        this.phaseTicks = 0;

        for (BlockPos pos : this.body) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof SnakeBlock) {
                this.placeMovingBlock(level, pos, state, state, state.getValue(SnakeBlock.FACING),
                        state.getValue(SnakeBlock.FACING), SnakeMovement.SHAKE);
            }
        }
        BlockPos head = this.head();
        if (head != null) {
            level.playSound(null, head, SuperMarioSounds.SNAKE_BLOCK_SHAKE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    private boolean tickShaking(ServerLevel level) {
        if (++this.phaseTicks < SHAKE_TICKS) {
            return true;
        }
        this.phase = SnakePhase.FALLING;
        this.phaseTicks = 0;
        return true;
    }

    /** Lets go of one block at a time, starting at the tail. */
    private boolean tickFalling(ServerLevel level) {
        if (this.phaseTicks++ % FALL_INTERVAL != 0) {
            return true;
        }
        if (!this.body.isEmpty()) {
            this.dropAsFallingBlock(level, this.body.removeFirst());
        }
        return !this.body.isEmpty() || this.finish(level);
    }

    /**
     * Drops one block of the snake.
     * <p>
     * It falls as the ordinary snake block it is and lands as one, ready to be walked on again the
     * moment it has a route to take. A respawnable one drops nothing and vanishes where it lands:
     * the snake is about to be rebuilt whole, so anything it leaves behind would be a free copy of
     * itself.
     */
    private void dropAsFallingBlock(ServerLevel level, BlockPos pos) {
        BlockState snakeState = takeSnakeState(level, pos);
        if (snakeState == null) {
            return;
        }
        level.setBlock(pos, snakeState
                .setValue(SnakeBlock.SNAKE_STATE, SnakeBlockState.IDLE)
                .setValue(SnakeBlock.POWERED, false), Block.UPDATE_ALL);
        FallingBlockEntity falling = FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
        falling.dropItem = !this.respawnable;

        // A respawnable snake leaves its route behind whatever way a block of it goes, not just
        // behind the tail: the blocks it is standing on when it reaches the end of its path, and the
        // ones broken off it on the way, are cells the tail would have laid a path block in later.
        // Without this the last stretch of the route would be missing when the snake comes back.
        if (this.respawnable) {
            level.setBlock(pos, this.leftBehind(snakeState.getValue(SnakeBlock.FACING), pos.equals(this.startBody.getFirst())),
                    Block.UPDATE_ALL);
        }
    }

    /**
     * The snake block standing at {@code pos}, taking it out of a moving end first if that is what it
     * is, or {@code null} when the block there is no longer one of ours.
     */
    @Nullable
    private static BlockState takeSnakeState(ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof SnakeMovingBlockEntity moving) {
            BlockState moved = moving.getMovedState();
            moving.setRemoved();
            level.removeBlockEntity(pos);
            return moved.getBlock() instanceof SnakeBlock ? moved : null;
        }
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof SnakeBlock ? state : null;
    }

    /*===========*/
    /*  SPLITS   */
    /*===========*/

    /**
     * Checks the world still holds every block the snake thinks it has, and breaks it where it does
     * not.
     *
     * @return whether the snake is still around afterwards
     */
    private boolean validate(ServerLevel level) {
        if (this.phase != SnakePhase.MOVING) {
            return !this.body.isEmpty() || this.finish(level);
        }
        for (int i = 0; i < this.body.size(); i++) {
            BlockPos pos = this.body.get(i);
            if (!level.isLoaded(pos) || this.occupies(level, pos)) {
                continue;
            }
            return this.splitAt(level, i);
        }
        return true;
    }

    /** Whether {@code pos} still belongs to this snake, moving end or plain block alike. */
    private boolean occupies(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof SnakeBlock) {
            return state.getValue(SnakeBlock.SNAKE_STATE) == SnakeBlockState.MOVING;
        }
        return state.getBlock() instanceof SnakeMovingBlock
                && level.getBlockEntity(pos) instanceof SnakeMovingBlockEntity moving
                && moving.getSnakeId() == this.id;
    }

    /**
     * Breaks the snake at {@code index}: everything from there back to the tail comes off and falls,
     * and the part still holding the head carries on as a shorter snake.
     *
     * @return whether the snake is still around afterwards
     */
    private boolean splitAt(ServerLevel level, int index) {
        this.cancelStep(level);
        for (int i = 0; i < index; i++) {
            this.dropAsFallingBlock(level, this.body.get(i));
        }
        this.body = new ArrayList<>(this.body.subList(Math.min(index + 1, this.body.size()), this.body.size()));
        this.phaseTicks = 0;
        return !this.body.isEmpty() || this.finish(level);
    }

    /** Puts both ends of a half-finished step down where they were heading, so the body is whole again. */
    private void cancelStep(ServerLevel level) {
        this.movingTail.ifPresent(pos -> placeFinalState(level, pos));
        this.movingTail = Optional.empty();
        BlockPos head = this.head();
        if (head != null) {
            placeFinalState(level, head);
        }
        this.phaseTicks = 0;
    }

    /*=============*/
    /*  RESPAWNING */
    /*=============*/

    /**
     * Wraps the snake up once it has nothing left, putting a respawnable one back where it started.
     *
     * @return always {@code false}: the record goes either way, and a respawned snake is idle again
     */
    private boolean finish(ServerLevel level) {
        this.cancelStep(level);
        if (this.respawnable && !this.respawn(level)) {
            this.clearPath(level);
        }
        return false;
    }

    /**
     * Rebuilds the snake, idle and at full length, where it was set off.
     * <p>
     * The path is already there: a respawnable snake lays it again behind its tail as it walks, so
     * every block of the original route is still in the world and the body simply takes the front of
     * it back.
     *
     * @return whether there was room to come back
     */
    private boolean respawn(ServerLevel level) {
        for (BlockPos pos : this.startBody) {
            if (!level.isLoaded(pos)) {
                return false;
            }
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && !(state.getBlock() instanceof SnakePathBlock) && !state.canBeReplaced()) {
                return false;
            }
        }

        Block block = SuperMarioBlocks.snakeBlock(this.speed);
        for (int i = 0; i < this.startBody.size(); i++) {
            Direction facing = i + 1 < this.startBody.size()
                    ? directionBetween(this.startBody.get(i), this.startBody.get(i + 1))
                    : this.startFacing;
            level.setBlock(this.startBody.get(i), block.defaultBlockState()
                    .setValue(SnakeBlock.FACING, facing)
                    .setValue(SnakeBlock.SNAKE_STATE, SnakeBlockState.IDLE)
                    .setValue(SnakeBlock.RESPAWNABLE, true)
                    .setValue(SnakeBlock.POWERED, false), Block.UPDATE_ALL);
        }
        return true;
    }

    /**
     * Takes the whole route away when a respawnable snake has nowhere to come back to.
     * <p>
     * Whatever is standing in the starting spot is left alone: a snake that cannot come back is a
     * snake that is gone, not an excuse to break the build around it.
     */
    private void clearPath(ServerLevel level) {
        // The stretch the body stood on is walked by position rather than by following the chain,
        // since one of those cells is the very thing that blocked the respawn and is not ours.
        for (BlockPos pos : this.startBody) {
            removeIfPath(level, pos);
        }
        BlockPos current = this.startBody.getLast().relative(this.startFacing);
        for (int i = 0; i < SnakeChain.MAX_PATH_LENGTH; i++) {
            if (!level.isLoaded(current)) {
                return;
            }
            BlockState state = level.getBlockState(current);
            if (!(state.getBlock() instanceof SnakePathBlock)) {
                return;
            }
            BlockPos next = current.relative(state.getValue(SnakePathBlock.FACING));
            level.removeBlock(current, false);
            current = next;
        }
    }

    private static void removeIfPath(ServerLevel level, BlockPos pos) {
        if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof SnakePathBlock) {
            level.removeBlock(pos, false);
        }
    }

    /*==========*/
    /*  SHARED  */
    /*==========*/

    static Direction directionBetween(BlockPos from, BlockPos to) {
        Direction direction = Direction.getNearest(to.subtract(from), null);
        if (direction == null) {
            SuperMario.LOGGER.warn("Snake blocks at {} and {} are not neighbours", from, to);
            return Direction.NORTH;
        }
        return direction;
    }
}
