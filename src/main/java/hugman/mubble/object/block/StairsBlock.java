package hugman.mubble.object.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;

public class StairsBlock extends net.minecraft.block.StairsBlock {
	/* Extension for simplicity */
	public StairsBlock(Block baseBlock) {
		super(baseBlock.getDefaultState(), FabricBlockSettings.copy(baseBlock));
	}
}
