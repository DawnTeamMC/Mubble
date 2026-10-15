package fr.hugman.mubble.super_mario.world.level.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.super_mario.world.level.block.entity.SnakeMovingBlockEntity;
import fr.hugman.mubble.super_mario.world.level.block.entity.SuperMarioBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Stands in for a snake block while one of its ends is between two cells, the way
 * {@code MovingPistonBlock} stands in for a block a piston is pushing.
 * <p>
 * The block itself holds almost nothing: the shape, the drops and the whole of the movement live in
 * {@link SnakeMovingBlockEntity}. {@link #FACING} is the one exception, and it is not the direction
 * of travel — it is the link the moved snake block carries, so that a path block in front of the
 * head still has something pointing at it while the head is halfway there and does not take the rest
 * of the path down with it.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class SnakeMovingBlock extends BaseEntityBlock {
    public static final MapCodec<SnakeMovingBlock> CODEC = simpleCodec(SnakeMovingBlock::new);

    /** The link the snake block being moved carries, kept visible to the rest of the chain. */
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public SnakeMovingBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<SnakeMovingBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SnakeMovingBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, SuperMarioBlockEntityTypes.SNAKE_MOVING_BLOCK, SnakeMovingBlockEntity::tick);
    }

    @Nullable
    private static SnakeMovingBlockEntity getBlockEntity(BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof SnakeMovingBlockEntity entity ? entity : null;
    }

    /*===========*/
    /*  SHAPES   */
    /*===========*/

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SnakeMovingBlockEntity entity = getBlockEntity(level, pos);
        return entity != null ? entity.getCollisionShape(level, pos) : Shapes.empty();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    /*==========*/
    /*  DROPS   */
    /*==========*/

    /**
     * Whatever is caught mid-step drops as the snake block it really is, so that breaking a snake
     * the tick its head arrives is worth the same as breaking it a tick later.
     */
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        SnakeMovingBlockEntity entity = getBlockEntity(params.getLevel(), BlockPos.containing(params.getParameter(LootContextParams.ORIGIN)));
        return entity == null ? List.of() : entity.getMovedState().getDrops(params);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        SnakeMovingBlockEntity entity = level.getBlockEntity(pos) instanceof SnakeMovingBlockEntity moving ? moving : null;
        return entity == null ? ItemStack.EMPTY : new ItemStack(entity.getMovedState().getBlock());
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
