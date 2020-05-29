package hugman.mubble.objects.block;

import net.minecraft.block.Block;

public class StairsBlock extends net.minecraft.block.StairsBlock
{
	/* Extension for simplicity */
	public StairsBlock(Block baseBlock)
	{
		super(baseBlock.getDefaultState(), Settings.copy(baseBlock));
	}
}
