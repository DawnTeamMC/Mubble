package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.util.math.MathHelper;

public class OreBlock extends net.minecraft.block.OreBlock
{    
    public OreBlock(Settings properties)
    {
        super(properties);
    }
    
    @Override
    protected int getExperienceWhenMined(Random rand)
    {
    	if (this == MubbleBlocks.VANADIUM_ORE) return MathHelper.nextInt(rand, 4, 8);
    	return super.getExperienceWhenMined(rand);
    }
}
