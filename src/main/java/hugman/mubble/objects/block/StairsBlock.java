package hugman.mubble.objects.block;

import net.minecraft.block.Block;

public class StairsBlock extends net.minecraft.block.StairsBlock
{
	/* Extension for internal publicity */
    public StairsBlock(Block base_block)
    {
        super(base_block.getDefaultState(), Properties.from(base_block));
    }
}
