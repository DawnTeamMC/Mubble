package hugman.mod.objects.block;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends net.minecraft.block.BlockOre
{    
    public BlockOre(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
    {
    	if(this == MubbleBlocks.VANADIUM_ORE) return MubbleItems.VANADIUM;
    	return super.getItemDropped(state, worldIn, pos, fortune);
    }
    
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune)
    {
       World world = reader instanceof World ? (World)reader : null;
       if (world == null || this.getItemDropped(state, world, pos, fortune) != this)
       {
          int i = 0;
          if (this == MubbleBlocks.VANADIUM_ORE) i = MathHelper.nextInt(RANDOM, 4, 8);
          return i;
       }
   	return super.getExpDrop(state, reader, pos, fortune);
    }
}
