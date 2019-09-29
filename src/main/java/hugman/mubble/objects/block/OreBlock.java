package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
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
    protected int getExperience(Random rand)
    {
    	if (this == MubbleBlocks.VANADIUM_ORE) return MathHelper.nextInt(rand, 4, 8);
    	return super.getExperience(rand);
    }
    
    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silkTouch)
    {
    	return silkTouch == 0 ? this.getExperience(RANDOM) : 0;
    }
}
