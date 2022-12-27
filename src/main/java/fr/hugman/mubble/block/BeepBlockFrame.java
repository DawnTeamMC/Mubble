package fr.hugman.mubble.block;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.registry.SuperMarioContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BeepBlockFrame extends Block {
    public BeepBlockFrame(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, state.getBlock(), (int) (80 - world.getTime() %81));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient()) {
            this.trigger(world, pos);
            Mubble.LOGGER.info("JUDGEMENT!");
            world.scheduleBlockTick(pos, state.getBlock(), (int) (80 - world.getTime() %81));
        }
    }

    public void trigger(World world, BlockPos pos) {
        world.setBlockState(pos, SuperMarioContent.RED_BEEP_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
}