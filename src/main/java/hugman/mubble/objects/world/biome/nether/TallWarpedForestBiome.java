package hugman.mubble.objects.world.biome.nether;

import com.google.common.collect.ImmutableList;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class TallWarpedForestBiome extends Biome
{
	public TallWarpedForestBiome()
	{
		super((new Settings())
				.configureSurfaceBuilder(SurfaceBuilder.NETHER_FOREST, SurfaceBuilder.WARPED_NYLIUM_CONFIG)
				.precipitation(Precipitation.NONE)
				.category(Category.NETHER)
				.depth(0.2F)
				.scale(0.4F)
				.temperature(2.0F)
				.downfall(0.0F)
				.effects((new BiomeEffects.Builder())
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(1705242)
						.particleConfig(new BiomeParticleConfig(ParticleTypes.WARPED_SPORE, 0.01428F))
						.loopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
						.moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D))
						.additionsSound(new BiomeAdditionsSound(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
						.music(MusicType.method_27283(SoundEvents.MUSIC_NETHER_WARPED_FOREST))
						.build())
				.parent("warped_forest")
				.noises(ImmutableList.of(new MixedNoisePoint(0.0F, 0.5F, 0.1F, 0.0F, 0.375F))));
		this.addStructureFeature(DefaultBiomeFeatures.NETHER_RUINED_PORTAL);
		this.addStructureFeature(DefaultBiomeFeatures.FORTRESS);
		this.addStructureFeature(DefaultBiomeFeatures.BASTION_REMNANT);
		this.addCarver(GenerationStep.Carver.AIR, configureCarver(Carver.NETHER_CAVE, new ProbabilityConfig(0.2F)));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.LAVA_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_VERY_BIASED_RANGE.configure(new RangeDecoratorConfig(20, 8, 16, 256))));
		DefaultBiomeFeatures.addDefaultMushrooms(this);
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.NETHER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 4, 8, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.NETHER_FIRE_CONFIG).createDecoratedFeature(Decorator.FIRE.configure(new CountDecoratorConfig(10))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.SOUL_FIRE_CONFIG).createDecoratedFeature(Decorator.FIRE.configure(new CountDecoratorConfig(10))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.GLOWSTONE_BLOB.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.LIGHT_GEM_CHANCE.configure(new CountDecoratorConfig(10))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.GLOWSTONE_BLOB.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NETHERRACK, Blocks.MAGMA_BLOCK.getDefaultState(), 33)).createDecoratedFeature(Decorator.MAGMA.configure(new CountDecoratorConfig(4))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.ENCLOSED_NETHER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 10, 20, 128))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleFeatures.TALL_HUGE_FUNGUS.configure(HugeFungusFeatureConfig.WARPED_FUNGUS_NOT_PLANTED_CONFIG).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(8))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.NETHER_FOREST_VEGETATION.configure(DefaultBiomeFeatures.WARPED_ROOTS_CONFIG).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(5))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.NETHER_FOREST_VEGETATION.configure(DefaultBiomeFeatures.NETHER_SPROUTS_CONFIG).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(4))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.TWISTING_VINES.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 128))));
		DefaultBiomeFeatures.addNetherMineables(this);
		this.addSpawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.ENDERMAN, 1, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.STRIDER, 60, 2, 4));
		this.addSpawnDensity(EntityType.ENDERMAN, 1.0D, 0.08D);
	}
}