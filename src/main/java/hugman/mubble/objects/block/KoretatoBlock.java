package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class KoretatoBlock extends DirectionalBlock
{
	public static final BooleanProperty PRINCESS = MubbleBlockStateProperties.PRINCESS;
	
    public KoretatoBlock()
    {
        super(Properties.create(Material.ORGANIC, MaterialColor.YELLOW_TERRACOTTA).hardnessAndResistance(0.4f, 2f).sound(SoundType.SNOW));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(PRINCESS, false));
    }
    
    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
    	return state.with(FACING, direction.rotate(state.get(FACING)));
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
    	builder.add(PRINCESS, FACING);
	}
}
