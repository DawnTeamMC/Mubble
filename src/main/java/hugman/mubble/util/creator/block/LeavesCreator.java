package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;

public class LeavesCreator {
	private final BlockEntry leavesEntry;
	private final BlockEntry leafPileEntry;

	public LeavesCreator(String name) {
		this.leavesEntry = new BlockEntry.Builder(name, BlockTemplate.LEAVES, BlockTemplate.leavesSettings).copy(Blocks.OAK_LEAVES).setItemGroup(ItemGroup.DECORATIONS).build();
		this.leafPileEntry = new BlockEntry.Builder(name, BlockTemplate.LEAF_PILE, BlockTemplate.leafPileSettings).copy(Blocks.OAK_LEAVES).setItemGroup(ItemGroup.DECORATIONS).build();
	}

	public Block getLeaves() {
		return leavesEntry.getBlock();
	}

	public Block getLeafPile() {
		return leafPileEntry.getBlock();
	}
}
