package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;

public class BlockCreator {
	private final BlockEntry block;

	public BlockCreator(String name, Block block) {
		this.block = new BlockEntry(name, block);
		BlockCreatorHelper.addEntries(this.block);
	}

	public BlockCreator(String name, BlockTemplate template, FabricBlockSettings settings) {
		this.block = new BlockEntry(name, template, settings);
		BlockCreatorHelper.addEntries(this.block);
	}

	public BlockCreator(String name, BlockTemplate template, Block baseBlock) {
		this.block = new BlockEntry(name, template, baseBlock);
		BlockCreatorHelper.addEntries(this.block);
	}

	public Block getBlock() {
		return block.getBlock();
	}
}
