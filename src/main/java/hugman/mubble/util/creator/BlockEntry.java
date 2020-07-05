package hugman.mubble.util.creator;

import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntry {
	private Block block;

	private final String name;
	private final Block baseBlock;
	private RenderLayer renderLayer;
	private ItemGroup itemGroup;
	private int burn;
	private int spread;
	private boolean noItem;

	private BlockEntry(String name, Block baseBlock, RenderLayer renderLayer, ItemGroup itemGroup, int burn, int spread, boolean noItem) {
		this.name = name;
		this.baseBlock = baseBlock;
		this.renderLayer = renderLayer;
		this.itemGroup = itemGroup;
		this.burn = burn;
		this.spread = spread;
		this.noItem = noItem;
	}

	public BlockEntry register()
	{
		this.block = Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), baseBlock);
		BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
		if(!noItem) Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, new Item.Settings().group(itemGroup)));
		FlammableBlockRegistry.getDefaultInstance().add(block, burn, spread);
		return this;
	}

	public static class Builder {
		private final String name;
		private final Block baseBlock;
		private RenderLayer renderLayer;
		private ItemGroup itemGroup;
		private int burn;
		private int spread;
		private boolean noItem;

		public Builder(String name, Block block) {
			this.name = name;
			this.baseBlock = block;
			this.renderLayer = RenderLayer.getSolid();
			this.itemGroup = null;
			this.burn = 0;
			this.spread = 0;
			this.noItem = false;
		}

		public Builder(String name, BlockTemplate template, FabricBlockSettings settings) {
			this(name + template.getSuffix(), template.getBlock(settings));
		}

		public Builder setRenderLayer(RenderLayer renderLayer) {
			this.renderLayer = renderLayer;
			return this;
		}

		public Builder setItemGroup(ItemGroup itemGroup) {
			this.itemGroup = itemGroup;
			return this;
		}

		public Builder setFlammability(int burn, int spread) {
			this.burn = burn;
			this.spread = spread;
			return this;
		}

		public Builder noItem() {
			this.noItem = true;
			return this;
		}

		public Builder copy(BlockTemplate template) {
			this.renderLayer = template.getRenderLayer();
			this.itemGroup = template.getItemGroup();
			return this;
		}

		public Builder copy(Block baseBlock) {
			this.renderLayer = RenderLayers.getBlockLayer(baseBlock.getDefaultState());
			this.itemGroup = baseBlock.asItem().getGroup();
			this.burn = BlockCreatorHelper.getFlammabilityBurn(baseBlock);
			this.spread = BlockCreatorHelper.getFlammabilitySpread(baseBlock);
			return this;
		}

		public BlockEntry build() {
			return new BlockEntry(this.name, this.baseBlock, this.renderLayer, this.itemGroup, this.burn, this.spread, this.noItem).register();
		}
	}

	public Block getBlock() {
		return block;
	}
}
