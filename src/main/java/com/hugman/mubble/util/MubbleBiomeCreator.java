package com.hugman.mubble.util;

import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class MubbleBiomeCreator {
	private static int getSkyColor(float temperature) {
		float g = temperature / 3.0F;
		g = MathHelper.clamp(g, -1.0F, 1.0F);
		return MathHelper.hsvToRgb(0.62222224F - g * 0.05F, 0.5F + g * 0.1F, 1.0F);
	}

	public static Biome createScarletForest() {
		SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
		DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);
		DefaultBiomeFeatures.addBatsAndMonsters(spawnBuilder);
		GenerationSettings.Builder generationBuilder = new GenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
		generationBuilder.structureFeature(ConfiguredStructureFeatures.MANSION);
		DefaultBiomeFeatures.addDefaultUndergroundStructures(generationBuilder);
		generationBuilder.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
		DefaultBiomeFeatures.addLandCarvers(generationBuilder);
		DefaultBiomeFeatures.addDefaultLakes(generationBuilder);
		DefaultBiomeFeatures.addDungeons(generationBuilder);
		generationBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.SCARLET_TREES);
		DefaultBiomeFeatures.addMineables(generationBuilder);
		DefaultBiomeFeatures.addDefaultOres(generationBuilder);
		generationBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_SCARLET_ORCHID);
		generationBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_SCARLET_MUSHROOM_NORMAL);
		DefaultBiomeFeatures.addForestGrass(generationBuilder);
		DefaultBiomeFeatures.addSprings(generationBuilder);
		DefaultBiomeFeatures.addFrozenTopLayer(generationBuilder);
		BiomeEffects.Builder effectBuilder = new BiomeEffects.Builder();
		effectBuilder.waterColor(4159204);
		effectBuilder.waterFogColor(329011);
		effectBuilder.fogColor(12638463);
		effectBuilder.skyColor(getSkyColor(0.7f));
		effectBuilder.grassColor(8720466);
		effectBuilder.foliageColor(10622269);
		effectBuilder.moodSound(BiomeMoodSound.CAVE);
		Biome.Builder builder = new Biome.Builder();
		builder.precipitation(Biome.Precipitation.RAIN);
		builder.category(Biome.Category.FOREST);
		builder.depth(0.1f);
		builder.scale(0.2f);
		builder.temperature(0.7f);
		builder.downfall(0.8f);
		builder.effects(effectBuilder.build());
		builder.spawnSettings(spawnBuilder.build());
		builder.generationSettings(generationBuilder.build()).build();
		return builder.build();
	}

	public static Biome createPressGarden() {
		SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
		DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);
		DefaultBiomeFeatures.addBatsAndMonsters(spawnBuilder);
		GenerationSettings.Builder generationBuilder = new GenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
		generationBuilder.structureFeature(ConfiguredStructureFeatures.MANSION);
		DefaultBiomeFeatures.addDefaultUndergroundStructures(generationBuilder);
		generationBuilder.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
		DefaultBiomeFeatures.addLandCarvers(generationBuilder);
		DefaultBiomeFeatures.addDefaultLakes(generationBuilder);
		DefaultBiomeFeatures.addDungeons(generationBuilder);
		DefaultBiomeFeatures.addLargeFerns(generationBuilder);
		generationBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PRESS_GARDEN_TREES);
		DefaultBiomeFeatures.addMineables(generationBuilder);
		DefaultBiomeFeatures.addDefaultOres(generationBuilder);
		DefaultBiomeFeatures.addDefaultDisks(generationBuilder);
		DefaultBiomeFeatures.addDefaultFlowers(generationBuilder);
		DefaultBiomeFeatures.addDefaultMushrooms(generationBuilder);
		DefaultBiomeFeatures.addDefaultVegetation(generationBuilder);
		generationBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_RED_PRESS_GARDEN_LEAF_PILE_NORMAL);
		generationBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_PINK_PRESS_GARDEN_LEAF_PILE_NORMAL);
		DefaultBiomeFeatures.addSweetBerryBushesSnowy(generationBuilder);
		DefaultBiomeFeatures.addFrozenTopLayer(generationBuilder);
		BiomeEffects.Builder effectBuilder = new BiomeEffects.Builder();
		effectBuilder.waterColor(4020182);
		effectBuilder.waterFogColor(329011);
		effectBuilder.fogColor(12638463);
		effectBuilder.skyColor(getSkyColor(0.3f));
		effectBuilder.grassColor(15594485);
		effectBuilder.foliageColor(15594485);
		effectBuilder.moodSound(BiomeMoodSound.CAVE);
		Biome.Builder builder = new Biome.Builder();
		builder.precipitation(Biome.Precipitation.SNOW);
		builder.category(Biome.Category.TAIGA);
		builder.depth(0.2f);
		builder.scale(0.2f);
		builder.temperature(0.3f);
		builder.downfall(0.4f);
		builder.effects(effectBuilder.build());
		builder.spawnSettings(spawnBuilder.build());
		builder.generationSettings(generationBuilder.build()).build();
		return builder.build();
	}

}
