package hugman.mubble.util.entry.pack;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.util.entry.BlockEntry;
import hugman.mubble.util.entry.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class LeavesEntryPack {
	private final Block leaves;
	private final Block leafPile;

	/**
	 * Creates an entry pack containing a leaves entry and its leaf pile variant.
	 * @param suffix The suffix of the leaves.
	 */
	public LeavesEntryPack(String suffix) {
		this.leaves = new BlockEntry.Builder(suffix, BlockTemplate.LEAVES, MubbleBlocks.Settings.LEAVES).copy(Blocks.OAK_LEAVES).build();
		this.leafPile = new BlockEntry.Builder(suffix, BlockTemplate.PILE, MubbleBlocks.Settings.LEAF_PILE).copy(Blocks.OAK_LEAVES).build();
	}

	public Block getLeaves() {
		return leaves;
	}

	public Block getLeafPile() {
		return leafPile;
	}
}
