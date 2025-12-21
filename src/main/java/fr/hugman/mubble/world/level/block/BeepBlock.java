package fr.hugman.mubble.world.level.block;

import fr.hugman.mubble.world.level.gamerules.MubbleGameRules;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
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
        return BlockBehaviour.Properties.of().mapColor(mapColor)
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

    public void scheduleTick(Level level, BlockPos pos, Block block) {
        if (level instanceof ServerLevel serverLevel) {
            int cooldown = serverLevel.getGameRules().get(MubbleGameRules.BEEP_BLOCK_COOLDOWN);
            if (cooldown > 0) {
                long worldTime = level.getGameTime();
                int delta = (int) (cooldown - worldTime);
                level.scheduleTick(pos, block, (delta == 0) ? cooldown : delta % cooldown);
            }
        }
    }

    public BlockState getStateAtTime(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            int cooldown = serverLevel.getGameRules().get(MubbleGameRules.BEEP_BLOCK_COOLDOWN);
            if (cooldown > 0) {
                long worldTime = level.getGameTime();
                boolean frame = (int) ((worldTime + (this.offset ? cooldown : 0)) % (cooldown * 2)) < cooldown;
                return this.defaultBlockState().setValue(FRAME, frame);
            }
        }
        return defaultBlockState().setValue(FRAME, this.offset);
    }
}