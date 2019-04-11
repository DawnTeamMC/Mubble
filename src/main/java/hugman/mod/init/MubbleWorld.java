package hugman.mod.init;

import hugman.mod.objects.world.carver.PermafrostCaveWorldCarver;
import hugman.mod.objects.world.feature.ScarletFlowersFeature;
import hugman.mod.objects.world.feature.tree.autumn_oak.AutumnOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.pink.PinkCherryOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.pink.PinkCherryOakTreeTallFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.white.WhiteCherryOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.white.WhiteCherryOakTreeTallFeature;
import hugman.mod.objects.world.feature.tree.palm.PalmTreeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeLargeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeTallFeature;
import hugman.mod.objects.world.surface_builder.PermafrostSurfaceBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.ShrubFeature;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.CountRange;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;

public class MubbleWorld
{
	public static class Carvers
	{
		public static final WorldCarver<ProbabilityConfig> PERMAFROST_CAVE_WORLD_CARVER = new PermafrostCaveWorldCarver();
	}
	
	public static class SurfaceBuilder
	{
		protected static final IBlockState PERMAROCK = MubbleBlocks.PERMAROCK.getDefaultState();
		
		public static final ISurfaceBuilder<SurfaceBuilderConfig> PERMAFROST_SURFACE_BUILDER = new PermafrostSurfaceBuilder();
		public static final SurfaceBuilderConfig PERMAROCK_SURFACE = new SurfaceBuilderConfig(PERMAROCK, PERMAROCK, PERMAROCK);
	}
	
	public static class Features
	{
		public static final AbstractTreeFeature<NoFeatureConfig> PALM_TREE = new PalmTreeFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_TREE = new ScarletTreeFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> TALL_SCARLET_TREE = new ScarletTreeTallFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> LARGE_SCARLET_TREE = new ScarletTreeLargeFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> AUTUMN_OAK_TREE = new AutumnOakTreeFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> TALL_AUTUMN_OAK_TREE = new AutumnOakTreeFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> PINK_CHERRY_OAK_TREE = new PinkCherryOakTreeFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> TALL_PINK_CHERRY_OAK_TREE = new PinkCherryOakTreeTallFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> WHITE_CHERRY_OAK_TREE = new WhiteCherryOakTreeFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> TALL_WHITE_CHERRY_OAK_TREE = new WhiteCherryOakTreeTallFeature(false);
		public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_SHRUB = new ShrubFeature(MubbleBlocks.SCARLET_LOG.getDefaultState(), MubbleBlocks.SCARLET_LEAVES.getDefaultState()).setSapling((IPlantable)MubbleBlocks.SCARLET_SAPLING);
		public static final AbstractFlowersFeature SCARLET_FLOWERS = new ScarletFlowersFeature();
	}
	
	public static class Generators
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
							MubbleWorld.Features.PALM_TREE, 
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
}