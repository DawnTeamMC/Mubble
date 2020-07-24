package hugman.mubble.util.entry;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.util.DataWriter;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntry extends Entry<Block> {
	protected final Block baseBlock;
	protected final RenderLayer renderLayer;
	protected final ItemGroup itemGroup;
	protected final int flammabilityBurn;
	protected final int flammabilitySpread;
	protected final int cookTime;
	protected final boolean noItem;

	private BlockEntry(String name, Block baseBlock, RenderLayer renderLayer, ItemGroup itemGroup, int flammabilityBurn, int flammabilitySpread, int cookTime, boolean noItem) {
		super(name);
		this.baseBlock = baseBlock;
		this.renderLayer = renderLayer;
		this.itemGroup = itemGroup;
		this.flammabilityBurn = flammabilityBurn;
		this.flammabilitySpread = flammabilitySpread;
		this.cookTime = cookTime;
		this.noItem = noItem;
	}

	@Override
	protected Block register() {
		value = Registry.register(Registry.BLOCK, Mubble.id(name), baseBlock);
		BlockRenderLayerMap.INSTANCE.putBlock(value, renderLayer);
		FlammableBlockRegistry.getDefaultInstance().add(value, flammabilityBurn, flammabilitySpread);
		if(!noItem) {
			Item item = Registry.register(Registry.ITEM, Registry.BLOCK.getId(value), new BlockItem(value, new Item.Settings().group(itemGroup)));
			((BlockItem)item).appendBlocks(Item.BLOCK_ITEMS, item);
			DataWriter.entryNamesData.block_items.add(Mubble.id(name).toString());
			DataWriter.entryCountsData.block_items++;
			FuelRegistry.INSTANCE.add(value, cookTime);
		}
		DataWriter.entryNamesData.blocks.add(Mubble.id(name).toString());
		DataWriter.entryCountsData.blocks++;
		DataWriter.save();
		return value;
	}

	public static class Builder {
		protected String name;
		protected Block baseBlock;
		protected RenderLayer renderLayer;
		protected ItemGroup itemGroup;
		protected int flammabilityBurn;
		protected int flammabilitySpread;
		protected int cookTime;
		protected boolean noItem;

		/**
		 * Creates a simple block with an item but no item group, flammability or cook time and is rendered has a solid block.
		 *
		 * @param name  The name of the block.
		 * @param block The block itself.
		 */
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

		/**
		 * Creates a block copying some properties from a template and a block.
		 * <p>Template -> (block class, render layer, item group)
		 * <p>Block -> (block settings, flammability, cook time)
		 *
		 * @param suffix     The suffix of the block.
		 * @param template The template to copy properties from.
		 */
		public Builder(String suffix, BlockTemplate template, FabricBlockSettings settings) {
			this(suffix + template.getSuffix(), template.getBlock(settings));
		}

		/**
		 * Creates a block copying some properties from a template and a block.
		 * <p>Template -> (block class, render layer, item group)
		 * <p>Block -> (block settings, flammability, cook time)</p>
		 *
		 * @param suffix      The suffix of the block.
		 * @param template  The template to copy properties from.
		 * @param baseBlock The block to copy properties from.
		 */
		public Builder(String suffix, BlockTemplate template, Block baseBlock) {
			this(suffix, template, FabricBlockSettings.copyOf(baseBlock));
			copy(template, baseBlock);
		}

		/**
		 * Creates a block copying some properties from a template and a block. This constructor also allows for a custom material color.
		 * <p>Template -> (block class, render layer, item group)
		 * <p>Block -> (block settings, flammability, cook time)
		 *
		 * @param suffix      The suffix of the block.
		 * @param template  The template to copy properties from.
		 * @param baseBlock The block to copy properties from.
		 * @param color     The material color of the block.
		 */
		public Builder(String suffix, BlockTemplate template, Block baseBlock, MaterialColor color) {
			this(suffix, template, FabricBlockSettings.copyOf(baseBlock).materialColor(color));
			copy(template, baseBlock);
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public String getName() {
			return name;
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
		 * Copies some properties from a template. (render layer, item group)
		 *
		 * @param template The template to copy properties from.
		 */
		public Builder copy(BlockTemplate template) {
			setRenderLayer(template.getRenderLayer());
			setItemGroup(template.getItemGroup());
			return this;
		}

		/**
		 * Copies some properties from a block. (render layer, item group, flammability, cook time)
		 *
		 * @param baseBlock The block to copy properties from.
		 */
		public Builder copy(Block baseBlock) {
			setRenderLayer(RenderLayers.getBlockLayer(baseBlock.getDefaultState()));
			setItemGroup(baseBlock.asItem().getGroup());
			setFlammability(EntryHelper.getFlammabilityBurn(baseBlock), EntryHelper.getFlammabilitySpread(baseBlock));
			//setCookTime(CreatorHelper.getCookTime(baseBlock));
			return this;
		}

		/**
		 * Copies some block properties from a template and a block.
		 * <p>Block -> (flammability, cook time)</p>
		 * <p>Template -> (render layer, item group)</p>
		 *
		 * @param template  The template to copy properties from.
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
		public Block build() {
			return new BlockEntry(this.name, this.baseBlock, this.renderLayer, this.itemGroup, this.flammabilityBurn, this.flammabilitySpread, this.cookTime, this.noItem).register();
		}
	}
}
