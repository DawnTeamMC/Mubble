package hugman.mod.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class BlockDirectional extends net.minecraft.block.BlockDirectional
{	
	/* Extension for internal publicity
	 * + Missing features */
    public BlockDirectional(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.UP));
    }
    
    @Override
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
    	return state.with(FACING, rot.rotate(state.get(FACING)));
	}
    
    @Override
	public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
    	return state.mirror(mirrorIn);
	}
    
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
    	builder.add(FACING);
	}
}
