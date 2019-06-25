package hugman.mod.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class DirectionalBlock extends net.minecraft.block.DirectionalBlock
{	
	/* Extension for internal publicity
	 * + Missing features */
    public DirectionalBlock(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
    	return state.with(FACING, rot.rotate(state.get(FACING)));
	}
    
    @Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
    	return state.mirror(mirrorIn);
	}
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
    	builder.add(FACING);
	}
}
