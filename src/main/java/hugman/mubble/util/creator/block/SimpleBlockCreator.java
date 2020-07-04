package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockCreatorHelper;
import hugman.mubble.util.creator.BlockEntry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;

public class SimpleBlockCreator {
	private final BlockEntry block;

	public SimpleBlockCreator(String name, FabricBlockSettings settings) {
		this.block = new BlockEntry(name, new Block(settings));
		BlockCreatorHelper.addEntries(this.block);
	}

	private SimpleBlockCreator(String name, Block baseBlock, FabricBlockSettings settings) {
		this.block = new BlockEntry(name, new Block(settings)).setItemGroup(baseBlock).setFlammability(baseBlock);
		BlockCreatorHelper.addEntries(this.block);
	}

	public SimpleBlockCreator(String name, Block baseBlock) {
		this(name, baseBlock, FabricBlockSettings.copyOf(baseBlock));
	}

	public SimpleBlockCreator(String name, Block baseBlock, MaterialColor color) {
		this(name, baseBlock, FabricBlockSettings.copyOf(baseBlock).materialColor(color));
	}

	public Block getBlock() {
		return block.getBlock();
	}
}
