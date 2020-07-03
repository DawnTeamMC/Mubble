package hugman.mubble.util.entry.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;

public class SimpleBlockEntry extends BlockEntry {
	private final Block block;

	public SimpleBlockEntry(String name, Block baseBlock, MaterialColor color) {
		this.block = registerBlock(new Block(FabricBlockSettings.copyOf(baseBlock).materialColor(color)), name);
		registerBlockItem(this.getBlock(), baseBlock);
		copyFlammability(this.getBlock(), baseBlock);
	}

	public Block getBlock() {
		return block;
	}
}
