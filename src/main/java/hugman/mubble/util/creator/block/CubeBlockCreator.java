package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemGroup;

public class CubeBlockCreator {
	private final BlockEntry blockEntry;

	public CubeBlockCreator(String name, FabricBlockSettings settings) {
		this.blockEntry = new BlockEntry.Builder(name, BlockTemplate.CUBE, settings).build();
	}

	public CubeBlockCreator(String name, Block baseBlock) {
		this.blockEntry = new BlockEntry.Builder(name, BlockTemplate.CUBE, baseBlock).build();
	}

	public CubeBlockCreator(String name, Block baseBlock, ItemGroup itemGroup) {
		this.blockEntry = new BlockEntry.Builder(name, BlockTemplate.CUBE, baseBlock).setItemGroup(itemGroup).build();
	}

	public CubeBlockCreator(String name, Block baseBlock, MaterialColor color) {
		this.blockEntry = new BlockEntry.Builder(name, BlockTemplate.CUBE, baseBlock, color).build();
	}

	public Block getBlock() {
		return blockEntry.getBlock();
	}
}
