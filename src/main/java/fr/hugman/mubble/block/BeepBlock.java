package fr.hugman.mubble.block;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BeepBlock extends Block {
    public static final BooleanProperty FRAME = BooleanProperty.of("frame");

    public BeepBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FRAME, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FRAME);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, state.getBlock(), (int) (80 - world.getTime() %80));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!world.isClient()) {
            this.trigger(world, pos, state);
            Mubble.LOGGER.info("SPIN ME!");
            world.scheduleBlockTick(pos, state.getBlock(), (int) (80 - world.getTime()%80));
        }
    }
    public void trigger(World world, BlockPos pos, BlockState state) {
        ////////////////world.setBlockState(pos, SuperMarioContent.BEEP_BLOCK_FRAME.getDefaultState(), Block.NOTIFY_LISTENERS);
        if (! state.get(FRAME)) {
            world.setBlockState(pos, state.with(FRAME, true));
        }
        else {
            world.setBlockState(pos, state.with(FRAME, false));
        }
    }
}

