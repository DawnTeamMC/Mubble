package hugman.mubble.init.world;

public class MubbleGenerators {
	/*
	public static void handleBiome(Biome biome) {
		if(!biome.getCategory().equals(Category.NETHER) && !biome.getCategory().equals(Category.THEEND)) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.BLUNITE_BLOCKS.getBlock(BlockTemplate.CUBE).getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.CARBONITE_BLOCKS.getBlock(BlockTemplate.CUBE).getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, MubbleBlocks.VANADIUM_ORE.getBlock().getDefaultState(), 6)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(1, 0, 0, 16))));
		}
		if(biome.getCategory().equals(Category.DESERT)) {
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.TREE.configure(MubbleConfiguredFeatures.PALM_TREE).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.12F, 1))));
		}
		if(biome.getCategory().equals(Category.PLAINS)) {
			biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.TOAD, 10, 4, 4));
		}
		if(DuckEntity.getSpawnBiomes().contains(biome)) {
			biome.getEntitySpawnList(SpawnGroup.CREATURE).add(new Biome.SpawnEntry(MubbleEntities.DUCK, 10, 4, 4));
		}
		Biomes.END_MIDLANDS.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, MubbleFeatures.ENDER_BOULDER.configure(new ForestRockFeatureConfig(Blocks.OBSIDIAN.getDefaultState(), 0)).createDecoratedFeature(Decorator.FOREST_ROCK.configure(new CountDecoratorConfig(3))));
	}

	public static void init() {
		Registry.BIOME.forEach(MubbleGenerators::handleBiome);
		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> MubbleGenerators.handleBiome(biome));
	}

	 */
}