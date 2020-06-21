package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.objects.entity.DuckEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ForestRockFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class MubbleGenerators {
	public static void handleBiome(Biome biome) {
		if(!biome.getCategory().equals(Category.NETHER) && !biome.getCategory().equals(Category.THEEND)) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.BLUNITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.CARBONITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.VANADIUM_ORE.getDefaultState(), 6)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 16))));
		}
		if(biome.getCategory().equals(Category.DESERT)) {
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.TREE.configure(MubbleFeatureConfigs.PALM_TREE).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.12F, 1))));
		}
		if(biome.getCategory().equals(Category.PLAINS)) {
			biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.TOAD, 10, 4, 4));
		}
		if(DuckEntity.getSpawnBiomes().contains(biome)) {
			biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.DUCK, 10, 4, 4));
		}

		if(biome == Biomes.END_MIDLANDS) {
			biome.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, MubbleFeatures.ENDER_BOULDER.configure(new ForestRockFeatureConfig(Blocks.OBSIDIAN.getDefaultState(), 0)).createDecoratedFeature(Decorator.FOREST_ROCK.configure(new CountDecoratorConfig(3))));
		}
	}
}