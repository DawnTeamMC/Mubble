package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.object.entity.DuckEntity;
import hugman.mubble.util.creator.BlockTemplate;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;

public class MubbleGenerators {
	public static void handleBiome(Biome biome) {
		if(!biome.getCategory().equals(Biome.Category.NETHER) && !biome.getCategory().equals(Biome.Category.THEEND)) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, MubbleBlocks.BLUNITE_BLOCKS.getBlock(BlockTemplate.CUBE).getDefaultState(), 33)).method_30377(80).spreadHorizontally().repeat(10));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, MubbleBlocks.CARBONITE_BLOCKS.getBlock(BlockTemplate.CUBE).getDefaultState(), 33)).method_30377(80).spreadHorizontally().repeat(10));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, MubbleBlocks.VANADIUM_ORE.getBlock().getDefaultState(), 6)).method_30377(80).spreadHorizontally().repeat(10));
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
		Biomes.END_MIDLANDS.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, MubbleFeatures.ENDER_BOULDER.configure(new SingleStateFeatureConfig(Blocks.OBSIDIAN.getDefaultState())).decorate(ConfiguredFeatures.Decorators.field_26165).repeatRandomly(2));
	}

	public static void init() {
		BuiltinRegistries.BIOME.forEach(MubbleGenerators::handleBiome);
		RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, identifier, biome) -> MubbleGenerators.handleBiome(biome));
	}
}