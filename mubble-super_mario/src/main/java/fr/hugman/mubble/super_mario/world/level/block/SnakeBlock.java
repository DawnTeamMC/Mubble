package fr.hugman.mubble.super_mario.world.level.block;

import fr.hugman.mubble.super_mario.tags.SuperMarioItemTags;
import fr.hugman.mubble.super_mario.world.level.snake.SnakeChain;
import fr.hugman.mubble.super_mario.world.level.snake.SnakeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * A snake block: one link of a chain that, once a player steps on it, walks the path drawn in front
 * of its head one block at a time, growing at the head and shrinking at the tail.
 * <p>
 * While the snake is {@link SnakeBlockState#IDLE} it is nothing more than the blocks themselves,
 * touching each other in whatever shape they were laid out: they carry no orientation a player can
 * see, and {@link SnakeChain} reads the body off the blocks that touch. The moment it starts,
 * {@link SnakeManager} takes over and drives it from a record of its own.
 * <p>
 * {@link #FACING} is not that orientation. It is only ever read on the head, where it says which way
 * the path goes, and {@link SnakeManager} writes the rest of the chain into it as the snake starts so
 * that the running snake has an order to it. Nothing about it shows in the model.
 *
 * @author komerish
 * @author Hugman
 * @since v4.0.0
 */
public class SnakeBlock extends Block implements Fallable {
    /**
     * Where this block links on: the way into the path for a head, and the next block along for the
     * rest of a snake that is already running. It is never drawn, and means nothing on an idle block
     * that no path was drawn from.
     */
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final EnumProperty<SnakeBlockState> SNAKE_STATE = EnumProperty.create("snake_state", SnakeBlockState.class);
    /**
     * Whether the snake puts itself back together once it is done. Only commands set this: there is
     * no item for it, and the only way to flip it while playing is the debug stick.
     */
    public static final BooleanProperty RESPAWNABLE = BooleanProperty.create("respawnable");
    /**
     * Whether a redstone signal currently reaches the block, so that only the moment it arrives sets
     * a snake off. A signal left switched on would otherwise try again on every block update around
     * it, and a snake coming back from a respawn next to one would never get to sit still.
     */
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private final SnakeSpeed speed;

    public SnakeBlock(Properties settings, SnakeSpeed speed) {
        super(settings);
        this.speed = speed;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(SNAKE_STATE, SnakeBlockState.IDLE)
                .setValue(RESPAWNABLE, false)
                .setValue(POWERED, false));
    }

    public SnakeSpeed speed() {
        return this.speed;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SNAKE_STATE, RESPAWNABLE, POWERED);
    }

    /*=============*/
    /*  PLACEMENT  */
    /*=============*/

    /**
     * A snake block is laid down the same way round whichever face it was placed against: the shape
     * of a snake is the shape the blocks were laid out in, not the order they were placed in.
     * <p>
     * All the state carries is a default link direction, which means nothing until a path is drawn
     * off this block, and whether the block it was placed against comes back — so that a respawnable
     * snake stays respawnable as it is built.
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        BlockState base = this.defaultBlockState().setValue(FACING, face);
        BlockState againstState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(face.getOpposite()));
        return againstState.is(this) ? base.setValue(RESPAWNABLE, againstState.getValue(RESPAWNABLE)) : base;
    }

    /*========*/
    /*  PATH  */
    /*========*/

    /**
     * Starts the path off this block when it is clicked with a snake block in hand, rather than
     * placing that block against it. Whichever block the path is drawn from becomes the head.
     * <p>
     * Only a clump with no path yet answers: once one has been drawn, clicking a snake block places
     * another and the path carries on from its own last block instead. To stack blocks against each
     * other before there is a path, sneak — vanilla already takes that to mean "place it, do not use
     * what I clicked".
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(SuperMarioItemTags.SNAKE_BLOCKS) || state.getValue(SNAKE_STATE) != SnakeBlockState.IDLE) {
            return InteractionResult.PASS;
        }
        if (SnakeChain.head(level, pos) != null) {
            return InteractionResult.PASS;
        }
        return SnakePathBlock.extendPath(level, player, pos, hit.getDirection());
    }

    /*============*/
    /*  STARTING  */
    /*============*/

    /** A player walking over any block of an idle snake sets it off. Spectators walk over nothing. */
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (level instanceof ServerLevel serverLevel
                && entity instanceof Player player
                && !player.isSpectator()
                && state.getValue(SNAKE_STATE) == SnakeBlockState.IDLE) {
            SnakeManager.get(serverLevel).start(serverLevel, pos);
        }
    }

    /**
     * A signal arriving next to any block of a snake sets it off, the same as a player stepping on
     * one. Only the moment it arrives counts, which is what {@link #POWERED} is for.
     */
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbourBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        boolean powered = level.hasNeighborSignal(pos);
        if (powered == state.getValue(POWERED)) {
            return;
        }
        level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
        if (!powered) {
            return;
        }
        if (state.getValue(SNAKE_STATE) == SnakeBlockState.IDLE) {
            SnakeManager.get(serverLevel).start(serverLevel, pos);
        }
    }

    /*===========*/
    /*  FALLING  */
    /*===========*/

    /**
     * A respawnable block never lands: the snake it came from is going to be rebuilt whole, so letting
     * the pieces pile up would hand the player a free copy of every block it ever lost.
     * <p>
     * Whatever it came down on is put back rather than cleared, because what it lands on is very
     * often a block of the snake's own route: a respawnable snake lays its path again as it comes
     * apart, and a block of it falling through that would tear a hole in the way home.
     */
    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedState, FallingBlockEntity entity) {
        if (!state.getValue(RESPAWNABLE)) {
            return;
        }
        if (replacedState.getBlock() instanceof SnakePathBlock) {
            level.setBlock(pos, replacedState, Block.UPDATE_ALL);
        } else {
            level.removeBlock(pos, false);
        }
    }

    /*============*/
    /*  ROTATION  */
    /*============*/

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
