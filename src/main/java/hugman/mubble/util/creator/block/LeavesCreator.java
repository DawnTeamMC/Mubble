package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class LeavesCreator {
	private final BlockEntry leavesEntry;
	private final BlockEntry leafPileEntry;

	public LeavesCreator(String name) {
		this.leavesEntry = new BlockEntry.Builder(name, BlockTemplate.LEAVES, BlockTemplate.LEAVES_SETTINGS).copy(Blocks.OAK_LEAVES).build();
		this.leafPileEntry = new BlockEntry.Builder(name, BlockTemplate.PILE, BlockTemplate.LEAF_PILE_SETTINGS).copy(Blocks.OAK_LEAVES).build();
	}

	public Block getLeaves() {
		return leavesEntry.getBlock();
	}

	public Block getLeafPile() {
		return leafPileEntry.getBlock();
	}
}
