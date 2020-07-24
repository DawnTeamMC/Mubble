package hugman.mubble.util.creator;

import com.google.common.base.Preconditions;
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
	protected Block block;

	protected final String name;
	protected final Block baseBlock;
	protected final RenderLayer renderLayer;
	protected final ItemGroup itemGroup;
	protected final int flammabilityBurn;
	protected final int flammabilitySpread;
	protected final int cookTime;
	protected final boolean noItem;

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
		protected String name;
		protected Block baseBlock;
		protected RenderLayer renderLayer;
		protected ItemGroup itemGroup;
		protected int flammabilityBurn;
		protected int flammabilitySpread;
		protected int cookTime;
		protected boolean noItem;

		public Builder() {
			this.name = null;
			this.baseBlock = null;
			this.renderLayer = RenderLayer.getSolid();
			this.itemGroup = null;
			this.flammabilityBurn = 0;
			this.flammabilitySpread = 0;
			this.cookTime = 0;
			this.noItem = false;
		}

		public Builder(String name, Block block) {
			this();
			this.name = name;
			this.baseBlock = block;
		}

		/**
		 * Creates a block copying some properties from a template and a block.
		 * <p>Template -> (block class, render layer, item group)
		 * <p>Block -> (block settings, flammability, cook time)
		 *
		 * @param name The name of the block.
		 * @param template The template to copy properties from.
		 */
		public Builder(String name, BlockTemplate template, FabricBlockSettings settings) {
			this(name + template.getSuffix(), template.getBlock(settings));
		}

		/**
		 * Creates a block copying some properties from a template and a block.
		 * <p>Template -> (block class, render layer, item group)
		 * <p>Block -> (block settings, flammability, cook time)</p>
		 *
		 * @param name The name of the block.
		 * @param template The template to copy properties from.
		 * @param baseBlock The block to copy properties from.
		 */
		public Builder(String name, BlockTemplate template, Block baseBlock) {
			this(name, template, FabricBlockSettings.copyOf(baseBlock));
			copy(template, baseBlock);
		}

		/**
		 * Creates a block copying some properties from a template and a block. This constructor also allows for a custom material color.
		 * <p>Template -> (block class, render layer, item group)
		 * <p>Block -> (block settings, flammability, cook time)
		 *
		 * @param name The name of the block.
		 * @param template The template to copy properties from.
		 * @param baseBlock The block to copy properties from.
		 * @param color The material color of the block.
		 */
		public Builder(String name, BlockTemplate template, Block baseBlock, MaterialColor color) {
			this(name, template, FabricBlockSettings.copyOf(baseBlock).materialColor(color));
			copy(template, baseBlock);
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setBaseBlock(Block baseBlock) {
			this.baseBlock = baseBlock;
			return this;
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
		 * @param template The template to copy properties from.
		 */
		public Builder copy(BlockTemplate template) {
			setRenderLayer(template.getRenderLayer());
			setItemGroup(template.getItemGroup());
			return this;
		}

		/**
		 * Copies some properties from a block. (render layer, item group, flammability, cook time)
		 * @param baseBlock The block to copy properties from.
		 */
		public Builder copy(Block baseBlock) {
			setRenderLayer(RenderLayers.getBlockLayer(baseBlock.getDefaultState()));
			setItemGroup(baseBlock.asItem().getGroup());
			setFlammability(CreatorHelper.getFlammabilityBurn(baseBlock), CreatorHelper.getFlammabilitySpread(baseBlock));
			//setCookTime(CreatorHelper.getCookTime(baseBlock));
			return this;
		}

		/**
		 * Copies some block properties from a template and a block.
		 * <p>Block -> (flammability, cook time)</p>
		 * <p>Template -> (render layer, item group)</p>
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
			Preconditions.checkArgument(name != null, "Name is null");
			Preconditions.checkArgument(baseBlock != null, "Base block is null");
			return new BlockEntry(this.name, this.baseBlock, this.renderLayer, this.itemGroup, this.flammabilityBurn, this.flammabilitySpread, this.cookTime, this.noItem).register();
		}
	}

	public Block getBlock() {
		return block;
	}

	public String getName() {
		return name;
	}
}
