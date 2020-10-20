package com.hugman.mubble.init.client;

import com.hugman.mubble.init.MubbleBlocks;
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
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> pos != null && world != null ? BiomeColors.getGrassColor(pos, world) : GrassColors.getColor(0.5D, 1.0D), MubbleBlocks.GREEN_HILL_GRASS_BLOCK);
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> 15218768, MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> 15771888, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> 10622269, MubbleBlocks.SCARLET_WOOD.getLeaves(), MubbleBlocks.SCARLET_WOOD.getLeafPile());
	}

	private static void registerItemColors() {
		ColorProviderRegistry.ITEM.register((item, layer) -> GrassColors.getColor(0.5D, 1.0D), MubbleBlocks.GREEN_HILL_GRASS_BLOCK);
		ColorProviderRegistry.ITEM.register((item, layer) -> 15218768, MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) -> 15771888, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getLeaves(), MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getLeafPile());
		ColorProviderRegistry.ITEM.register((item, layer) -> 10622269, MubbleBlocks.SCARLET_WOOD.getLeaves(), MubbleBlocks.SCARLET_WOOD.getLeafPile());
	}
}