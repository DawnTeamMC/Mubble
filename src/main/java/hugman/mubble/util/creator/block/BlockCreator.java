package hugman.mubble.util.creator.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import net.minecraft.block.Block;

public class BlockCreator {
	private final BlockEntry block;

	public BlockCreator(String name, Block block) {
		this.block = new BlockEntry(name, block);
		BlockCreatorHelper.addEntries(this.block);
	}

	public Block getBlock() {
		return block.getBlock();
	}
}
