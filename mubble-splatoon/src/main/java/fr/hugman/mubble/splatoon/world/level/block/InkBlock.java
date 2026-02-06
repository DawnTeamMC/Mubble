package fr.hugman.mubble.splatoon.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class InkBlock extends MultifaceBlock {
    public InkBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if(!level.getFluidState(pos).isEmpty()) {
            return false;
        }

        boolean bl = false;
        for(Direction direction : DIRECTIONS) {
            if(!MultifaceBlock.hasFace(state, direction)) continue;
            var otherPos = pos.relative(direction);
            var otherState = level.getBlockState(otherPos);
            if(!(Block.isFaceFull(otherState.getBlockSupportShape(level, otherPos), direction.getOpposite()) || Block.isFaceFull(otherState.getCollisionShape(level, otherPos), direction.getOpposite()))) {
                return false;
            }
            bl = true;
        }
        return bl;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if(!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }
}
