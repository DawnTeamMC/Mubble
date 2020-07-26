package com.hugman.mubble.util;

import com.google.common.collect.ImmutableList;
import com.hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.*;

public class MubbleBiomeCreator {
	private static class Helper {
		private static Biome.Settings copyBiomeSettings(Biome biome) {
			return new Biome.Settings()
					.surfaceBuilder(biome.getSurfaceBuilder())
					.precipitation(biome.getPrecipitation())
					.category(biome.getCategory())
					.depth(biome.getDepth())
					.scale(biome.getScale())
					.temperature(biome.getTemperature())
					.downfall(biome.getDownfall())
					.effects(biome.getEffects());
		}

		private static int getSkyColor(float f) {
			float g = f / 3.0F;
			g = MathHelper.clamp(g, -1.0F, 1.0F);
			return MathHelper.hsvToRgb(0.62222224F - g * 0.05F, 0.5F + g * 0.1F, 1.0F);
		}
	}

	public static Biome createPumpkinPastures() {
		Biome biome = createForest(0.8F, 0.9F, 155336, 541, 15232304, 15443554);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PUMPKIN_PASTURES_TREES);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_AUTUMN_BIRCH_LEAF_PILE);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_AUTUMN_OAK_LEAF_PILE);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_YELLOW_MUSHROOM_NORMAL);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_ORANGE_MUSHROOM_NORMAL);
		return biome;
	}

	public static Biome createCherryOakForest(boolean isPink) {
		Biome biome = createForest(0.6F, 0.4F, 6459391, 2170954, 15768259);
		if(isPink) {
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PINK_CHERRY_OAK_FOREST_TREES);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_PINK_CHERRY_OAK_LEAF_PILE);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_PINK_MUSHROOM_NORMAL);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_MAGENTA_MUSHROOM_NORMAL);
		}
		else {
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.WHITE_CHERRY_OAK_FOREST_TREES);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_WHITE_CHERRY_OAK_LEAF_PILE);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_WHITE_MUSHROOM_NORMAL);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.PATCH_LIGHT_GRAY_MUSHROOM_NORMAL);
		}
		return biome;
	}

	private static Biome createForest(float temperature, float downfall, int waterColor, int waterFogColor, int foliageColor, int grassColor) {
		Biome biome = new Biome(Helper.copyBiomeSettings(Biomes.FOREST)
				.temperature(temperature)
				.downfall(downfall)
				.effects((new BiomeEffects.Builder())
						.waterColor(waterColor)
						.waterFogColor(waterFogColor)
						.fogColor(12638463)
						.skyColor(Helper.getSkyColor(temperature))
						.grassColor(grassColor)
						.foliageColor(foliageColor)
						.moodSound(BiomeMoodSound.CAVE)
						.build())
				.parent(null));
		DefaultBiomeFeatures.addDefaultUndergroundStructures(biome);
		biome.addStructureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
		DefaultBiomeFeatures.addLandCarvers(biome);
		DefaultBiomeFeatures.addDefaultLakes(biome);
		DefaultBiomeFeatures.addDungeons(biome);
		DefaultBiomeFeatures.addForestFlowers(biome);
		DefaultBiomeFeatures.addMineables(biome);
		DefaultBiomeFeatures.addDefaultOres(biome);
		DefaultBiomeFeatures.addDefaultDisks(biome);
		DefaultBiomeFeatures.addDefaultFlowers(biome);
		DefaultBiomeFeatures.addForestGrass(biome);
		DefaultBiomeFeatures.addDefaultMushrooms(biome);
		DefaultBiomeFeatures.addDefaultVegetation(biome);
		DefaultBiomeFeatures.addSprings(biome);
		DefaultBiomeFeatures.addFrozenTopLayer(biome);
		DefaultBiomeFeatures.addFarmAnimals(biome);
		DefaultBiomeFeatures.addBatsAndMonsters(biome);
		biome.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.WOLF, 5, 4, 4));
		return biome;
	}

	private static Biome createForest(float temperature, float downfall, int waterColor, int waterFogColor, int foliageColor) {
		double d = MathHelper.clamp(temperature, 0.0F, 1.0F);
		double e = MathHelper.clamp(downfall, 0.0F, 1.0F);
		return createForest(temperature, downfall, waterColor, waterFogColor, foliageColor, GrassColors.getColor(d, e));
	}

	public static Biome createTallNetherForest(boolean isWarped) {
		Biome baseBiome = isWarped ? Biomes.WARPED_FOREST : Biomes.CRIMSON_FOREST;
		Biome biome = new Biome(Helper.copyBiomeSettings(baseBiome).depth(baseBiome.getDepth() * 2).scale(baseBiome.getScale() * 2));
		biome.addStructureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_NETHER);
		biome.addCarver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE);
		biome.addStructureFeature(ConfiguredStructureFeatures.FORTRESS);
		biome.addStructureFeature(ConfiguredStructureFeatures.BASTION_REMNANT);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.SPRING_LAVA);
		DefaultBiomeFeatures.addDefaultMushrooms(biome);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.SPRING_OPEN);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.PATCH_FIRE);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.GLOWSTONE_EXTRA);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.GLOWSTONE);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.ORE_MAGMA);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.SPRING_CLOSED);
		DefaultBiomeFeatures.addNetherMineables(biome);
		if(isWarped) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.PATCH_SOUL_FIRE);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.TALL_WARPED_FUNGI);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.WARPED_FOREST_VEGETATION);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.NETHER_SPROUTS);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.TWISTING_VINES);
			biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 1, 4, 4));
			biome.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.STRIDER, 60, 1, 2));
			biome.addSpawnDensity(EntityType.ENDERMAN, 1.0D, 0.12D);
		}
		else {
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.WEEPING_VINES);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleConfiguredFeatures.TALL_CRIMSON_FUNGI);
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.CRIMSON_FOREST_VEGETATION);
			biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4));
			biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.HOGLIN, 9, 3, 4));
			biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.PIGLIN, 5, 3, 4));
			biome.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.STRIDER, 60, 1, 2));
		}
		return biome;
	}

	private static Biome createGallery() {
		Biome biome = new Biome(Helper.copyBiomeSettings(Biomes.NETHER_WASTES).parent(Biomes.NETHER_WASTES.toString()));
		biome.addStructureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_NETHER);
		biome.addStructureFeature(ConfiguredStructureFeatures.FORTRESS);
		biome.addStructureFeature(ConfiguredStructureFeatures.BASTION_REMNANT);
		biome.addCarver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.SPRING_LAVA);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.SPRING_OPEN);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.PATCH_FIRE);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.PATCH_SOUL_FIRE);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.GLOWSTONE_EXTRA);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.GLOWSTONE);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.ORE_MAGMA);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.SPRING_CLOSED);
		DefaultBiomeFeatures.addNetherMineables(biome);
		biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.GHAST, 50, 4, 4));
		biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4));
		biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.MAGMA_CUBE, 2, 4, 4));
		biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 1, 4, 4));
		biome.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.PIGLIN, 15, 4, 4));
		biome.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.STRIDER, 60, 1, 2));
		return biome;
	}

	public static Biome createTritanopianGallery() {
		Biome biome = createGallery();
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_PINK_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_CYAN_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_PINK,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_PINK_FLAT,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN_FLAT))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_PINK_UPSIDE,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN_UPSIDE))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_PINK_UPSIDE_FLAT,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN_UPSIDE_FLAT))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		return biome;
	}

	public static Biome createAchromatopsianGallery() {
		Biome biome = createGallery();
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_WHITE_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_LIGHT_GRAY_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_GRAY_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_BLACK_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_GRAY,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BLACK))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_GRAY_FLAT,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BLACK_FLAT))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_WHITE_UPSIDE,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_LIGHT_GRAY_UPSIDE))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_WHITE_UPSIDE_FLAT,
						() -> MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_LIGHT_GRAY_UPSIDE_FLAT))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		return biome;
	}

	public static Biome createProtanopianGallery() {
		Biome biome = createGallery();
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.BROWN_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_YELLOW_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_CYAN_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleConfiguredFeatures.PATCH_BLUE_MUSHROOM_NETHER);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BROWN_FLAT.withChance(0.25F),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_YELLOW.withChance(0.25F),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN.withChance(0.25F)),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BLUE))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BROWN_UPSIDE_FLAT.withChance(0.25F),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_YELLOW_UPSIDE.withChance(0.25F),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN_UPSIDE.withChance(0.25F)),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BLUE_UPSIDE))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_YELLOW_FLAT.withChance(1F / 3F),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN_FLAT.withChance(1F / 3F)),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BLUE_FLAT))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_YELLOW_UPSIDE_FLAT.withChance(1F / 3F),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_CYAN_UPSIDE_FLAT.withChance(1F / 3F)),
						MubbleConfiguredFeatures.HUGE_NETHER_MUSHROOM_BLUE_UPSIDE_FLAT))
						.decorate(ConfiguredFeatures.Decorators.field_26165));
		return biome;
	}
}
