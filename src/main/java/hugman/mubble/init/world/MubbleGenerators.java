package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.objects.entity.DuckEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class MubbleGenerators
{
	private static final BlockState BLUNITE = MubbleBlocks.BLUNITE.getDefaultState();
	private static final BlockState CARBONITE = MubbleBlocks.CARBONITE.getDefaultState();
	private static final BlockState VANADIUM_ORE = MubbleBlocks.VANADIUM_ORE.getDefaultState();

	public static void registerOres()
	{
		for (Biome biome : Registry.BIOME)
		{
			if (!biome.getCategory().equals(Category.NETHER) && !biome.getCategory().equals(Category.THEEND))
			{
				biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, BLUNITE, 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
				biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, CARBONITE, 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
				biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, VANADIUM_ORE, 6)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 16))));
			}
		}
	}

	public static void registerTrees()
	{
		for (Biome biome : Registry.BIOME)
		{
			if (biome.getCategory().equals(Category.DESERT))
			{
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.TREE.configure(MubbleFeatureConfigs.PALM_TREE).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.12F, 1))));
			}
		}
	}

	public static void registerSpawns()
	{
		for (Biome biome : Registry.BIOME)
		{
			if (biome.getCategory().equals(Category.PLAINS))
			{
				biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.TOAD, 10, 4, 4));
			}
		}
		for (Biome biome : DuckEntity.getSpawnBiomes())
		{
			biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.DUCK, 10, 4, 4));
		}
	}
	
	/*
	private static boolean canEntitySpawnInBiome(EntityType<?> entity, Biome biome)
	{
		return biome.getSpawns(entity.getClassification()).stream().anyMatch(entry -> entry.entityType == entity);
	}
	*/
}