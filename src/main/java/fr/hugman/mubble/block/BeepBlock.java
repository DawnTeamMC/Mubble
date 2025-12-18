package fr.hugman.mubble.block;

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
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        this.refreshState(world, pos);
        this.scheduleTick(world, pos, state.getBlock());
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!world.isClientSide()) {
            this.refreshState(world, pos);
            this.scheduleTick(world, pos, state.getBlock());
        }
    }

    public void refreshState(Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, getStateAtTime(world));
    }

    public void scheduleTick(Level world, BlockPos pos, Block block) {
        if (world instanceof ServerLevel serverWorld) {
            int cooldown = serverWorld.getGameRules().get(MubbleGameRules.BEEP_BLOCK_COOLDOWN);
            if (cooldown > 0) {
                long worldTime = world.getGameTime();
                int delta = (int) (cooldown - worldTime);
                world.scheduleTick(pos, block, (delta == 0) ? cooldown : delta % cooldown);
            }
        }
    }

    public BlockState getStateAtTime(Level world) {
        if (world instanceof ServerLevel serverWorld) {
            int cooldown = serverWorld.getGameRules().get(MubbleGameRules.BEEP_BLOCK_COOLDOWN);
            if (cooldown > 0) {
                long worldTime = world.getGameTime();
                boolean frame = (int) ((worldTime + (this.offset ? cooldown : 0)) % (cooldown * 2)) < cooldown;
                return this.defaultBlockState().setValue(FRAME, frame);
            }
        }
        return defaultBlockState().setValue(FRAME, this.offset);
    }
}