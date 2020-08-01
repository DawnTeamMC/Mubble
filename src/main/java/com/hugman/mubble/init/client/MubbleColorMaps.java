package com.hugman.mubble.init.client;

import com.hugman.mubble.init.MubbleBlockPack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;

@Environment(EnvType.CLIENT)
public class MubbleColorMaps {
	public static void registerColors() {
		registerBlockColors();
		registerItemColors();
	}

	private static void registerBlockColors() {
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return pos != null && world != null ? BiomeColors.getGrassColor(pos, world) : GrassColors.getColor(0.5D, 1.0D);
		}, MubbleBlockPack.GREEN_HILL_GRASS_BLOCK);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return pos != null && world != null ? BiomeColors.getFoliageColor(pos, world) : FoliageColors.getDefaultColor();
		}, MubbleBlockPack.PALM_WOOD.getLeaves(), MubbleBlockPack.PALM_WOOD.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15232304;
		}, MubbleBlockPack.AUTUMN_OAK_LEAVES.getLeaves(), MubbleBlockPack.AUTUMN_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15645495;
		}, MubbleBlockPack.AUTUMN_BIRCH_LEAVES.getLeaves(), MubbleBlockPack.AUTUMN_BIRCH_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15768259;
		}, MubbleBlockPack.PINK_CHERRY_OAK_LEAVES.getLeaves(), MubbleBlockPack.PINK_CHERRY_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15786729;
		}, MubbleBlockPack.WHITE_CHERRY_OAK_LEAVES.getLeaves(), MubbleBlockPack.WHITE_CHERRY_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15218768;
		}, MubbleBlockPack.RED_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlockPack.RED_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 15771888;
		}, MubbleBlockPack.PINK_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlockPack.PINK_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) ->
		{
			return 10622269;
		}, MubbleBlockPack.SCARLET_WOOD.getLeaves(), MubbleBlockPack.SCARLET_WOOD.getLeafPile());
	}

	private static void registerItemColors() {
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return GrassColors.getColor(0.5D, 1.0D);
		}, MubbleBlockPack.PALM_WOOD.getLeaves(), MubbleBlockPack.PALM_WOOD.getLeafPile(), MubbleBlockPack.GREEN_HILL_GRASS_BLOCK);
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15232304;
		}, MubbleBlockPack.AUTUMN_OAK_LEAVES.getLeaves(), MubbleBlockPack.AUTUMN_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15645495;
		}, MubbleBlockPack.AUTUMN_BIRCH_LEAVES.getLeaves(), MubbleBlockPack.AUTUMN_BIRCH_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15768259;
		}, MubbleBlockPack.PINK_CHERRY_OAK_LEAVES.getLeaves(), MubbleBlockPack.PINK_CHERRY_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15786729;
		}, MubbleBlockPack.WHITE_CHERRY_OAK_LEAVES.getLeaves(), MubbleBlockPack.WHITE_CHERRY_OAK_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15218768;
		}, MubbleBlockPack.RED_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlockPack.RED_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 15771888;
		}, MubbleBlockPack.PINK_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlockPack.PINK_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) ->
		{
			return 10622269;
		}, MubbleBlockPack.SCARLET_WOOD.getLeaves(), MubbleBlockPack.SCARLET_WOOD.getLeafPile());
	}
}