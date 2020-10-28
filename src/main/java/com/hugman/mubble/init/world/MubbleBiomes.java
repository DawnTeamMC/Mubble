package com.hugman.mubble.init.world;

import com.hugman.dawn.api.creator.BiomeCreator;
import com.hugman.mubble.Mubble;
import com.hugman.mubble.config.MubbleConfig;
import com.hugman.mubble.init.MubblePack;
import com.hugman.mubble.util.MubbleBiomeCreator;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class MubbleBiomes extends MubblePack {
	public static final MubbleConfig.BiomesCategory CONFIG = Mubble.CONFIG.biomes;
	public static RegistryKey<Biome> PRESS_GARDEN;
	public static RegistryKey<Biome> SCARLET_FOREST;

	public static void init() {
		BiomeCreator.Builder pressGardenBuilder = new BiomeCreator.Builder("press_garden", MubbleBiomeCreator.createPressGarden());
		BiomeCreator.Builder scarletForestBuilder = new BiomeCreator.Builder("scarlet_forest", MubbleBiomeCreator.createScarletForest());
		if(CONFIG.press_garden) pressGardenBuilder.addToOverworldContinental(OverworldClimate.SNOWY, 0.25D);
		if(CONFIG.scarlet_forest) scarletForestBuilder.addToOverworldContinental(OverworldClimate.COOL, 1D);
		PRESS_GARDEN = register(pressGardenBuilder);
		SCARLET_FOREST = register(scarletForestBuilder);
	}
}