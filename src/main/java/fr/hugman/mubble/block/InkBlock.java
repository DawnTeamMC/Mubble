package fr.hugman.mubble.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class InkBlock extends MultifaceBlock {
    public InkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (!world.getFluidState(pos).isEmpty()) {
            return false;
        }

        boolean bl = false;
        for (Direction direction : DIRECTIONS) {
            if (!MultifaceBlock.hasDirection(state, direction)) continue;
            var otherPos = pos.offset(direction);
            var otherState = world.getBlockState(otherPos);
            if (!(Block.isFaceFullSquare(otherState.getSidesShape(world, otherPos), direction.getOpposite()) || Block.isFaceFullSquare(otherState.getCollisionShape(world, otherPos), direction.getOpposite()))) {
                return false;
            }
            bl = true;
        }
        return bl;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }
}
