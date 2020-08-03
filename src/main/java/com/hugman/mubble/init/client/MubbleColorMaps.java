package com.hugman.mubble.init.client;

import com.hugman.mubble.init.MubbleBlockPack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
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
		}, MubbleBlockPack.GREEN_HILL_GRASS_BLOCK);
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