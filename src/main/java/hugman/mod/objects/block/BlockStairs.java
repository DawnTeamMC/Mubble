package hugman.mod.objects.block;

import net.minecraft.block.Block;

public class BlockStairs extends net.minecraft.block.BlockStairs
{
	/* Extension for internal publicity */
    public BlockStairs(Block base_block)
    {
        super(base_block.getDefaultState(), Properties.from(base_block));
    }
}
