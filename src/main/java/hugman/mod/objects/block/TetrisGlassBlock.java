package hugman.mod.objects.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TetrisGlassBlock extends FallingBlock
{
    public TetrisGlassBlock(Block.Properties builder)
    {
        super(builder);
    }
    
    @Override
	public BlockRenderLayer getRenderLayer()
    {
    	return BlockRenderLayer.TRANSLUCENT;
	}
    
    @OnlyIn(Dist.CLIENT)
    public float func_220080_a(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
       return true;
    }

    public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return false;
    }

    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
       return false;
    }

    public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type)
    {
       return false;
    }
}
