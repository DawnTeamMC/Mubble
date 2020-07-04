package hugman.mubble.init.client;

import hugman.mubble.init.MubbleBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;

@Environment(EnvType.CLIENT)
public class MubbleColorMaps {
	public static void registerBlockColors() {
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return pos != null && world != null ? BiomeColors.getGrassColor(pos, world) : GrassColors.getColor(0.5D, 1.0D);
		}, MubbleBlocks.GREEN_HILL_GRASS_BLOCK);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return FoliageColors.getSpruceColor();
		}, MubbleBlocks.SPRUCE_LEAF_PILE);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return FoliageColors.getBirchColor();
		}, MubbleBlocks.BIRCH_LEAF_PILE);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return pos != null && world != null ? BiomeColors.getFoliageColor(pos, world) : FoliageColors.getDefaultColor();
		}, MubbleBlocks.OAK_LEAF_PILE, MubbleBlocks.JUNGLE_LEAF_PILE, MubbleBlocks.ACACIA_LEAF_PILE, MubbleBlocks.DARK_OAK_LEAF_PILE, MubbleBlocks.PALM_LEAVES.getLeaves(), MubbleBlocks.PALM_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15232304;
		}, MubbleBlocks.AUTUMN_OAK_LEAVES.getLeaves(), MubbleBlocks.AUTUMN_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15645495;
		}, MubbleBlocks.AUTUMN_BIRCH_LEAVES.getLeaves(), MubbleBlocks.AUTUMN_BIRCH_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15768259;
		}, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_LEAF_PILE);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15786729;
		}, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_LEAF_PILE);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15218768;
		}, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_LEAF_PILE);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15771888;
		}, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_LEAF_PILE);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 10622269;
		}, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_LEAF_PILE);
	}

	public static void registerItemColors() {
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return GrassColors.getColor(0.5D, 1.0D);
		}, MubbleBlocks.OAK_LEAF_PILE, MubbleBlocks.SPRUCE_LEAF_PILE, MubbleBlocks.BIRCH_LEAF_PILE, MubbleBlocks.JUNGLE_LEAF_PILE, MubbleBlocks.ACACIA_LEAF_PILE, MubbleBlocks.DARK_OAK_LEAF_PILE, MubbleBlocks.PALM_LEAVES.getLeaves(), MubbleBlocks.PALM_LEAVES.getLeafPile(), MubbleBlocks.GREEN_HILL_GRASS_BLOCK);
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15232304;
		}, MubbleBlocks.AUTUMN_OAK_LEAVES.getLeaves(), MubbleBlocks.AUTUMN_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15645495;
		}, MubbleBlocks.AUTUMN_BIRCH_LEAVES.getLeaves(), MubbleBlocks.AUTUMN_BIRCH_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15768259;
		}, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_LEAF_PILE);
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15786729;
		}, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_LEAF_PILE);
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15218768;
		}, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_LEAF_PILE);
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15771888;
		}, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_LEAF_PILE);
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 10622269;
		}, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_LEAF_PILE);
	}
}