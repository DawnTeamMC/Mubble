package hugman.mubble.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

public class PileBlock extends PlantBlock
{
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    
    public PileBlock(Settings builder)
    {
        super(builder);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public int getOpacity(BlockState state, BlockView worldIn, BlockPos pos)
    {
    	return 1;
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return !stateIn.canPlaceAt(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    
    @Override
    public boolean canPlantOnTop(BlockState state, BlockView worldIn, BlockPos pos)
    {
        BlockPos blockpos = pos.offset(Direction.DOWN);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return Block.isSideSolidFullSquare(blockstate, worldIn, blockpos, Direction.UP);
    }
}
