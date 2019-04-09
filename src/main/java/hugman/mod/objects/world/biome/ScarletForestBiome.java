package hugman.mod.objects.world.biome;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBiomes;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.LakesConfig;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.RandomDefaultFeatureListConfig;
import net.minecraft.world.gen.feature.TallGrassConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.DungeonRoomConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.LakeChanceConfig;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;

public class ScarletForestBiome extends Biome
{
	public ScarletForestBiome()
	{
		super((new Biome.BiomeBuilder()).surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE)).precipitation(Biome.RainType.RAIN).category(Biome.Category.FOREST).depth(0.1F).scale(0.2F).temperature(0.7F).downfall(0.8F).waterColor(4159204).waterFogColor(329011).parent((String)null));
		this.addCarver(GenerationStage.Carving.AIR, createWorldCarverWrapper(CAVE_WORLD_CARVER, new ProbabilityConfig(0.14285715F)));
		this.addCarver(GenerationStage.Carving.AIR, createWorldCarverWrapper(CANYON_WORLD_CARVER, new ProbabilityConfig(0.02F)));
		this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, createCompositeFeature(Feature.LAKES, new LakesConfig(Blocks.WATER), LAKE_WATER, new LakeChanceConfig(4)));
		this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, createCompositeFeature(Feature.LAKES, new LakesConfig(Blocks.LAVA), LAVA_LAKE, new LakeChanceConfig(80)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, createCompositeFeature(Feature.DUNGEONS, IFeatureConfig.NO_FEATURE_CONFIG, DUNGEON_ROOM, new DungeonRoomConfig(8)));
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.RANDOM_FEATURE_LIST, new RandomDefaultFeatureListConfig(new Feature[]{MubbleFeatures.LARGE_SCARLET_TREE, MubbleFeatures.TALL_SCARLET_TREE, MubbleFeatures.SCARLET_SHRUB}, new IFeatureConfig[]{IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG}, new float[]{0.6666667F, 0.1F, 0.1F}, MubbleFeatures.SCARLET_TREE, IFeatureConfig.NO_FEATURE_CONFIG), ROOFED_TREE, IPlacementConfig.NO_PLACEMENT_CONFIG));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.DIRT.getDefaultState(), 33), COUNT_RANGE, new CountRangeConfig(10, 0, 0, 256)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.GRAVEL.getDefaultState(), 33), COUNT_RANGE, new CountRangeConfig(8, 0, 0, 256)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.GRANITE.getDefaultState(), 33), COUNT_RANGE, new CountRangeConfig(10, 0, 0, 80)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.DIORITE.getDefaultState(), 33), COUNT_RANGE, new CountRangeConfig(10, 0, 0, 80)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.ANDESITE.getDefaultState(), 33), COUNT_RANGE, new CountRangeConfig(10, 0, 0, 80)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.COAL_ORE.getDefaultState(), 17), COUNT_RANGE, new CountRangeConfig(20, 0, 0, 128)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.IRON_ORE.getDefaultState(), 9), COUNT_RANGE, new CountRangeConfig(20, 0, 0, 64)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.GOLD_ORE.getDefaultState(), 9), COUNT_RANGE, new CountRangeConfig(2, 0, 0, 32)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.REDSTONE_ORE.getDefaultState(), 8), COUNT_RANGE, new CountRangeConfig(8, 0, 0, 16)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.DIAMOND_ORE.getDefaultState(), 8), COUNT_RANGE, new CountRangeConfig(1, 0, 0, 16)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, Blocks.LAPIS_ORE.getDefaultState(), 7), DEPTH_AVERAGE, new DepthAverageConfig(1, 16, 16)));
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFlowerFeature(MubbleFeatures.SCARLET_FLOWERS, SURFACE_PLUS_32, new FrequencyConfig(4)));
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.TALL_GRASS, new TallGrassConfig(Blocks.GRASS.getDefaultState()), TWICE_SURFACE, new FrequencyConfig(2)));
	    this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.TALL_GRASS, new TallGrassConfig(MubbleBlocks.SCARLET_LEAVES_CARPET.getDefaultState()), TWICE_SURFACE, new FrequencyConfig(2)));
	    this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.BUSH, new BushConfig(MubbleBlocks.SCARLET_MUSHROOM), TWICE_SURFACE_WITH_CHANCE, new ChanceConfig(12)));
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.REED, IFeatureConfig.NO_FEATURE_CONFIG, TWICE_SURFACE, new FrequencyConfig(10)));
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.PUMPKIN, IFeatureConfig.NO_FEATURE_CONFIG, TWICE_SURFACE_WITH_CHANCE, new ChanceConfig(32)));
		this.addSpawn(EnumCreatureType.CREATURE, new Biome.SpawnListEntry(EntityType.SHEEP, 12, 4, 4));
		this.addSpawn(EnumCreatureType.CREATURE, new Biome.SpawnListEntry(EntityType.PIG, 10, 4, 4));
		this.addSpawn(EnumCreatureType.CREATURE, new Biome.SpawnListEntry(EntityType.CHICKEN, 10, 4, 4));
		this.addSpawn(EnumCreatureType.CREATURE, new Biome.SpawnListEntry(EntityType.COW, 8, 4, 4));
		this.addSpawn(EnumCreatureType.AMBIENT, new Biome.SpawnListEntry(EntityType.BAT, 10, 8, 8));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.SPIDER, 100, 4, 4));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE, 95, 4, 4));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.SKELETON, 100, 4, 4));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.CREEPER, 100, 4, 4));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 100, 4, 4));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4));
		this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.WITCH, 5, 1, 1));
		
        this.setRegistryName(Mubble.MOD_ID, "scarlet_forest");
        MubbleBiomes.register(this);
	}
	
	@Override
	public int getGrassColor(BlockPos pos)
	{
		return 8720466;
	}
	
	@Override
	public int getFoliageColor(BlockPos pos)
	{
		return 10622269;
	}
}