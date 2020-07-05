package hugman.mubble.init.client;

import hugman.mubble.init.MubbleBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MubbleRenderLayers {
	public static void init() {
		// Grass Blocks
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.GREEN_HILL_GRASS_BLOCK
		);
		// Flowers
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.BOOMERANG_FLOWER,
				MubbleBlocks.CLOUD_FLOWER, MubbleBlocks.FIRE_FLOWER, MubbleBlocks.GOLD_FLOWER,
				MubbleBlocks.ICE_FLOWER, MubbleBlocks.POTATO_FLOWER, MubbleBlocks.SCARLET_ORCHID,
				MubbleBlocks.AMARANTH_ROOTS.getBlock()
		);
		// Mushrooms
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.BLACK_MUSHROOM,
				MubbleBlocks.BLUE_MUSHROOM, MubbleBlocks.CYAN_MUSHROOM, MubbleBlocks.GRAY_MUSHROOM,
				MubbleBlocks.GREEN_MUSHROOM, MubbleBlocks.LIGHT_BLUE_MUSHROOM, MubbleBlocks.LIGHT_GRAY_MUSHROOM,
				MubbleBlocks.LIME_MUSHROOM, MubbleBlocks.MAGENTA_MUSHROOM, MubbleBlocks.ORANGE_MUSHROOM,
				MubbleBlocks.PINK_MUSHROOM, MubbleBlocks.PURPLE_MUSHROOM, MubbleBlocks.WHITE_MUSHROOM,
				MubbleBlocks.YELLOW_MUSHROOM, MubbleBlocks.SCARLET_MUSHROOM
		);
		// Food Plants
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.BLUEBERRY_BUSH,
				MubbleBlocks.SALAD, MubbleBlocks.TOMATOES
		);
		// Shiny Garlands
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.RED_SHINY_GARLAND.getBlock(),
				MubbleBlocks.SILVER_SHINY_GARLAND.getBlock(), MubbleBlocks.GOLD_SHINY_GARLAND.getBlock()
		);
		// Presents
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.BLUE_PRESENT,
				MubbleBlocks.GREEN_PRESENT, MubbleBlocks.YELLOW_PRESENT, MubbleBlocks.RED_PRESENT,
				MubbleBlocks.PURPLE_PRESENT, MubbleBlocks.BLACK_PRESENT, MubbleBlocks.WHITE_PRESENT,
				MubbleBlocks.GOLDEN_PRESENT
		);
		// Balloons
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), MubbleBlocks.WHITE_BALLOON,
				MubbleBlocks.LIGHT_GRAY_BALLOON, MubbleBlocks.GRAY_BALLOON, MubbleBlocks.BLACK_BALLOON,
				MubbleBlocks.BROWN_BALLOON, MubbleBlocks.RED_BALLOON, MubbleBlocks.ORANGE_BALLOON,
				MubbleBlocks.YELLOW_BALLOON, MubbleBlocks.LIME_BALLOON, MubbleBlocks.GREEN_BALLOON,
				MubbleBlocks.CYAN_BALLOON, MubbleBlocks.LIGHT_BLUE_BALLOON, MubbleBlocks.BLUE_BALLOON,
				MubbleBlocks.PURPLE_BALLOON, MubbleBlocks.MAGENTA_BALLOON, MubbleBlocks.PINK_BALLOON
		);
		// Potted Plants
		MubbleBlocks.POTTED_PLANTS.forEach((block ->
		{
			BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
		}));
		// Others
		BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.TETRIS_GLASS, RenderLayer.getTranslucent());
	}
}
