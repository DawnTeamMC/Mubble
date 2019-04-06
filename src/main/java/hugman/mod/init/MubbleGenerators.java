package hugman.mod.init;

import hugman.mod.objects.world.structure.PalmTreeFeature;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRange;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleGenerators
{
	public static final AbstractTreeFeature<NoFeatureConfig> PALM_TREE = new PalmTreeFeature(false);
	public static void init()
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
			biome.getFeatures(Decoration.VEGETAL_DECORATION).removeAll(biome.getFeatures(Decoration.VEGETAL_DECORATION));
			biome.getFeatures(Decoration.LOCAL_MODIFICATIONS).removeAll(biome.getFeatures(Decoration.LOCAL_MODIFICATIONS));
			biome.getFeatures(Decoration.UNDERGROUND_DECORATION).removeAll(biome.getFeatures(Decoration.UNDERGROUND_DECORATION));
			biome.getFeatures(Decoration.UNDERGROUND_ORES).removeAll(biome.getFeatures(Decoration.UNDERGROUND_ORES));
			biome.getFeatures(Decoration.TOP_LAYER_MODIFICATION).removeAll(biome.getFeatures(Decoration.TOP_LAYER_MODIFICATION));
			biome.getSpawns(EnumCreatureType.AMBIENT).removeAll(biome.getSpawns(EnumCreatureType.AMBIENT));
			biome.getSpawns(EnumCreatureType.CREATURE).removeAll(biome.getSpawns(EnumCreatureType.CREATURE));
			biome.getSpawns(EnumCreatureType.MONSTER).removeAll(biome.getSpawns(EnumCreatureType.MONSTER));
			biome.getSpawns(EnumCreatureType.WATER_CREATURE).removeAll(biome.getSpawns(EnumCreatureType.WATER_CREATURE));
			biome.getSpawns(EnumCreatureType.CREATURE).add(new Biome.SpawnListEntry(MubbleEntities.TOAD, 100, 8, 8));
			biome.addFeature
			(
				Decoration.VEGETAL_DECORATION, 
				Biome.createCompositeFeature
				(
					Feature.MELON,
					IFeatureConfig.NO_FEATURE_CONFIG,
					Biome.TWICE_SURFACE,
					new FrequencyConfig(25)
				)
			);
			biome.addFeature
			(
				Decoration.UNDERGROUND_ORES, 
				Biome.createCompositeFeature
				(
					Feature.MINABLE, 
					new MinableConfig(MinableConfig.IS_ROCK, MubbleBlocks.SPACE_JAM.getDefaultState(), 50), 
					new CountRange(),
					new CountRangeConfig(666, 0, 0, 200)
				)
			);
		}
	}
}