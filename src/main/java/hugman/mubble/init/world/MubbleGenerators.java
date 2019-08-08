package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRange;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleGenerators
{
	public static void initOres()
	{
		for (Biome biome : ForgeRegistries.BIOMES)
		{
			if (!biome.getCategory().equals(Category.NETHER) && !biome.getCategory().equals(Category.THEEND))
			{
				biome.addFeature
				(
					Decoration.UNDERGROUND_ORES, 
					Biome.createDecoratedFeature
					(
						Feature.ORE, 
						new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MubbleBlocks.BLUNITE.getDefaultState(), 33),
						new CountRange(CountRangeConfig::deserialize),
						new CountRangeConfig(10, 0, 0, 80)
					)
				);
				
				biome.addFeature
				(
					Decoration.UNDERGROUND_ORES, 
					Biome.createDecoratedFeature
					(
						Feature.ORE, 
						new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MubbleBlocks.CARBONITE.getDefaultState(), 33),
						new CountRange(CountRangeConfig::deserialize),
						new CountRangeConfig(10, 0, 0, 80)
					)
				);
				
				biome.addFeature
				(
					Decoration.UNDERGROUND_ORES, 
					Biome.createDecoratedFeature
					(
						Feature.ORE, 
						new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, MubbleBlocks.VANADIUM_ORE.getDefaultState(), 6),
						new CountRange(CountRangeConfig::deserialize),
						new CountRangeConfig(1, 0, 0, 16)
					)
				);
			}
		}
	}
	
	public static void initTrees()
	{
		for (Biome biome : ForgeRegistries.BIOMES)
		{
			if (biome.getCategory().equals(Category.DESERT))
			{
				biome.addFeature
				(
					Decoration.VEGETAL_DECORATION, 
					Biome.createDecoratedFeature
					(
						MubbleFeatures.PALM_TREE, 
						IFeatureConfig.NO_FEATURE_CONFIG,
						Placement.COUNT_EXTRA_HEIGHTMAP,
						new AtSurfaceWithExtraConfig(0, 0.12F, 1)
					)
				);
			}
		}
	}
	
	public static void initSpawns()
	{
		for (Biome biome : ForgeRegistries.BIOMES)
		{
			if (!biome.getCategory().equals(Category.PLAINS))
			{
				biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(MubbleEntities.TOAD, 10, 4, 4));
			}
		}
	}
}