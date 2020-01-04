package hugman.mubble;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleScreens;
import hugman.mubble.init.data.MubbleColorMaps;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class MubbleClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		MubbleScreens.init();
		
		MubbleColorMaps.registerBlockColors();
		MubbleColorMaps.registerItemColors();
		registerBlockRenderLayers();
	}
	
	private void registerBlockRenderLayers()
	{
		BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.PERMAFROST_PORTAL, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.RED_SHINY_GARLAND,
				MubbleBlocks.SILVER_SHINY_GARLAND, MubbleBlocks.GOLD_SHINY_GARLAND
			);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.BLUE_PRESENT,
				MubbleBlocks.GREEN_PRESENT, MubbleBlocks.YELLOW_PRESENT, MubbleBlocks.RED_PRESENT
			);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), MubbleBlocks.WHITE_CLOUD_BLOCK,
				MubbleBlocks.LIGHT_GRAY_CLOUD_BLOCK, MubbleBlocks.GRAY_CLOUD_BLOCK, MubbleBlocks.BLACK_CLOUD_BLOCK,
				MubbleBlocks.BROWN_CLOUD_BLOCK, MubbleBlocks.RED_CLOUD_BLOCK, MubbleBlocks.ORANGE_CLOUD_BLOCK,
				MubbleBlocks.YELLOW_CLOUD_BLOCK, MubbleBlocks.LIME_CLOUD_BLOCK, MubbleBlocks.GREEN_CLOUD_BLOCK,
				MubbleBlocks.CYAN_CLOUD_BLOCK, MubbleBlocks.LIGHT_BLUE_CLOUD_BLOCK, MubbleBlocks.BLUE_CLOUD_BLOCK,
				MubbleBlocks.PURPLE_CLOUD_BLOCK, MubbleBlocks.MAGENTA_CLOUD_BLOCK, MubbleBlocks.PINK_CLOUD_BLOCK
			);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), MubbleBlocks.WHITE_BALLOON,
				MubbleBlocks.LIGHT_GRAY_BALLOON, MubbleBlocks.GRAY_BALLOON, MubbleBlocks.BLACK_BALLOON,
				MubbleBlocks.BROWN_BALLOON, MubbleBlocks.RED_BALLOON, MubbleBlocks.ORANGE_BALLOON,
				MubbleBlocks.YELLOW_BALLOON, MubbleBlocks.LIME_BALLOON, MubbleBlocks.GREEN_BALLOON,
				MubbleBlocks.CYAN_BALLOON, MubbleBlocks.LIGHT_BLUE_BALLOON, MubbleBlocks.BLUE_BALLOON,
				MubbleBlocks.PURPLE_BALLOON, MubbleBlocks.MAGENTA_BALLOON, MubbleBlocks.PINK_BALLOON
			);
		BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.FLUID_TANK, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.TETRIS_GLASS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.ACACIA_LEAF_PILE,
				MubbleBlocks.ALLIUM_PILE, MubbleBlocks.AUTUMN_OAK_LEAF_PILE, MubbleBlocks.AZURE_BLUET_PILE,
				MubbleBlocks.BIRCH_LEAF_PILE, MubbleBlocks.BLUE_ORCHID_PILE, MubbleBlocks.CORNFLOWER_PILE,
				MubbleBlocks.DANDELION_PILE, MubbleBlocks.DARK_OAK_LEAF_PILE, MubbleBlocks.JUNGLE_LEAF_PILE,
				MubbleBlocks.LILY_OF_THE_VALLEY_PILE, MubbleBlocks.OAK_LEAF_PILE, MubbleBlocks.ORANGE_TULIP_PILE,
				MubbleBlocks.OXEYE_DAISY_PILE, MubbleBlocks.PALM_LEAF_PILE, MubbleBlocks.PINK_CHERRY_OAK_LEAF_PILE,
				MubbleBlocks.PINK_PRESS_GARDEN_LEAF_PILE, MubbleBlocks.PINK_TULIP_PILE, MubbleBlocks.POPPY_PILE,
				MubbleBlocks.RED_PRESS_GARDEN_LEAF_PILE, MubbleBlocks.RED_TULIP_PILE, MubbleBlocks.SCARLET_LEAF_PILE,
				MubbleBlocks.SPRUCE_LEAF_PILE, MubbleBlocks.WHITE_CHERRY_OAK_LEAF_PILE, MubbleBlocks.WHITE_TULIP_PILE,
				MubbleBlocks.WITHER_ROSE_PILE
			);
	}
}
