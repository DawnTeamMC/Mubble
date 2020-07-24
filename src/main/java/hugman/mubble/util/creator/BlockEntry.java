package hugman.mubble.util.creator;

import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BlockEntry {
	private Block block;

	private final String name;
	private final Block baseBlock;
	private final RenderLayer renderLayer;
	private final ItemGroup itemGroup;
	private final int flammabilityBurn;
	private final int flammabilitySpread;
	private final int cookTime;
	private final boolean noItem;

	private BlockEntry(String name, Block baseBlock, RenderLayer renderLayer, ItemGroup itemGroup, int flammabilityBurn, int flammabilitySpread, int cookTime, boolean noItem) {
		this.name = name;
		this.baseBlock = baseBlock;
		this.renderLayer = renderLayer;
		this.itemGroup = itemGroup;
		this.flammabilityBurn = flammabilityBurn;
		this.flammabilitySpread = flammabilitySpread;
		this.cookTime = cookTime;
		this.noItem = noItem;
	}

	private BlockEntry register() {
		this.block = Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), baseBlock);
		BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
		if(flammabilityBurn != 0 && flammabilitySpread != 0) FlammableBlockRegistry.getDefaultInstance().add(block, flammabilityBurn, flammabilitySpread);
		if(!noItem) {
			Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, new Item.Settings().group(itemGroup)));
			if(cookTime != 0) FuelRegistry.INSTANCE.add(block, cookTime);
		}
		return this;
	}

	public static class Builder {
		private final String name;
		private final Block baseBlock;
		private RenderLayer renderLayer;
		private ItemGroup itemGroup;
		private int flammabilityBurn;
		private int flammabilitySpread;
		private int cookTime;
		private boolean noItem;

		public Builder(String name, Block block) {
			this.name = name;
			this.baseBlock = block;
			this.renderLayer = RenderLayer.getSolid();
			this.itemGroup = null;
			this.flammabilityBurn = 0;
			this.flammabilitySpread = 0;
			this.cookTime = 0;
			this.noItem = false;
		}

		public Builder(String name, BlockTemplate template, FabricBlockSettings settings) {
			this(name + template.getSuffix(), template.getBlock(settings));
		}

		public Builder(String name, BlockTemplate template, Block baseBlock) {
			this(name, template, FabricBlockSettings.copyOf(baseBlock));
			copy(template, baseBlock);
		}

		public Builder(String name, BlockTemplate template, Block baseBlock, MaterialColor color) {
			this(name, template, FabricBlockSettings.copyOf(baseBlock).materialColor(color));
			copy(template, baseBlock);
		}

		public Builder setRenderLayer(RenderLayer renderLayer) {
			this.renderLayer = renderLayer;
			return this;
		}

		public Builder setItemGroup(ItemGroup itemGroup) {
			this.itemGroup = itemGroup;
			return this;
		}

		public Builder setFlammability(int flammabilityBurn, int flammabilitySpread) {
			this.flammabilityBurn = flammabilityBurn;
			this.flammabilitySpread = flammabilitySpread;
			return this;
		}

		public Builder setCookTime(int cookTime) {
			this.cookTime = cookTime;
			return this;
		}

		/**
		 * Removes the item form of the block.
		 */
		public Builder noItem() {
			this.noItem = true;
			return this;
		}

		/**
		 * Copies some block properties from a template. (render layer, item group)
		 * @param template The template to copy properties from.
		 */
		public Builder copy(BlockTemplate template) {
			setRenderLayer(template.getRenderLayer());
			setItemGroup(template.getItemGroup());
			return this;
		}

		/**
		 * Copies some block properties from a block. (render layer, item group, flammability, cook time)
		 * @param baseBlock The block to copy properties from.
		 */
		public Builder copy(Block baseBlock) {
			setRenderLayer(RenderLayers.getBlockLayer(baseBlock.getDefaultState()));
			setItemGroup(baseBlock.asItem().getGroup());
			setFlammability(CreatorHelper.getFlammabilityBurn(baseBlock), CreatorHelper.getFlammabilitySpread(baseBlock));
			setCookTime(CreatorHelper.getCookTime(baseBlock));
			return this;
		}

		/**
		 * Copies some block properties from a template and a block.
		 * <p>Block -> (flammability, cook time)
		 * <p>Template -> (render layer, item group)
		 *
		 * @param template The template to copy properties from.
		 * @param baseBlock The block to copy properties from.
		 */
		public Builder copy(BlockTemplate template, Block baseBlock) {
			copy(baseBlock);
			copy(template);
			return this;
		}

		/**
		 * Builds the entry and registers the block with all its settings.
		 */
		public BlockEntry build() {
			return new BlockEntry(this.name, this.baseBlock, this.renderLayer, this.itemGroup, this.flammabilityBurn, this.flammabilitySpread, this.cookTime, this.noItem).register();
		}
	}

	public Block getBlock() {
		return block;
	}
}
