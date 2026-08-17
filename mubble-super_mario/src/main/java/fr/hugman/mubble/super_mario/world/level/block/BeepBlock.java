package fr.hugman.mubble.super_mario.world.level.block;

import fr.hugman.mubble.super_mario.world.level.gamerules.SuperMarioGameRules;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * @author komerish
 * @author Napero
 * @author Hugman
 * @since v4.0.0
 */
public class BeepBlock extends Block {
    public static final int DEFAULT_COOLDOWN = SharedConstants.TICKS_PER_SECOND * 4;
    public static final BooleanProperty FRAME = BooleanProperty.create("frame");

    public final boolean offset;

    public BeepBlock(Properties settings, boolean offset) {
        super(settings);
        this.offset = offset;
        this.registerDefaultState(defaultBlockState().setValue(FRAME, false));
    }

    public static Properties settings(MapColor mapColor) {
        return Properties.of().mapColor(mapColor)
                .sound(SoundType.AMETHYST)
                .strength(1.5f).requiresCorrectToolForDrops()
                .isValidSpawn(BeepBlock::isNotFrame)
                .isRedstoneConductor(BeepBlock::isNotFrame)
                .isSuffocating(BeepBlock::isNotFrame)
                .isViewBlocking(BeepBlock::isNotFrame);
    }

    private static boolean isFrame(BlockState state) {
        return state.getValue(FRAME);
    }

    private static boolean isNotFrame(BlockState state, BlockGetter world, BlockPos pos) {
        return !isFrame(state);
    }

    private static boolean isNotFrame(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
        return !isFrame(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return isFrame(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return isFrame(state) ? Shapes.empty() : Shapes.block();
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        return isFrame(state) ? Shapes.empty() : Shapes.block();
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return isFrame(state) ? 1.0F : 0.2f;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return getStateAtTime(ctx.getLevel());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean notify) {
        this.refreshState(level, pos);
        this.scheduleTick(level, pos, state.getBlock());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide()) {
            this.refreshState(level, pos);
            this.scheduleTick(level, pos, state.getBlock());
        }
    }

    public void refreshState(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, getStateAtTime(level));
    }

    /**
     * Schedules the next state change, which happens whenever the world time reaches a multiple of the
     * cooldown -- the very times {@link #getStateAtTime} flips the state at.
     */
    public void scheduleTick(Level level, BlockPos pos, Block block) {
        if (level instanceof ServerLevel serverLevel) {
            int cooldown = serverLevel.getGameRules().get(SuperMarioGameRules.BEEP_BLOCK_COOLDOWN);
            if (cooldown > 0) {
                long worldTime = level.getGameTime();
                // floorMod, as the world time is past the cooldown within the first seconds of a world:
                // the remainder of a plain modulo would be negative from then on, which schedules the tick
                // in the past and has every beep block tick again on the very next tick, forever.
                int delay = Math.floorMod(-worldTime, cooldown);
                level.scheduleTick(pos, block, (delay == 0) ? cooldown : delay);
            }
        }
    }

    public BlockState getStateAtTime(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            int cooldown = serverLevel.getGameRules().get(SuperMarioGameRules.BEEP_BLOCK_COOLDOWN);
            if (cooldown > 0) {
                long worldTime = level.getGameTime();
                boolean frame = (int) ((worldTime + (this.offset ? cooldown : 0)) % (cooldown * 2)) < cooldown;
                return this.defaultBlockState().setValue(FRAME, frame);
            }
        }
        return defaultBlockState().setValue(FRAME, this.offset);
    }
}