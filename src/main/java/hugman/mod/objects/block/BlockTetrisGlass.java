package hugman.mod.objects.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockTetrisGlass extends BlockFalling
{
    public BlockTetrisGlass()
    {
        super("tetris_glass", Properties.from(Blocks.GLASS));
    }
    
    @Override
    public boolean propagatesSkylightDown(IBlockState state, IBlockReader reader, BlockPos pos)
    {
    	return true;
	}

    @Override
	public int quantityDropped(IBlockState state, Random random)
    {
    	return 0;
	}
    
    @Override
	public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
	}
    
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
     
	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
}
