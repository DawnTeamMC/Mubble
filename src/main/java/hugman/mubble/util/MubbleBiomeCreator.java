package hugman.mubble.util;

import com.google.common.collect.ImmutableList;
import hugman.mubble.init.world.MubbleConfiguredFeatures;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.decorator.CountDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class MubbleBiomeCreator {

	public static Biome createGallery() {
		Biome biome = new Biome((new Biome.Settings())
				.surfaceBuilder(ConfiguredSurfaceBuilders.NETHER)
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.NETHER)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(2.0F)
				.downfall(0.0F)
				.effects((new BiomeEffects.Builder())
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(3344392)
						.method_30820(getSkyColor(2.0F))
						.loopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP)
						.moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0D))
						.additionsSound(new BiomeAdditionsSound(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))
						.music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_NETHER_WASTES))
						.build())
				.parent((String)null));
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

	private static int getSkyColor(float f) {
		float g = f / 3.0F;
		g = MathHelper.clamp(g, -1.0F, 1.0F);
		return MathHelper.hsvToRgb(0.62222224F - g * 0.05F, 0.5F + g * 0.1F, 1.0F);
	}
}
