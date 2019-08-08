package hugman.mubble.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.IShearable;

public class LeavesCarpetBlock extends BushBlock implements IShearable
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private final Block base;
    
    public LeavesCarpetBlock(Block base_block)
    {
        super(Properties.create(Material.PLANTS).hardnessAndResistance(0.1F).sound(SoundType.PLANT).doesNotBlockMovement());
        this.base = base_block;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
    	return SHAPE;
    }
    
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
    	return base.getOpacity(base.getDefaultState(), worldIn, pos);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer()
    {
    	return base.getRenderLayer();
    }
    
    @Override
    public boolean isSolid(BlockState state)
    {
    	return false;
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockPos blockpos = pos.offset(Direction.DOWN);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return Block.hasSolidSide(blockstate, worldIn, blockpos, Direction.UP);
    }
}
