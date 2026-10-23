package fr.hugman.mubble.super_mario.world.level.block;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.super_mario.sounds.SuperMarioSounds;
import fr.hugman.mubble.super_mario.tags.SuperMarioItemTags;
import fr.hugman.mubble.super_mario.world.level.block.entity.SnakePathBlockEntity;
import fr.hugman.mubble.super_mario.world.level.snake.SnakeChain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * One step of the route a snake will walk, drawn block by block in front of its head.
 * <p>
 * Outside of that, a path block behaves like air: nothing collides with it, anything may be built or
 * pushed into it, explosions ignore it and pistons wipe it out. It only exists for the player
 * building the snake, so it is only visible — and therefore only breakable — while a snake block is
 * in hand, which is what {@link #getShape} keys off {@link CollisionContext}, the way scaffolding
 * does.
 * <p>
 * Removing one takes the rest of the path with it. That happens the way chorus plants and scaffolding
 * fall apart rather than through a recursive sweep: every path block checks in {@link #updateShape}
 * whether anything still points at it and quietly turns to air when nothing does, which makes the
 * block after it check in turn.
 *
 * @author Hugman
 * @since v4.0.0
 */
public class SnakePathBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<SnakePathBlock> CODEC = simpleCodec(SnakePathBlock::new);

    /** The next block of the path, the way the snake will go. */
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    /**
     * Whether this block starts a path of its own and so needs nothing pointing at it to stay.
     * <p>
     * A respawnable snake lays its path again behind its tail as it goes, and the very first of those
     * blocks — the spot the snake started from — has nothing in front of it. Without this it would
     * take the whole trail down with it on the next block update.
     */
    public static final BooleanProperty ROOT = BooleanProperty.create("root");

    public SnakePathBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(ROOT, false));
    }

    @Override
    protected MapCodec<SnakePathBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, ROOT);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SnakePathBlockEntity(pos, state);
    }

    /*==============*/
    /*  VISIBILITY  */
    /*==============*/

    /** Whether {@code entity} is holding a snake block, and so is shown the paths. */
    public static boolean isVisibleTo(@Nullable Entity entity) {
        return entity instanceof LivingEntity living
                && (living.getMainHandItem().is(SuperMarioItemTags.SNAKE_BLOCKS)
                || living.getOffhandItem().is(SuperMarioItemTags.SNAKE_BLOCKS));
    }

    private static boolean isVisibleIn(CollisionContext context) {
        return context instanceof EntityCollisionContext entityContext && isVisibleTo(entityContext.getEntity());
    }

    /*===========*/
    /*  SHAPES   */
    /*===========*/

    /**
     * The outline, which doubles as what the player can aim at: a full cube for whoever is holding a
     * snake block, and nothing at all for everyone else. A path block nobody can see is a path block
     * nobody can click on, break, or even pick.
     */
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return isVisibleIn(context) ? Shapes.block() : Shapes.empty();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.empty();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        // Drawn by SnakePathBlockRenderer instead: a chunk mesh is baked once and cannot know what
        // the player happens to be holding when it is drawn.
        return RenderShape.INVISIBLE;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
    }

    /*===========*/
    /*  BREAKING */
    /*===========*/

    /**
     * Instant by hand, and impossible otherwise. The outline is already empty for anyone without a
     * snake block in hand, so this only ever comes up for something aiming at the block another way.
     */
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return isVisibleTo(player) ? 1.0F : 0.0F;
    }

    /*================*/
    /*  REPLACEMENT   */
    /*================*/

    /** Anything may be built or dropped into a path block, exactly as if it were air. */
    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return true;
    }

    /**
     * Flowing water and lava stop at a path block all the same. It is not solid, which is normally
     * enough for a fluid to wash straight through, and a path drawn next to a lake would then drain
     * it one block at a time.
     */
    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /*==================*/
    /*  CHAIN REACTION  */
    /*==================*/

    /**
     * Falls apart as soon as whatever pointed at this block stops doing so, which is what carries the
     * break down the rest of the path one block update at a time.
     */
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (!state.getValue(ROOT) && SnakeChain.pathPredecessor(level, pos) == null) {
            return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState().createLegacyBlock() : Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    /*===========*/
    /*  DRAWING  */
    /*===========*/

    /** Carries the path on from its last block, the same way the head starts it. */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(SuperMarioItemTags.SNAKE_BLOCKS)) {
            return InteractionResult.PASS;
        }
        // Only the last block of a path grows it; clicking one in the middle would fork the route.
        if (level.getBlockState(pos.relative(state.getValue(FACING))).getBlock() instanceof SnakePathBlock) {
            return InteractionResult.PASS;
        }
        return extendPath(level, player, pos, hit.getDirection());
    }

    /**
     * Adds one path block on the {@code direction} face of {@code from}, and points {@code from} at
     * it.
     * <p>
     * Both sides run the checks — they only read the world, and the client needs the same answer to
     * decide whether to swing — but only the server touches anything.
     */
    public static InteractionResult extendPath(Level level, Player player, BlockPos from, Direction direction) {
        BlockPos target = from.relative(direction);
        BlockState targetState = level.getBlockState(target);

        // Running back into the path or into the snake itself is a dead end, not a turn.
        if (!targetState.canBeReplaced() || targetState.getBlock() instanceof SnakePathBlock || targetState.getBlock() instanceof SnakeBlock) {
            return InteractionResult.PASS;
        }
        if (SnakeChain.pathLengthEndingAt(level, from) >= SnakeChain.MAX_PATH_LENGTH) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockState fromState = level.getBlockState(from);
        boolean waterlogged = targetState.getFluidState().is(Fluids.WATER);
        // The head and a path block link on through the same property, so one line points either.
        level.setBlockAndUpdate(from, fromState.setValue(FACING, direction));
        level.setBlockAndUpdate(target, SuperMarioBlocks.SNAKE_PATH.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(WATERLOGGED, waterlogged)
                // The first block of a route answers to nothing on a path, so it has to be allowed
                // to stand without one: a route outlives the snake that was going to walk it.
                .setValue(ROOT, fromState.getBlock() instanceof SnakeBlock));

        if (level instanceof ServerLevel serverLevel) {
            playForViewers(serverLevel, target, SuperMarioSounds.SNAKE_PATH_PLACE, 0.8F);
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    /*===========*/
    /*  EFFECTS  */
    /*===========*/

    /**
     * Sees a path block off with the same sound and dust its placing got.
     * <p>
     * A snake eating its own path is not a break: the block is replaced by the head sliding into it,
     * and that already has a sound of its own.
     */
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Block replacement = level.getBlockState(pos).getBlock();
        if (replacement instanceof SnakeMovingBlock || replacement instanceof SnakeBlock) {
            return;
        }
        playForViewers(level, pos, SuperMarioSounds.SNAKE_PATH_BREAK, 1.0F);
    }

    /**
     * Path blocks are silent and particle-less to the world at large — {@link SoundType#EMPTY} and an
     * empty outline see to that — so the sound and the dust of one appearing or going away are sent
     * by hand, and only to the players who were able to see it in the first place.
     */
    public static void playForViewers(ServerLevel level, BlockPos pos, Holder<SoundEvent> sound, float pitch) {
        ClientboundSoundPacket packet = new ClientboundSoundPacket(sound, SoundSource.BLOCKS,
                pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1.0F, pitch, level.getRandom().nextLong());
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, SuperMarioBlocks.SNAKE_PATH.defaultBlockState());

        for (ServerPlayer player : level.players()) {
            if (!isVisibleTo(player)) {
                continue;
            }
            player.connection.send(packet);
            level.sendParticles(player, particle, false, false,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 8, 0.25D, 0.25D, 0.25D, 0.0D);
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
