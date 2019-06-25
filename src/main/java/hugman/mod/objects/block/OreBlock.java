package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class OreBlock extends net.minecraft.block.OreBlock
{    
    public OreBlock(Properties properties)
    {
        super(properties);
    }
    
    @Override
    protected int func_220281_a(Random p_220281_1_)
    {
    	if (this == MubbleBlocks.VANADIUM_ORE) return MathHelper.nextInt(p_220281_1_, 4, 8);
    	return super.func_220281_a(p_220281_1_);
    }
    
    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch)
    {
    	return silktouch == 0 ? this.func_220281_a(RANDOM) : 0;
    }
}
