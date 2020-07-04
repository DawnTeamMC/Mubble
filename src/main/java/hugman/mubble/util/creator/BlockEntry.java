package hugman.mubble.util.creator;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;

public class BlockEntry {
	private final String name;
	private final Block block;
	private final RenderLayer renderLayer;
	private final ItemGroup itemGroup;
	private final int burn;
	private final int spread;
	// Internal builders

	private BlockEntry(String name, Block block, RenderLayer renderLayer, ItemGroup itemGroup, int burn, int spread) {
		this.name = name;
		this.block = block;
		this.renderLayer = renderLayer;
		this.itemGroup = itemGroup;
		this.burn = burn;
		this.spread = spread;
	}

	private BlockEntry(String name, BlockTemplate template, FabricBlockSettings settings, Block baseBlock) {
		this(name, template, settings, BlockCreatorHelper.getFlammabilityBurn(baseBlock), BlockCreatorHelper.getFlammabilitySpread(baseBlock));
	}

	private BlockEntry(String name, BlockTemplate template, FabricBlockSettings settings, int burn, int spread) {
		this(name + template.getSuffix(), template.getBlock(settings), template.getRenderLayer(), template.getItemGroup(), burn, spread);
	}
	// Public builders

	public BlockEntry(String name, Block block) {
		this(name, block, RenderLayer.getSolid(), null, 0, 0);
	}

	public BlockEntry(String name, BlockTemplate template, FabricBlockSettings settings) {
		this(name + template.getSuffix(), template.getBlock(settings), template.getRenderLayer(), template.getItemGroup(), 0, 0);
	}

	public BlockEntry(String name, BlockTemplate template, Block baseBlock, MaterialColor color) {
		this(name, template, FabricBlockSettings.copyOf(baseBlock).materialColor(color), baseBlock);
	}

	public BlockEntry(String name, BlockTemplate template, Block baseBlock) {
		this(name, template, FabricBlockSettings.copyOf(baseBlock), baseBlock);
	}
	// Additional constructors

	public BlockEntry setRenderLayer(RenderLayer renderLayer) {
		return new BlockEntry(this.name, this.block, renderLayer, this.itemGroup, this.burn, this.spread);
	}

	public BlockEntry setItemGroup(ItemGroup itemGroup) {
		return new BlockEntry(this.name, this.block, this.renderLayer, itemGroup, this.burn, this.spread);
	}

	public BlockEntry setItemGroup(Block baseBlock) {
		return new BlockEntry(this.name, this.block, this.renderLayer, baseBlock.asItem().getGroup(), this.burn, this.spread);
	}

	public BlockEntry setFlammability(int burn, int spread) {
		return new BlockEntry(this.name, this.block, this.renderLayer, this.itemGroup, burn, spread);
	}

	public BlockEntry setFlammability(Block baseBlock) {
		return new BlockEntry(this.name, this.block, this.renderLayer, this.itemGroup, BlockCreatorHelper.getFlammabilityBurn(baseBlock), BlockCreatorHelper.getFlammabilitySpread(baseBlock));
	}
	// Getters

	public String getName() {
		return name;
	}

	public Block getBlock() {
		return block;
	}

	public RenderLayer getRenderLayer() {
		return renderLayer;
	}

	public ItemGroup getItemGroup() {
		return itemGroup;
	}

	public int getBurn() {
		return burn;
	}

	public int getSpread() {
		return spread;
	}
}
