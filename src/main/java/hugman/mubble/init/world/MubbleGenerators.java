package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;

public class MubbleGenerators
{
	public static void registerOres()
	{
		for (Biome biome : Registry.BIOME)
		{
			if (!biome.getCategory().equals(Category.NETHER) && !biome.getCategory().equals(Category.THEEND))
			{
				biome.addFeature
				(
					GenerationStep.Feature.UNDERGROUND_ORES,
					Feature.ORE.configure
					(
						new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.BLUNITE.getDefaultState(), 33)
					)
					.createDecoratedFeature
					(
						Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))
					)
				);
				
				biome.addFeature
				(
					GenerationStep.Feature.UNDERGROUND_ORES,
					Feature.ORE.configure
					(
						new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.CARBONITE.getDefaultState(), 33)
					)
					.createDecoratedFeature
					(
						Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))
					)
				);
				
				biome.addFeature
				(
					GenerationStep.Feature.UNDERGROUND_ORES,
					Feature.ORE.configure
					(
						new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.VANADIUM_ORE.getDefaultState(), 6)
					)
					.createDecoratedFeature
					(
						Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 16))
					)
				);
			}
		}
	}
	
	public static void registerTrees()
	{
		for (Biome biome : Registry.BIOME)
		{
			if (biome.getCategory().equals(Category.DESERT))
			{
				biome.addFeature
				(
					GenerationStep.Feature.VEGETAL_DECORATION,
					MubbleFeatures.PALM_TREE.configure
					( 
						new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PALM_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(2, 0)).build()
					)
					.createDecoratedFeature
					(
						Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.12F, 1))
					)
				);
			}
		}
	}
	
	public static void registerSpawns()
	{
		for (Biome biome : Registry.BIOME)
		{
			if (biome.getCategory().equals(Category.PLAINS))
			{
				biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.TOAD, 10, 4, 4));
			}
		}
	}
}