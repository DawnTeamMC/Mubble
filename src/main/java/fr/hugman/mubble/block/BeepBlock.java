package fr.hugman.mubble.block;

//Komerish is a cool dude. (sometimes (all the ((some)time))

import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BeepBlock extends Block {
    private static final int COOLDOWN = SharedConstants.TICKS_PER_SECOND * 4;
    public static final BooleanProperty FRAME = BooleanProperty.of("frame");

    public final int offset;

    public BeepBlock(Settings settings, int offset) {
        super(settings);
        this.offset = offset;
        this.setDefaultState(getDefaultState().with(FRAME, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FRAME);
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return state.get(FRAME);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(FRAME) ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(context.isHolding(this.asItem())) return VoxelShapes.fullCube();
        return state.get(FRAME) ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return state.get(FRAME) ? 1.0F : super.getAmbientOcclusionLightLevel(state, world, pos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getStateAtTime(ctx.getWorld().getTime());
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, state.getBlock(), this.getNextUpdateTickDelta(world.getTime()));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!world.isClient()) {
            long worldTime = world.getTime();
            world.setBlockState(pos, getStateAtTime(worldTime));
            world.scheduleBlockTick(pos, state.getBlock(), this.getNextUpdateTickDelta(worldTime));
        }
    }

    public int getNextUpdateTickDelta(long worldTime) {
        int delta = (int) (COOLDOWN - worldTime);
        if(delta == 0) return COOLDOWN;
        else return delta % COOLDOWN;
    }

    public BlockState getStateAtTime(long worldTime) {
        boolean frame = (int) ((worldTime + this.offset) % (COOLDOWN * 2)) < COOLDOWN;
        return this.getDefaultState().with(FRAME, frame);
    }
}