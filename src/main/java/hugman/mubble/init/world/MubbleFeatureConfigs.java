package hugman.mubble.init.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.gen.feature.HugeNetherMushroomFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.gen.decorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.*;

import java.util.OptionalInt;

public class MubbleFeatureConfigs {
	private static final BlockState OAK_LOG = Blocks.OAK_LOG.getDefaultState();
	private static final BlockState BIRCH_LOG = Blocks.BIRCH_LOG.getDefaultState();

	private static final BlockState AUTUMN_OAK_LEAVES = MubbleBlocks.AUTUMN_OAK_LEAVES.getDefaultState();
	private static final BlockState AUTUMN_OAK_LEAF_PILE = MubbleBlocks.AUTUMN_OAK_LEAF_PILE.getDefaultState();
	private static final BlockState AUTUMN_BIRCH_LEAVES = MubbleBlocks.AUTUMN_BIRCH_LEAVES.getDefaultState();
	private static final BlockState AUTUMN_BIRCH_LEAF_PILE = MubbleBlocks.AUTUMN_BIRCH_LEAF_PILE.getDefaultState();

	private static final BlockState CHERRY_OAK_LOG = MubbleBlocks.CHERRY_OAK_LOG.getDefaultState();
	private static final BlockState PINK_CHERRY_OAK_LEAVES = MubbleBlocks.PINK_CHERRY_OAK_LEAVES.getDefaultState();
	private static final BlockState PINK_CHERRY_OAK_LEAF_PILE = MubbleBlocks.PINK_CHERRY_OAK_LEAF_PILE.getDefaultState();
	private static final BlockState WHITE_CHERRY_OAK_LEAVES = MubbleBlocks.WHITE_CHERRY_OAK_LEAVES.getDefaultState();
	private static final BlockState WHITE_CHERRY_OAK_LEAF_PILE = MubbleBlocks.WHITE_CHERRY_OAK_LEAF_PILE.getDefaultState();

	private static final BlockState PRESS_GARDEN_LOG = MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState();
	private static final BlockState RED_PRESS_GARDEN_LEAVES = MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getDefaultState();
	private static final BlockState RED_PRESS_GARDEN_LEAF_PILE = MubbleBlocks.RED_PRESS_GARDEN_LEAF_PILE.getDefaultState();
	private static final BlockState PINK_PRESS_GARDEN_LEAVES = MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getDefaultState();
	private static final BlockState PINK_PRESS_GARDEN_LEAF_PILE = MubbleBlocks.PINK_PRESS_GARDEN_LEAF_PILE.getDefaultState();

	private static final BlockState SCARLET_LOG = MubbleBlocks.SCARLET_LOG.getDefaultState();
	private static final BlockState SCARLET_LEAVES = MubbleBlocks.SCARLET_LEAVES.getDefaultState();
	private static final BlockState SCARLET_LEAF_PILE = MubbleBlocks.SCARLET_LEAF_PILE.getDefaultState();
	private static final BlockState SCARLET_ORCHID = MubbleBlocks.SCARLET_ORCHID.getDefaultState();

	private static final BlockState PALM_LOG = MubbleBlocks.PALM_LOG.getDefaultState();
	private static final BlockState PALM_LEAVES = MubbleBlocks.PALM_LEAVES.getDefaultState();

	private static final BlockState BLUEBERRY_BUSH = MubbleBlocks.BLUEBERRY_BUSH.getDefaultState();

	public static final BeehiveTreeDecorator VERY_RARE_BEEHIVES_DECORATOR = new BeehiveTreeDecorator(0.002F);
	public static final BeehiveTreeDecorator REGULAR_BEEHIVES_DECORATOR = new BeehiveTreeDecorator(0.02F);
	public static final BeehiveTreeDecorator MORE_BEEHIVES_DECORATOR = new BeehiveTreeDecorator(0.05F);

	/* MINECRAFT */
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

	public static final RandomPatchFeatureConfig BLUEBERRY_BUSH_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BLUEBERRY_BUSH), SimpleBlockPlacer.field_24871)).tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build();

	public static final RandomPatchFeatureConfig BLUE_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.BLUE_MUSHROOM);
	public static final RandomPatchFeatureConfig CYAN_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.CYAN_MUSHROOM);
	public static final RandomPatchFeatureConfig YELLOW_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.YELLOW_MUSHROOM);
	public static final RandomPatchFeatureConfig ORANGE_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.ORANGE_MUSHROOM);
	public static final RandomPatchFeatureConfig PINK_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.PINK_MUSHROOM);
	public static final RandomPatchFeatureConfig MAGENTA_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.LIGHT_GRAY_MUSHROOM);
	public static final RandomPatchFeatureConfig WHITE_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.WHITE_MUSHROOM);
	public static final RandomPatchFeatureConfig LIGHT_GRAY_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.LIGHT_GRAY_MUSHROOM);
	public static final RandomPatchFeatureConfig GRAY_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.GRAY_MUSHROOM);
	public static final RandomPatchFeatureConfig BLACK_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.BLACK_MUSHROOM);

	public static final HugeNetherMushroomFeatureConfig BLUE_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(13, 8, MubbleBlocks.BLUE_MUSHROOM_BLOCK.getDefaultState(), 3, 3, Blocks.SHROOMLIGHT.getDefaultState(), 0.1F, 0.05F);
	public static final HugeNetherMushroomFeatureConfig CYAN_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 3, MubbleBlocks.CYAN_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.1F);
	public static final HugeNetherMushroomFeatureConfig PINK_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(5, 4, MubbleBlocks.PINK_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.1F);
	public static final HugeNetherMushroomFeatureConfig YELLOW_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 3, MubbleBlocks.YELLOW_MUSHROOM_BLOCK.getDefaultState(), 3, 0, Blocks.SHROOMLIGHT.getDefaultState(), 0.1F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig BROWN_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 3, Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), 3, 0, Blocks.SHROOMLIGHT.getDefaultState(), 0.1F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig WHITE_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(6, 2, MubbleBlocks.WHITE_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig LIGHT_GRAY_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 7, MubbleBlocks.LIGHT_GRAY_MUSHROOM_BLOCK.getDefaultState(), 3, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.05F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig GRAY_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(4, 7, MubbleBlocks.GRAY_MUSHROOM_BLOCK.getDefaultState(), 3, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.01F, 0.0F);
	public static final HugeNetherMushroomFeatureConfig BLACK_HUGE_NETHER_MUSHROOM = new HugeNetherMushroomFeatureConfig(6, 2, MubbleBlocks.BLACK_MUSHROOM_BLOCK.getDefaultState(), 2, 1, Blocks.SHROOMLIGHT.getDefaultState(), 0.01F, 0.0F);

	public static final HugeFungusFeatureConfig AMARANTH_FUNGUS_CONFIG = new HugeFungusFeatureConfig(MubbleBlocks.AMARANTH_DYLIUM.getDefaultState(), MubbleBlocks.DARK_AMARANTH_STEM.getDefaultState(), MubbleBlocks.AMARANTH_WART_BLOCK.getDefaultState(), Blocks.COBWEB.getDefaultState(), true);;
	public static final HugeFungusFeatureConfig AMARANTH_FUNGUS_NOT_PLANTED_CONFIG = new HugeFungusFeatureConfig(AMARANTH_FUNGUS_CONFIG.validBaseBlock, AMARANTH_FUNGUS_CONFIG.stemState, AMARANTH_FUNGUS_CONFIG.hatState, AMARANTH_FUNGUS_CONFIG.decorationState, false);

	public static final BlockPileFeatureConfig AMARANTH_ROOTS_CONFIG = new BlockPileFeatureConfig((new WeightedBlockStateProvider()).addState(MubbleBlocks.AMARANTH_ROOTS.getDefaultState(), 87).addState(MubbleBlocks.DARK_AMARANTH_FUNGUS.getDefaultState(), 11));;

	/* SONIC */
	public static final TreeFeatureConfig RED_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig FANCY_RED_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig MEGA_RED_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAVES), new JungleFoliagePlacer(2, 0, 0, 0, 2), new MegaJungleTrunkPlacer(30, 2, 20), new TwoLayersFeatureSize(1, 1, 2))).build();
	public static final RandomPatchFeatureConfig RED_PRESS_GARDEN_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();
	public static final TreeFeatureConfig PINK_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig FANCY_PINK_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig MEGA_PINK_PRESS_GARDEN_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new JungleFoliagePlacer(2, 0, 0, 0, 2), new MegaJungleTrunkPlacer(30, 2, 20), new TwoLayersFeatureSize(1, 1, 2))).build();
	public static final RandomPatchFeatureConfig PINK_PRESS_GARDEN_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();
	public static final TreeFeatureConfig PINK_PRESS_GARDEN_GROUND_BUSH_PATCHES = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new BushFoliagePlacer(2, 0, 1, 0, 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).heightmap(Type.MOTION_BLOCKING_NO_LEAVES).build();

	/* DELTARUNE */
	public static final TreeFeatureConfig SCARLET_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build();
	public static final TreeFeatureConfig FANCY_SCARLET_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new LargeOakFoliagePlacer(2, 0, 4, 0, 4), new LargeOakTrunkPlacer(3, 11, 0), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().heightmap(Type.MOTION_BLOCKING).build();
	public static final TreeFeatureConfig HUGE_SCARLET_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new DarkOakFoliagePlacer(0, 0, 0, 0), new DarkOakTrunkPlacer(6, 2, 1), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).maxWaterDepth(2147483647).heightmap(Type.MOTION_BLOCKING).ignoreVines().build();
	public static final RandomPatchFeatureConfig SCARLET_LEAF_PILE_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LEAF_PILE), SimpleBlockPlacer.field_24871)).tries(32).build();
	public static final TreeFeatureConfig SCARLET_GROUND_BUSH = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new BushFoliagePlacer(2, 0, 1, 0, 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayersFeatureSize(0, 0, 0))).heightmap(Type.MOTION_BLOCKING_NO_LEAVES).build();
	public static final RandomPatchFeatureConfig SCARLET_MUSHROOM_PATCHES = mushroomConfig(MubbleBlocks.SCARLET_MUSHROOM);
	public static final RandomPatchFeatureConfig SCARLET_ORCHID_PATCHES = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_ORCHID), SimpleBlockPlacer.field_24871)).tries(64).build();

	public static RandomPatchFeatureConfig mushroomConfig(Block block) {
		return (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(block.getDefaultState()), SimpleBlockPlacer.field_24871)).tries(64).cannotProject().build();
	}
}
