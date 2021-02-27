package com.hugman.mubble.init.world;

import com.hugman.dawn.api.creator.BiomeCreator;
import com.hugman.mubble.Mubble;
import com.hugman.mubble.config.MubbleConfig;
import com.hugman.mubble.init.MubblePack;
import com.hugman.mubble.object.world.gen.MubbleBiomeCreator;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class MubbleBiomes extends MubblePack {
	public static final MubbleConfig.BiomesCategory CONFIG = Mubble.CONFIG.biomes;
	public static RegistryKey<Biome> SCAMPER_SHORES = register(new BiomeCreator.Builder("scamper_shores", MubbleBiomeCreator.createScamperShores()));

	public static RegistryKey<Biome> PRESS_GARDEN = register(new BiomeCreator.Builder("press_garden", MubbleBiomeCreator.createPressGarden()));

	public static RegistryKey<Biome> SCARLET_FOREST = register(new BiomeCreator.Builder("scarlet_forest", MubbleBiomeCreator.createScarletForest()));

	public static void init() {
	}

	public static void addToGen() {
		OverworldBiomes.addContinentalBiome(SCAMPER_SHORES, OverworldClimate.TEMPERATE, 0.6D);
		if(CONFIG.press_garden) OverworldBiomes.addContinentalBiome(PRESS_GARDEN, OverworldClimate.SNOWY, 0.25D);
		if(CONFIG.scarlet_forest) OverworldBiomes.addContinentalBiome(SCARLET_FOREST, OverworldClimate.COOL, 1D);
	}
}