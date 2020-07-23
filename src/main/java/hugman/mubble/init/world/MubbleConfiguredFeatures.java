package hugman.mubble.init.world;

public class MubbleConfiguredFeatures {
	/*
	public static final TreeFeatureConfig AUTUMN_OAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig AUTUMN_OAK_TREE_B1 = AUTUMN_OAK_TREE.setTreeDecorators(ImmutableList.of(REGULAR_BEEHIVES_DECORATOR));
	public static final TreeFeatureConfig FANCY_AUTUMN_OAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(AUTUMN_OAK_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig FANCY_AUTUMN_OAK_TREE_B1 = FANCY_AUTUMN_OAK_TREE.setTreeDecorators(ImmutableList.of(REGULAR_BEEHIVES_DECORATOR));
	public static final RandomPatchFeatureConfig AUTUMN_OAK_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(AUTUMN_OAK_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(16).build();

	public static final TreeFeatureConfig AUTUMN_BIRCH_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BIRCH_LOG), new SimpleBlockStateProvider(AUTUMN_BIRCH_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig AUTUMN_BIRCH_TREE_B1 = AUTUMN_BIRCH_TREE.setTreeDecorators(ImmutableList.of(REGULAR_BEEHIVES_DECORATOR));
	public static final TreeFeatureConfig FANCY_AUTUMN_BIRCH_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BIRCH_LOG), new SimpleBlockStateProvider(AUTUMN_BIRCH_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig FANCY_AUTUMN_BIRCH_TREE_B1_CONFIG = FANCY_AUTUMN_BIRCH_TREE.setTreeDecorators(ImmutableList.of(REGULAR_BEEHIVES_DECORATOR));
	public static final RandomPatchFeatureConfig AUTUMN_BIRCH_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(AUTUMN_BIRCH_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(16).build();

	public static final TreeFeatureConfig PINK_CHERRY_OAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(PINK_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig PINK_CHERRY_OAK_TREE_B1 = PINK_CHERRY_OAK_TREE.setTreeDecorators(ImmutableList.of(MORE_BEEHIVES_DECORATOR));
	public static final TreeFeatureConfig FANCY_PINK_CHERRY_OAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(PINK_CHERRY_OAK_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(4, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig FANCY_PINK_CHERRY_OAK_TREE_B1 = FANCY_PINK_CHERRY_OAK_TREE.setTreeDecorators(ImmutableList.of(MORE_BEEHIVES_DECORATOR));
	public static final RandomPatchFeatureConfig PINK_CHERRY_OAK_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(PINK_CHERRY_OAK_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();

	public static final TreeFeatureConfig WHITE_CHERRY_OAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(WHITE_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig WHITE_CHERRY_OAK_TREE_B1 = WHITE_CHERRY_OAK_TREE.setTreeDecorators(ImmutableList.of(MORE_BEEHIVES_DECORATOR));
	public static final TreeFeatureConfig FANCY_WHITE_CHERRY_OAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(WHITE_CHERRY_OAK_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(4, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig FANCY_WHITE_CHERRY_OAK_TREE_B1 = FANCY_WHITE_CHERRY_OAK_TREE.setTreeDecorators(ImmutableList.of(MORE_BEEHIVES_DECORATOR));
	public static final RandomPatchFeatureConfig WHITE_CHERRY_OAK_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(WHITE_CHERRY_OAK_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();

	public static final TreeFeatureConfig PALM_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PALM_LOG), new SimpleBlockStateProvider(PALM_LEAVES), new AcaciaFoliagePlacer(2, 0, 0, 0), new ForkingTrunkPlacer(16, 2, 2), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();

	public static final ConfiguredFeature<?, ?> BLUEBERRY_BUSH_PATCHES = register("patch_blueberry_bush", Configs.berryBush(States.BLUEBERRY_BUSH));
	public static final ConfiguredFeature<?, ?> BLUEBERRY_BUSH_PATCHES = register("patch_blueberry_bush", Configs.berryBush(States.BLUEBERRY_BUSH));

	public static final ConfiguredFeature<?, ?> PATCH_BLUE_MUSHROOM = register("patch_blue_mushroom", Configs.mushroom(States.BLUE_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_CYAN_MUSHROOM = register("patch_cyan_mushroom", Configs.mushroom(States.CYAN_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_YELLOW_MUSHROOM = register("patch_yellow_mushroom", Configs.mushroom(States.YELLOW_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_ORANGE_MUSHROOM = register("patch_orange_mushroom", Configs.mushroom(States.ORANGE_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_PINK_MUSHROOM = register("patch_pink_mushroom", Configs.mushroom(States.PINK_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_MAGENTA_MUSHROOM = register("patch_magenta_mushroom", Configs.mushroom(States.MAGENTA_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_WHITE_MUSHROOM = register("patch_white_mushroom", Configs.mushroom(States.WHITE_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_LIGHT_GRAY_MUSHROOM = register("patch_light_gray_mushroom", Configs.mushroom(States.LIGHT_GRAY_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_GRAY_MUSHROOM = register("patch_gray_mushroom", Configs.mushroom(States.GRAY_MUSHROOM));
	public static final ConfiguredFeature<?, ?> PATCH_BLACK_MUSHROOM = register("patch_black_mushroom", Configs.mushroom(States.SCARLET_MUSHROOM));

	public static final ConfiguredFeature<?, ?> PATCH_BLUE_MUSHROOM_NETHER = register("patch_blue_mushroom_nether", PATCH_BLUE_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_CYAN_MUSHROOM_NETHER = register("patch_cyan_mushroom_nether", PATCH_CYAN_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_YELLOW_MUSHROOM_NETHER = register("patch_yellow_mushroom_nether", PATCH_YELLOW_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_ORANGE_MUSHROOM_NETHER = register("patch_orange_mushroom_nether", PATCH_ORANGE_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_PINK_MUSHROOM_NETHER = register("patch_pink_mushroom_nether", PATCH_PINK_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_MAGENTA_MUSHROOM_NETHER = register("patch_magenta_mushroom_nether", PATCH_MAGENTA_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_WHITE_MUSHROOM_NETHER = register("patch_white_mushroom_nether", PATCH_WHITE_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_LIGHT_GRAY_MUSHROOM_NETHER = register("patch_light_gray_mushroom_nether", PATCH_LIGHT_GRAY_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_GRAY_MUSHROOM_NETHER = register("patch_gray_mushroom_nether", PATCH_GRAY_MUSHROOM.method_30377(128)).applyChance(2);
	public static final ConfiguredFeature<?, ?> PATCH_BLACK_MUSHROOM_NETHER = register("patch_black_mushroom_nether", PATCH_BLACK_MUSHROOM.method_30377(128)).applyChance(2);

	public static final HugeNetherMushroomFeatureConfig BLUE_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(13, 8, MubbleBlocks.BLUE_MUSHROOM_BLOCK.getDefaultState(), 3, 3, Blocks.SHROOMLIGHT.getDefaultState(), 0.1F, 0.05F);
	public static final HugeNetherMushroomFeatureConfig CYAN_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 3, MubbleBlocks.CYAN_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.1F);
	public static final HugeNetherMushroomFeatureConfig PINK_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(5, 4, MubbleBlocks.PINK_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.1F);
	public static final HugeNetherMushroomFeatureConfig YELLOW_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 3, MubbleBlocks.YELLOW_MUSHROOM_BLOCK.getDefaultState(), 3, 0, Blocks.SHROOMLIGHT.getDefaultState(), 0.1F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig BROWN_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 3, Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), 3, 0, Blocks.SHROOMLIGHT.getDefaultState(), 0.1F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig WHITE_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(6, 2, MubbleBlocks.WHITE_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig LIGHT_GRAY_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 7, MubbleBlocks.LIGHT_GRAY_MUSHROOM_BLOCK.getDefaultState(), 3, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig GRAY_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 7, MubbleBlocks.GRAY_MUSHROOM_BLOCK.getDefaultState(), 3, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.01F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig BLACK_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(6, 2, MubbleBlocks.BLACK_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.01F, 0.0F);

	public static final HugeFungusFeatureConfig AMARANTH_FUNGUS_CONFIG = new HugeFungusFeatureConfig(MubbleBlocks.AMARANTH_DYLIUM.getBlock().getDefaultState(), MubbleBlocks.DARK_AMARANTH_WOOD.getStem().getDefaultState(), MubbleBlocks.AMARANTH_WART_BLOCK.getBlock().getDefaultState(), Blocks.COBWEB.getDefaultState(), true);
	public static final HugeFungusFeatureConfig AMARANTH_FUNGUS_NOT_PLANTED_CONFIG = new HugeFungusFeatureConfig(AMARANTH_FUNGUS_CONFIG.validBaseBlock, AMARANTH_FUNGUS_CONFIG.stemState, AMARANTH_FUNGUS_CONFIG.hatState, AMARANTH_FUNGUS_CONFIG.decorationState, false);

	public static final BlockPileFeatureConfig AMARANTH_ROOTS_CONFIG = new BlockPileFeatureConfig((new WeightedBlockStateProvider()).addState(MubbleBlocks.AMARANTH_ROOTS.getBlock().getDefaultState(), 87).addState(MubbleBlocks.DARK_AMARANTH_WOOD.getFungus().getDefaultState(), 11));

	public static final TreeFeatureConfig RED_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.RED_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig FANCY_RED_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.PRESS_GARDEN_LOG), new SimpleBlockStateProvider(States.RED_PRESS_GARDEN_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig MEGA_RED_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAVES), new JungleFoliagePlacer(2, 0, 0, 0, 2), new MegaJungleTrunkPlacer(30, 2, 20), new TwoLayersFeatureSize(1, 1, 2))).build();
	public static final RandomPatchFeatureConfig RED_PRESS_GARDEN_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();
	public static final TreeFeatureConfig PINK_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig FANCY_PINK_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig MEGA_PINK_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new JungleFoliagePlacer(2, 0, 0, 0, 2), new MegaJungleTrunkPlacer(30, 2, 20), new TwoLayersFeatureSize(1, 1, 2))).build();
	public static final RandomPatchFeatureConfig PINK_PRESS_GARDEN_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();
	public static final TreeFeatureConfig PINK_PRESS_GARDEN_GROUND_BUSH_PATCHES = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new BushFoliagePlacer(2, 0, 1, 0, 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).heightmap(Type.MOTION_BLOCKING_NO_LEAVES).build();

	public static final TreeFeatureConfig SCARLET_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig FANCY_SCARLET_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig HUGE_SCARLET_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new DarkOakFoliagePlacer(0, 0, 0, 0), new DarkOakTrunkPlacer(6, 2, 1), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).maxWaterDepth(2147483647).heightmap(Type.MOTION_BLOCKING).ignoreVines().build();
	public static final RandomPatchFeatureConfig SCARLET_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(States.SCARLET_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();
	public static final TreeFeatureConfig SCARLET_GROUND_BUSH = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(States.SCARLET_LOG), new SimpleBlockStateProvider(States.SCARLET_LEAVES), new BushFoliagePlacer(2, 0, 1, 0, 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).heightmap(Type.MOTION_BLOCKING_NO_LEAVES).build();

	public static final ConfiguredFeature<?, ?> PATCH_SCARLET_MUSHROOM = register("patch_scarlet_mushroom", Configs.mushroom(States.SCARLET_MUSHROOM));
	public static final ConfiguredFeature<?, ?> SCARLET_MUSHROOM_NORMAL = register("scarlet_mushroom_normal", PATCH_SCARLET_MUSHROOM.decorate(ConfiguredFeatures.Decorators.field_26166).applyChance(4));
	public static final RandomPatchFeatureConfig SCARLET_ORCHID_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_ORCHID), SimpleBlockPlacer.field_24871)).tries(64).build();



	private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> feature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Mubble.MOD_ID, name), feature);
	}

	public static final class Configs {
		protected final static ConfiguredFeature<RandomPatchFeatureConfig, ?> berryBush(BlockState blockState)
		{
			return Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(blockState), SimpleBlockPlacer.INSTANCE)).tries(64).whitelist(ImmutableSet.of(States.GRASS_BLOCK.getBlock())).cannotProject().build());
		}
		protected final static ConfiguredFeature<RandomPatchFeatureConfig, ?> mushroom(BlockState blockState)
		{
			return Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(blockState), SimpleBlockPlacer.INSTANCE)).tries(64).cannotProject().build());
		}
	}

	public static final class States {
		protected static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();

		protected static final BlockState OAK_LOG = Blocks.OAK_LOG.getDefaultState();
		protected static final BlockState BIRCH_LOG = Blocks.BIRCH_LOG.getDefaultState();

		protected static final BlockState AUTUMN_OAK_LEAVES = MubbleBlocks.AUTUMN_OAK_LEAVES.getLeaves().getDefaultState();
		protected static final BlockState AUTUMN_OAK_LEAF_PILE = MubbleBlocks.AUTUMN_OAK_LEAVES.getLeafPile().getDefaultState();
		protected static final BlockState AUTUMN_BIRCH_LEAVES = MubbleBlocks.AUTUMN_BIRCH_LEAVES.getLeaves().getDefaultState();
		protected static final BlockState AUTUMN_BIRCH_LEAF_PILE = MubbleBlocks.AUTUMN_BIRCH_LEAVES.getLeafPile().getDefaultState();

		protected static final BlockState CHERRY_OAK_LOG = MubbleBlocks.CHERRY_OAK_LOG.getDefaultState();
		protected static final BlockState PINK_CHERRY_OAK_LEAVES = MubbleBlocks.PINK_CHERRY_OAK_LEAVES.getDefaultState();
		protected static final BlockState PINK_CHERRY_OAK_LEAF_PILE = MubbleBlocks.PINK_CHERRY_OAK_LEAF_PILE.getDefaultState();
		protected static final BlockState WHITE_CHERRY_OAK_LEAVES = MubbleBlocks.WHITE_CHERRY_OAK_LEAVES.getDefaultState();
		protected static final BlockState WHITE_CHERRY_OAK_LEAF_PILE = MubbleBlocks.WHITE_CHERRY_OAK_LEAF_PILE.getDefaultState();

		protected static final BlockState PRESS_GARDEN_LOG = MubbleBlocks.PRESS_GARDEN_WOOD.getLog().getDefaultState();
		protected static final BlockState RED_PRESS_GARDEN_LEAVES = MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getLeaves().getDefaultState();
		protected static final BlockState RED_PRESS_GARDEN_LEAF_PILE = MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getLeafPile().getDefaultState();
		protected static final BlockState PINK_PRESS_GARDEN_LEAVES = MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getLeaves().getDefaultState();
		protected static final BlockState PINK_PRESS_GARDEN_LEAF_PILE = MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getLeafPile().getDefaultState();

		protected static final BlockState SCARLET_LOG = MubbleBlocks.SCARLET_WOOD.getLog().getDefaultState();
		protected static final BlockState SCARLET_LEAVES = MubbleBlocks.SCARLET_WOOD.getLeaves().getDefaultState();
		protected static final BlockState SCARLET_LEAF_PILE = MubbleBlocks.SCARLET_WOOD.getLeafPile().getDefaultState();
		protected static final BlockState SCARLET_ORCHID = MubbleBlocks.SCARLET_ORCHID.getDefaultState();

		protected static final BlockState PALM_LOG = MubbleBlocks.PALM_WOOD.getLog().getDefaultState();
		protected static final BlockState PALM_LEAVES = MubbleBlocks.PALM_WOOD.getLeaves().getDefaultState();

		protected static final BlockState BLUEBERRY_BUSH = MubbleBlocks.BLUEBERRY_BUSH.getDefaultState();

		protected static final BlockState BLUE_MUSHROOM = MubbleBlocks.BLUE_MUSHROOM.getDefaultState();
		protected static final BlockState CYAN_MUSHROOM = MubbleBlocks.CYAN_MUSHROOM.getDefaultState();
		protected static final BlockState YELLOW_MUSHROOM = MubbleBlocks.YELLOW_MUSHROOM.getDefaultState();
		protected static final BlockState ORANGE_MUSHROOM = MubbleBlocks.ORANGE_MUSHROOM.getDefaultState();
		protected static final BlockState PINK_MUSHROOM = MubbleBlocks.PINK_MUSHROOM.getDefaultState();
		protected static final BlockState MAGENTA_MUSHROOM = MubbleBlocks.MAGENTA_MUSHROOM.getDefaultState();
		protected static final BlockState WHITE_MUSHROOM = MubbleBlocks.WHITE_MUSHROOM.getDefaultState();
		protected static final BlockState LIGHT_GRAY_MUSHROOM = MubbleBlocks.LIGHT_GRAY_MUSHROOM.getDefaultState();
		protected static final BlockState GRAY_MUSHROOM = MubbleBlocks.GRAY_MUSHROOM.getDefaultState();
		protected static final BlockState BLACK_MUSHROOM = MubbleBlocks.BLACK_MUSHROOM.getDefaultState();

		protected static final BlockState SCARLET_MUSHROOM = MubbleBlocks.SCARLET_MUSHROOM.getDefaultState();
	}
	*/
}
