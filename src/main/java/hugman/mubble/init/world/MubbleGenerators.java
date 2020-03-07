package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.objects.entity.DuckEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleGenerators
{
	private static final BlockState BLUNITE = MubbleBlocks.BLUNITE.getDefaultState();
	private static final BlockState CARBONITE = MubbleBlocks.CARBONITE.getDefaultState();
	private static final BlockState VANADIUM_ORE = MubbleBlocks.VANADIUM_ORE.getDefaultState();
	
	public static void registerOres()
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			if(!biome.getCategory().equals(Category.NETHER) && !biome.getCategory().equals(Category.THEEND))
			{
				biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BLUNITE, 33)).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
				biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, CARBONITE, 33)).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
				biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, VANADIUM_ORE, 6)).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 16))));
			}
		}
	}
	
	public static void registerTrees()
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			if(biome.getCategory().equals(Category.DESERT))
			{
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MubbleFeatures.PALM_TREE.configure(MubbleFeatureConfigs.PALM_TREE_CONFIG).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(0, 0.12F, 1))));
			}
		}
	}
	
	public static void registerSpawns()
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			if(biome.getCategory().equals(Category.PLAINS))
			{
				biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(MubbleEntities.TOAD, 10, 4, 4));
			}
		}
		for(Biome biome : DuckEntity.getSpawnBiomes())
		{
			biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(MubbleEntities.DUCK, 10, 4, 4));
		}
	}
	
	/*
	private static boolean canEntitySpawnInBiome(EntityType<?> entity, Biome biome)
	{
		return biome.getSpawns(entity.getClassification()).stream().anyMatch(entry -> entry.entityType == entity);
	}
	*/
}