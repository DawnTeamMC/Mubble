package hugman.mubble.util.entry.block;

import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntry {
	protected Block registerBlock(Block block, String name) {
		return Registry.register(Registry.BLOCK, new Identifier(Mubble.MOD_ID, name), block);
	}

	protected Block createPottedPlant(Block baseBlock) {
		Block newBlock = registerBlock(new FlowerPotBlock(baseBlock, FabricBlockSettings.of(Material.SUPPORTED).breakInstantly().nonOpaque().lightLevel(baseBlock.getDefaultState().getLuminance())), "potted_" + Registry.BLOCK.getId(baseBlock).getPath());
		BlockRenderLayerMap.INSTANCE.putBlock(newBlock, RenderLayer.getCutout());
		return newBlock;
	}

	protected Item registerBlockItem(Block block, ItemGroup itemGroup) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, new Item.Settings().group(itemGroup)));
	}

	protected Item registerBlockItem(Block block, Block baseBlock) {
		return registerBlockItem(block, baseBlock.asItem().getGroup());
	}

	protected void copyFlammability(Block block, Block baseBlock) {
		FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
		flammableBlockRegistry.add(block, flammableBlockRegistry.get(baseBlock));
	}
}
