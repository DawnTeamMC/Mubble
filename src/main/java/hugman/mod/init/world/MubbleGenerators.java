package hugman.mod.init.world;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleEntities;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRange;
import net.minecraft.world.gen.placement.CountRangeConfig;
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
					Biome.createCompositeFeature
					(
						Feature.MINABLE, 
						new MinableConfig(MinableConfig.IS_ROCK, MubbleBlocks.BLUNITE.getDefaultState(), 33), 
						new CountRange(),
						new CountRangeConfig(10, 0, 0, 80)
					)
				);
				
				biome.addFeature
				(
					Decoration.UNDERGROUND_ORES, 
					Biome.createCompositeFeature
					(
						Feature.MINABLE, 
						new MinableConfig(MinableConfig.IS_ROCK, MubbleBlocks.CARBONITE.getDefaultState(), 33), 
						new CountRange(),
						new CountRangeConfig(10, 0, 0, 80)
					)
				);
				
				biome.addFeature
				(
					Decoration.UNDERGROUND_ORES, 
					Biome.createCompositeFeature
					(
						Feature.MINABLE, 
						new MinableConfig(MinableConfig.IS_ROCK, MubbleBlocks.VANADIUM_ORE.getDefaultState(), 6), 
						new CountRange(),
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
				biome.addFeature(
					Decoration.VEGETAL_DECORATION, 
					Biome.createCompositeFeature
					(
						MubbleFeatures.PALM_TREE, 
						IFeatureConfig.NO_FEATURE_CONFIG,
						Biome.AT_SURFACE_WITH_EXTRA,
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
				biome.getSpawns(EnumCreatureType.CREATURE).add(new Biome.SpawnListEntry(MubbleEntities.TOAD, 10, 4, 4));
			}
		}
	}
}