package fr.hugman.mubble.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class InkBlock extends MultifaceBlock {
    public InkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if(!world.getFluidState(pos).isEmpty()) {
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
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
