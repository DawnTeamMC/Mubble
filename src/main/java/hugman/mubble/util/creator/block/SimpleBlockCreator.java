package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemGroup;

public class SimpleBlockCreator {
	private final BlockEntry blockEntry;

	public SimpleBlockCreator(String name, FabricBlockSettings settings) {
		this.blockEntry = new BlockCreator(name, BlockTemplate.CUBE, settings).getBlockEntry();
	}

	public SimpleBlockCreator(String name, Block baseBlock) {
		this.blockEntry = new BlockCreator(name, BlockTemplate.CUBE, baseBlock).getBlockEntry();
	}

	public SimpleBlockCreator(String name, Block baseBlock, ItemGroup itemGroup) {
		this.blockEntry = new BlockCreator(name, BlockTemplate.CUBE, baseBlock, itemGroup).getBlockEntry();
	}

	public SimpleBlockCreator(String name, Block baseBlock, MaterialColor color) {
		this.blockEntry = new BlockCreator(name, BlockTemplate.CUBE, baseBlock, color).getBlockEntry();
	}

	public Block getBlock() {
		return blockEntry.getBlock();
	}
}
