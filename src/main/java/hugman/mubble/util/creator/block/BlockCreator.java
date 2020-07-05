package hugman.mubble.util.creator.block;

import hugman.mubble.util.creator.BlockEntry;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;

public class BlockCreator {
	private final BlockEntry blockEntry;

	public BlockCreator(String name, Block block) {
		this.blockEntry = new BlockEntry.Builder(name, block).build();
	}

	public BlockCreator(String name, Block block, ItemGroup itemGroup) {
		this.blockEntry = new BlockEntry.Builder(name, block).setItemGroup(itemGroup).build();
	}

	public BlockCreator(String name, Block block, ItemGroup itemGroup, RenderLayer renderLayer) {
		this.blockEntry = new BlockEntry.Builder(name, block).setItemGroup(itemGroup).build();
	}

	public BlockCreator(String name, Block block, ItemGroup itemGroup, int burn, int spread) {
		this.blockEntry = new BlockEntry.Builder(name, block).setItemGroup(itemGroup).setFlammability(burn, spread).build();
	}

	public BlockCreator(String name, BlockTemplate template, FabricBlockSettings settings) {
		this.blockEntry = new BlockEntry.Builder(name, template, settings).build();
	}

	public BlockCreator(String name, BlockTemplate template, Block baseBlock) {
		this.blockEntry = new BlockEntry.Builder(name, template, FabricBlockSettings.copyOf(baseBlock)).copy(baseBlock).copy(template).build();
	}

	public BlockCreator(String name, BlockTemplate template, Block baseBlock, MaterialColor color) {
		this.blockEntry = new BlockEntry.Builder(name, template, FabricBlockSettings.copyOf(baseBlock).materialColor(color)).copy(baseBlock).copy(template).build();
	}

	public BlockCreator(String name, BlockTemplate template, Block baseBlock, ItemGroup itemGroup) {
		this.blockEntry = new BlockEntry.Builder(name, template, FabricBlockSettings.copyOf(baseBlock)).copy(baseBlock).copy(template).setItemGroup(itemGroup).build();
	}

	public Block getBlock() {
		return blockEntry.getBlock();
	}

	public BlockEntry getBlockEntry() {
		return blockEntry;
	}
}
