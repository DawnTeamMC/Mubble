package hugman.mubble.init.client;

import hugman.mubble.init.MubbleBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class MubbleRenderLayers
{
	public static void init()
	{
		// Shiny Garlands
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.RED_SHINY_GARLAND,
				MubbleBlocks.SILVER_SHINY_GARLAND, MubbleBlocks.GOLD_SHINY_GARLAND
		);
		// Presents
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.BLUE_PRESENT,
				MubbleBlocks.GREEN_PRESENT, MubbleBlocks.YELLOW_PRESENT, MubbleBlocks.RED_PRESENT,
				MubbleBlocks.PURPLE_PRESENT, MubbleBlocks.BLACK_PRESENT, MubbleBlocks.WHITE_PRESENT,
				MubbleBlocks.GOLDEN_PRESENT
		);
		// Cloud Blocks
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), MubbleBlocks.WHITE_CLOUD_BLOCK,
				MubbleBlocks.LIGHT_GRAY_CLOUD_BLOCK, MubbleBlocks.GRAY_CLOUD_BLOCK, MubbleBlocks.BLACK_CLOUD_BLOCK,
				MubbleBlocks.BROWN_CLOUD_BLOCK, MubbleBlocks.RED_CLOUD_BLOCK, MubbleBlocks.ORANGE_CLOUD_BLOCK,
				MubbleBlocks.YELLOW_CLOUD_BLOCK, MubbleBlocks.LIME_CLOUD_BLOCK, MubbleBlocks.GREEN_CLOUD_BLOCK,
				MubbleBlocks.CYAN_CLOUD_BLOCK, MubbleBlocks.LIGHT_BLUE_CLOUD_BLOCK, MubbleBlocks.BLUE_CLOUD_BLOCK,
				MubbleBlocks.PURPLE_CLOUD_BLOCK, MubbleBlocks.MAGENTA_CLOUD_BLOCK, MubbleBlocks.PINK_CLOUD_BLOCK
		);
		// Balloons
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), MubbleBlocks.WHITE_BALLOON,
				MubbleBlocks.LIGHT_GRAY_BALLOON, MubbleBlocks.GRAY_BALLOON, MubbleBlocks.BLACK_BALLOON,
				MubbleBlocks.BROWN_BALLOON, MubbleBlocks.RED_BALLOON, MubbleBlocks.ORANGE_BALLOON,
				MubbleBlocks.YELLOW_BALLOON, MubbleBlocks.LIME_BALLOON, MubbleBlocks.GREEN_BALLOON,
				MubbleBlocks.CYAN_BALLOON, MubbleBlocks.LIGHT_BLUE_BALLOON, MubbleBlocks.BLUE_BALLOON,
				MubbleBlocks.PURPLE_BALLOON, MubbleBlocks.MAGENTA_BALLOON, MubbleBlocks.PINK_BALLOON
		);
		BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.FLUID_TANK, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(MubbleBlocks.TETRIS_GLASS, RenderLayer.getTranslucent());
		// Food Plants
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.BLUEBERRY_BUSH,
				MubbleBlocks.SALAD, MubbleBlocks.TOMATOES);
		// Pots
		MubbleBlocks.POTTED_PLANTS.forEach((block ->
		{
			BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
		}));
		// Piles
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.ACACIA_LEAF_PILE,
				MubbleBlocks.ALLIUM_PILE, MubbleBlocks.AUTUMN_BIRCH_LEAF_PILE, MubbleBlocks.AUTUMN_OAK_LEAF_PILE, MubbleBlocks.AZURE_BLUET_PILE,
				MubbleBlocks.BIRCH_LEAF_PILE, MubbleBlocks.BLUE_ORCHID_PILE, MubbleBlocks.CORNFLOWER_PILE,
				MubbleBlocks.DANDELION_PILE, MubbleBlocks.DARK_OAK_LEAF_PILE, MubbleBlocks.JUNGLE_LEAF_PILE,
				MubbleBlocks.LILY_OF_THE_VALLEY_PILE, MubbleBlocks.OAK_LEAF_PILE, MubbleBlocks.ORANGE_TULIP_PILE,
				MubbleBlocks.OXEYE_DAISY_PILE, MubbleBlocks.PALM_LEAF_PILE, MubbleBlocks.PINK_CHERRY_OAK_LEAF_PILE,
				MubbleBlocks.PINK_PRESS_GARDEN_LEAF_PILE, MubbleBlocks.PINK_TULIP_PILE, MubbleBlocks.POPPY_PILE,
				MubbleBlocks.RED_PRESS_GARDEN_LEAF_PILE, MubbleBlocks.RED_TULIP_PILE, MubbleBlocks.SCARLET_LEAF_PILE,
				MubbleBlocks.SPRUCE_LEAF_PILE, MubbleBlocks.WHITE_CHERRY_OAK_LEAF_PILE, MubbleBlocks.WHITE_TULIP_PILE,
				MubbleBlocks.WITHER_ROSE_PILE
		);
		// Flowers
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.BOOMERANG_FLOWER,
				MubbleBlocks.CLOUD_FLOWER, MubbleBlocks.FIRE_FLOWER, MubbleBlocks.GOLD_FLOWER,
				MubbleBlocks.ICE_FLOWER, MubbleBlocks.POTATO_FLOWER, MubbleBlocks.SCARLET_ORCHID);
		// Mushrooms
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.BLACK_MUSHROOM,
				MubbleBlocks.BLUE_MUSHROOM, MubbleBlocks.CYAN_MUSHROOM, MubbleBlocks.GRAY_MUSHROOM,
				MubbleBlocks.GREEN_MUSHROOM, MubbleBlocks.LIGHT_BLUE_MUSHROOM, MubbleBlocks.LIGHT_GRAY_MUSHROOM,
				MubbleBlocks.LIME_MUSHROOM, MubbleBlocks.MAGENTA_MUSHROOM, MubbleBlocks.ORANGE_MUSHROOM,
				MubbleBlocks.PINK_MUSHROOM, MubbleBlocks.PURPLE_MUSHROOM, MubbleBlocks.WHITE_MUSHROOM,
				MubbleBlocks.YELLOW_MUSHROOM, MubbleBlocks.SCARLET_MUSHROOM);
		// Saplings
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.AUTUMN_BIRCH_SAPLING, MubbleBlocks.AUTUMN_OAK_SAPLING,
				MubbleBlocks.PALM_SAPLING, MubbleBlocks.PINK_CHERRY_OAK_SAPLING, MubbleBlocks.PINK_PRESS_GARDEN_SAPLING,
				MubbleBlocks.RED_PRESS_GARDEN_SAPLING, MubbleBlocks.SCARLET_SAPLING, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
		// Leaves
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), MubbleBlocks.AUTUMN_BIRCH_LEAVES, MubbleBlocks.AUTUMN_OAK_LEAVES,
				MubbleBlocks.PALM_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES,
				MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES);
		// Doors
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.CHERRY_OAK_DOOR,
				MubbleBlocks.NSMBU_DOOR, MubbleBlocks.NSMBU_KEY_DOOR, MubbleBlocks.PALM_DOOR,
				MubbleBlocks.PRESS_GARDEN_DOOR, MubbleBlocks.SCARLET_DOOR, MubbleBlocks.SMB3_DOOR,
				MubbleBlocks.SMB3_KEY_DOOR, MubbleBlocks.SMB_DOOR, MubbleBlocks.SMB_KEY_DOOR,
				MubbleBlocks.SMW_DOOR, MubbleBlocks.SMW_KEY_DOOR);
		// Trapdoors
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), MubbleBlocks.CHERRY_OAK_TRAPDOOR,
				MubbleBlocks.PALM_TRAPDOOR, MubbleBlocks.PRESS_GARDEN_TRAPDOOR, MubbleBlocks.SCARLET_TRAPDOOR);
	}
}
