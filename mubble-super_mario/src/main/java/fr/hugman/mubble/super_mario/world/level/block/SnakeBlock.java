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
 * While the snake is {@link SnakeBlockState#IDLE} it is nothing more than the blocks themselves —
 * {@link SnakeChain} reads the chain out of the world through {@link #FACING}. The moment it starts,
 * {@link SnakeManager} takes over and drives it from a record of its own, and the blocks only carry
 * the state the renderer and the player can see.
 *
 * @author komerish
 * @author Hugman
 * @since v4.0.0
 */
public class SnakeBlock extends Block implements Fallable {
    /** The next block of the chain: the one closer to the head, then the path in front of it. */
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final EnumProperty<SnakeBlockState> SNAKE_STATE = EnumProperty.create("snake_state", SnakeBlockState.class);
    /**
     * Whether the snake puts itself back together once it is done. Only commands set this: there is
     * no item for it, and the only way to flip it while playing is the debug stick.
     */
    public static final BooleanProperty RESPAWNABLE = BooleanProperty.create("respawnable");
    /**
     * Whether a redstone signal currently reaches the block. Kept in the state so that one pulse only
     * ever moves a deactivated block one step along: wake up first, start on the next pulse.
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
     * Points the fresh block at the tail it is lengthening, or away from whatever it was placed
     * against when it is a snake of its own.
     * <p>
     * A block only joins the snake behind it when it is the very same one — speeds never mix — when
     * that snake is still idle, when the clicked face really is its back, and when there is room left
     * under {@link SnakeChain#MAX_BODY_LENGTH}. Anything else starts a snake one block long.
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction face = ctx.getClickedFace();
        BlockState base = this.defaultBlockState().setValue(FACING, face);
        BlockPos against = ctx.getClickedPos().relative(face.getOpposite());
        BlockState againstState = ctx.getLevel().getBlockState(against);

        if (!this.extendsTail(ctx.getLevel(), against, againstState, face)) {
            return base;
        }
        return base.setValue(FACING, face.getOpposite())
                .setValue(RESPAWNABLE, againstState.getValue(RESPAWNABLE));
    }

    private boolean extendsTail(Level level, BlockPos against, BlockState againstState, Direction face) {
        if (!SnakeChain.isLink(againstState, this, SnakeBlockState.IDLE)) {
            return false;
        }
        // The back of the tail is the face opposite the one it points at, and a tail is a block with
        // nothing of its own chain pointing at it.
        if (againstState.getValue(FACING) != face
                || SnakeChain.predecessor(level, against, this, SnakeBlockState.IDLE) != null) {
            return false;
        }
        return SnakeChain.body(level, against).size() < SnakeChain.MAX_BODY_LENGTH;
    }

    /*========*/
    /*  PATH  */
    /*========*/

    /**
     * Draws the first block of the path when the head is clicked with a snake block in hand, rather
     * than placing that block against it.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(SuperMarioItemTags.SNAKE_BLOCKS) || state.getValue(SNAKE_STATE) != SnakeBlockState.IDLE) {
            return InteractionResult.PASS;
        }
        // Only the head starts a path, and only while it has none: once there is one, the player
        // carries on from its last block instead.
        if (!pos.equals(SnakeChain.headOf(level, pos)) || !SnakeChain.path(level, pos).isEmpty()) {
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
     * Redstone drives both of the things a signal can do to a snake block: set an idle one off, and
     * bring a deactivated one back.
     * <p>
     * Only the rising edge counts, which is what {@link #POWERED} is for. Without it a single pulse
     * would wake a deactivated block and start it in the same breath, and the two-pulse sequence the
     * deactivated state is built around could never be performed.
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
        switch (state.getValue(SNAKE_STATE)) {
            case DEACTIVATED -> level.setBlock(pos, level.getBlockState(pos).setValue(SNAKE_STATE, SnakeBlockState.IDLE), Block.UPDATE_ALL);
            case IDLE -> SnakeManager.get(serverLevel).start(serverLevel, pos);
            case MOVING -> {
            }
        }
    }

    /*===========*/
    /*  FALLING  */
    /*===========*/

    /**
     * A respawnable block never lands: the snake it came from is going to be rebuilt whole, so letting
     * the pieces pile up would hand the player a free copy of every block it ever lost.
     */
    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedState, FallingBlockEntity entity) {
        if (state.getValue(RESPAWNABLE)) {
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
