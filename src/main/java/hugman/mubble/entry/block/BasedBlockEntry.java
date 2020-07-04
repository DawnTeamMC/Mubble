package hugman.mubble.entry.block;

import hugman.mubble.util.creator.BlockCreatorUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;

public class BasedBlockEntry {
	private final Block block;

	public BasedBlockEntry(String name, Block baseBlock, MaterialColor color) {
		this.block = BlockCreatorUtil.registerBlock(new Block(FabricBlockSettings.copyOf(baseBlock).materialColor(color)), name, baseBlock);
	}

	public Block getBlock() {
		return block;
	}
}
