package fr.hugman.mubble.super_mario.world.level.snake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlock;
import fr.hugman.mubble.super_mario.world.level.block.SnakeBlockState;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Every snake currently running in one level, kept across restarts.
 * <p>
 * Only snakes that have been set off live here. An idle one is nothing but the blocks themselves, so
 * there is nothing to save and nothing to go stale; the record starts when a player steps on one and
 * ends when the snake has no blocks left.
 * <p>
 * Snakes are ticked at the very end of the level tick, after block entities have had theirs: the two
 * moving ends of a step advance their own progress and push whatever is in the way, and the manager
 * then swaps them for real blocks on the tick they arrive, so one step runs straight into the next
 * with nothing standing still in between.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class SnakeManager extends SavedData {
    public static final Codec<SnakeManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MovingSnake.CODEC.listOf().optionalFieldOf("snakes", List.of()).forGetter(manager -> manager.snakes),
            Codec.INT.optionalFieldOf("next_id", 1).forGetter(manager -> manager.nextId)
    ).apply(instance, SnakeManager::new));

    /**
     * Mubble has no data fixers of its own, and none of the vanilla ones touch a file written under
     * this type, so it only ever serves to stamp a data version on the file.
     */
    public static final SavedDataType<SnakeManager> TYPE = new SavedDataType<>(
            SuperMario.id("snakes"), SnakeManager::new, CODEC, DataFixTypes.SAVED_DATA_RANDOM_SEQUENCES);

    private final List<MovingSnake> snakes;
    private int nextId;

    public SnakeManager() {
        this(List.of(), 1);
    }

    public SnakeManager(List<MovingSnake> snakes, int nextId) {
        this.snakes = new ArrayList<>(snakes);
        this.nextId = nextId;
    }

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(SnakeManager::tick);
    }

    /** The record for {@code level}, creating it if this is the first snake it ever sees. */
    public static SnakeManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    private static void tick(ServerLevel level) {
        SnakeManager manager = level.getDataStorage().get(TYPE);
        if (manager != null && !manager.snakes.isEmpty()) {
            manager.tickAll(level);
        }
    }

    public List<MovingSnake> snakes() {
        return List.copyOf(this.snakes);
    }

    /** The snake {@code pos} belongs to, if it belongs to one at all. */
    @Nullable
    public MovingSnake snakeAt(BlockPos pos) {
        for (MovingSnake snake : this.snakes) {
            if (snake.body().contains(pos)) {
                return snake;
            }
        }
        return null;
    }

    private void tickAll(ServerLevel level) {
        Iterator<MovingSnake> iterator = this.snakes.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().tick(level)) {
                iterator.remove();
            }
        }
        this.setDirty();
    }

    /**
     * Sets off the snake {@code pos} belongs to.
     * <p>
     * The head has the last word on {@link SnakeBlock#RESPAWNABLE}: it is the block a player builds
     * towards and the one a command is most likely aimed at, and a snake half of which comes back
     * would make no sense. Its value is written onto every block as the snake starts.
     *
     * @return the snake that was set off, or {@code null} if there was nothing to set off
     */
    @Nullable
    public MovingSnake start(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SnakeBlock snakeBlock) || state.getValue(SnakeBlock.SNAKE_STATE) != SnakeBlockState.IDLE) {
            return null;
        }
        List<BlockPos> body = SnakeChain.body(level, pos);
        if (body.isEmpty()) {
            return null;
        }

        BlockState headState = level.getBlockState(body.getLast());
        boolean respawnable = headState.getValue(SnakeBlock.RESPAWNABLE);
        for (BlockPos blockPos : body) {
            level.setBlock(blockPos, level.getBlockState(blockPos)
                    .setValue(SnakeBlock.SNAKE_STATE, SnakeBlockState.MOVING)
                    .setValue(SnakeBlock.RESPAWNABLE, respawnable), Block.UPDATE_ALL);
        }

        MovingSnake snake = new MovingSnake(this.nextId++, snakeBlock.speed(), respawnable, body, body,
                headState.getValue(SnakeBlock.FACING), SnakePhase.MOVING, 0, Optional.empty());
        this.snakes.add(snake);
        this.setDirty();
        return snake;
    }
}
