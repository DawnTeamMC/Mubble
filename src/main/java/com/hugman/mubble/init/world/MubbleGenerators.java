package com.hugman.mubble.init.world;

import com.hugman.mubble.init.MubbleEntities;
import com.hugman.mubble.object.entity.DuckEntity;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;

public class MubbleGenerators {
	public static void handleBiome(Biome biome) {
		if(!biome.getCategory().equals(Biome.Category.NETHER) && !biome.getCategory().equals(Biome.Category.THEEND)) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, MubbleConfiguredFeatures.ORE_BLUNITE);
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, MubbleConfiguredFeatures.ORE_CARBONITE);
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, MubbleConfiguredFeatures.ORE_VANADIUM);
		}
		if(biome.getCategory().equals(Biome.Category.DESERT)) {
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PALM.decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.12F, 1))));
		}
		if(biome.getCategory().equals(Biome.Category.PLAINS)) {
			biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.TOAD, 10, 4, 4));
		}
		if(DuckEntity.getSpawnBiomes().contains(biome)) {
			biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.DUCK, 10, 4, 4));
		}
		Biomes.END_MIDLANDS.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, MubbleConfiguredFeatures.ENDER_BOULDER);
	}

	public static void init() {
		BuiltinRegistries.BIOME.forEach(MubbleGenerators::handleBiome);
		RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, identifier, biome) -> MubbleGenerators.handleBiome(biome));
	}
}