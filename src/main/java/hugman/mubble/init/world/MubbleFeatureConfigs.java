package hugman.mubble.init.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.HugeTreeFeatureConfig;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraftforge.common.IPlantable;

public class MubbleFeatureConfigs
{
	private static final BlockState OAK_LOG = Blocks.OAK_LOG.getDefaultState();
	
	private static final BlockState AUTUMN_OAK_LEAVES = MubbleBlocks.AUTUMN_OAK_LEAVES.getDefaultState();
	private static final IPlantable AUTUMN_OAK_SAPLING = (IPlantable)(MubbleBlocks.AUTUMN_OAK_SAPLING);
	private static final BlockState AUTUMN_OAK_LEAF_PILE = MubbleBlocks.AUTUMN_OAK_LEAF_PILE.getDefaultState();
	
	private static final BlockState CHERRY_OAK_LOG = MubbleBlocks.CHERRY_OAK_LOG.getDefaultState();
	private static final BlockState PINK_CHERRY_OAK_LEAVES = MubbleBlocks.PINK_CHERRY_OAK_LEAVES.getDefaultState();
	private static final IPlantable PINK_CHERRY_OAK_SAPLING = (IPlantable)(MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	private static final BlockState PINK_CHERRY_OAK_LEAF_PILE = MubbleBlocks.PINK_CHERRY_OAK_LEAF_PILE.getDefaultState();
	private static final BlockState WHITE_CHERRY_OAK_LEAVES = MubbleBlocks.WHITE_CHERRY_OAK_LEAVES.getDefaultState();
	private static final IPlantable WHITE_CHERRY_OAK_SAPLING = (IPlantable)(MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	private static final BlockState WHITE_CHERRY_OAK_LEAF_PILE = MubbleBlocks.WHITE_CHERRY_OAK_LEAF_PILE.getDefaultState();
	
	private static final BlockState PRESS_GARDEN_LOG = MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState();
	private static final BlockState RED_PRESS_GARDEN_LEAVES = MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getDefaultState();
	private static final IPlantable RED_PRESS_GARDEN_SAPLING = (IPlantable)(MubbleBlocks.RED_PRESS_GARDEN_SAPLING);
	private static final BlockState RED_PRESS_GARDEN_LEAF_PILE = MubbleBlocks.RED_PRESS_GARDEN_LEAF_PILE.getDefaultState();
	private static final BlockState PINK_PRESS_GARDEN_LEAVES = MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getDefaultState();
	private static final IPlantable PINK_PRESS_GARDEN_SAPLING = (IPlantable)(MubbleBlocks.PINK_PRESS_GARDEN_SAPLING);
	private static final BlockState PINK_PRESS_GARDEN_LEAF_PILE = MubbleBlocks.PINK_PRESS_GARDEN_LEAF_PILE.getDefaultState();

	private static final BlockState SCARLET_LOG = MubbleBlocks.SCARLET_LOG.getDefaultState();
	private static final BlockState SCARLET_LEAVES = MubbleBlocks.SCARLET_LEAVES.getDefaultState();
	private static final IPlantable SCARLET_SAPLING = (IPlantable)(MubbleBlocks.SCARLET_SAPLING);
	private static final BlockState SCARLET_LEAF_PILE = MubbleBlocks.SCARLET_LEAF_PILE.getDefaultState();
	private static final BlockState SCARLET_ORCHID = MubbleBlocks.SCARLET_ORCHID.getDefaultState();

	private static final BlockState PALM_LOG = MubbleBlocks.PALM_LOG.getDefaultState();
	private static final BlockState PALM_LEAVES = MubbleBlocks.PALM_LEAVES.getDefaultState();
	private static final IPlantable PALM_SAPLING = (IPlantable)(MubbleBlocks.PALM_SAPLING);
	
	private static final BlockState BLUEBERRY_BUSH = MubbleBlocks.BLUEBERRY_BUSH.getDefaultState();
	
	/* MINECRAFT */
	public static final TreeFeatureConfig AUTUMN_OAK_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).noVines().setSapling(AUTUMN_OAK_SAPLING).build();
	public static final TreeFeatureConfig FANCY_AUTUMN_OAK_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).setSapling(AUTUMN_OAK_SAPLING).build();
	public static final TreeFeatureConfig AUTUMN_OAK_TREE_BEEHIVED_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).noVines().treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).setSapling(AUTUMN_OAK_SAPLING).build();
	public static final TreeFeatureConfig FANCY_AUTUMN_OAK_BEEHIVED_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).setSapling(AUTUMN_OAK_SAPLING).build();
	public static final BlockClusterFeatureConfig AUTUMN_OAK_LEAF_PILE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AUTUMN_OAK_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig YELLOW_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.YELLOW_MUSHROOM);
	public static final BlockClusterFeatureConfig ORANGE_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.ORANGE_MUSHROOM);
	
	public static final TreeFeatureConfig PINK_CHERRY_OAK_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(PINK_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().setSapling(PINK_CHERRY_OAK_SAPLING).build();
	public static final TreeFeatureConfig FANCY_PINK_CHERRY_OAK_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(PINK_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).setSapling(PINK_CHERRY_OAK_SAPLING).build();
	public static final BlockClusterFeatureConfig PINK_CHERRY_OAK_LEAF_PILE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(PINK_CHERRY_OAK_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig PINK_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.WHITE_MUSHROOM);
	public static final BlockClusterFeatureConfig MAGENTA_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.LIGHT_GRAY_MUSHROOM);
	
	public static final TreeFeatureConfig WHITE_CHERRY_OAK_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(WHITE_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().setSapling(WHITE_CHERRY_OAK_SAPLING).build();
	public static final TreeFeatureConfig FANCY_WHITE_CHERRY_OAK_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(CHERRY_OAK_LOG), new SimpleBlockStateProvider(WHITE_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).setSapling(WHITE_CHERRY_OAK_SAPLING).build();
	public static final BlockClusterFeatureConfig WHITE_CHERRY_OAK_LEAF_PILE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(WHITE_CHERRY_OAK_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig WHITE_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.WHITE_MUSHROOM);
	public static final BlockClusterFeatureConfig LIGHT_GRAY_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.LIGHT_GRAY_MUSHROOM);

	public static final TreeFeatureConfig PALM_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PALM_LOG), new SimpleBlockStateProvider(PALM_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(14).heightRandA(4).noVines().setSapling(PALM_SAPLING).build();

	public static final BlockClusterFeatureConfig BLUEBLERRY_BUSH_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BLUEBERRY_BUSH), new SimpleBlockPlacer())).tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build();

	public static final LiquidsConfig PERMAFROST_SPRING_CONFIG = new LiquidsConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(MubbleBlocks.PERMAROCK));
	public static final LiquidsConfig ENCLOSED_PERMAFROST_SPRING_CONFIG = new LiquidsConfig(Fluids.WATER.getDefaultState(), false, 5, 0, ImmutableSet.of(MubbleBlocks.PERMAROCK));
	public static final BlockClusterFeatureConfig LIGHT_BLUE_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.LIGHT_BLUE_MUSHROOM);
	public static final OreFeatureConfig.FillerBlockType PERMAROCK_FILLER = OreFeatureConfig.FillerBlockType.create("permarock_filler", "permarock", new BlockMatcher(MubbleBlocks.PERMAROCK));
	
	/* SONIC */
	public static final TreeFeatureConfig RED_PRESS_GARDEN_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().setSapling(RED_PRESS_GARDEN_SAPLING).build();
	public static final TreeFeatureConfig FANCY_RED_PRESS_GARDEN_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(0, 0))).setSapling(RED_PRESS_GARDEN_SAPLING).build();
	public static final HugeTreeFeatureConfig MEGA_RED_PRESS_GARDEN_TREE_CONFIG = (new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAVES))).baseHeight(30).heightInterval(20).setSapling(RED_PRESS_GARDEN_SAPLING).build();
	public static final BlockClusterFeatureConfig RED_PRESS_GARDEN_LEAF_PILE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(RED_PRESS_GARDEN_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final TreeFeatureConfig PINK_PRESS_GARDEN_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().setSapling(PINK_PRESS_GARDEN_SAPLING).build();
	public static final TreeFeatureConfig FANCY_PINK_PRESS_GARDEN_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(0, 0))).setSapling(PINK_PRESS_GARDEN_SAPLING).build();
	public static final HugeTreeFeatureConfig MEGA_PINK_PRESS_GARDEN_TREE_CONFIG = (new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES))).baseHeight(30).heightInterval(20).setSapling(PINK_PRESS_GARDEN_SAPLING).build();
	public static final BlockClusterFeatureConfig PINK_PRESS_GARDEN_LEAF_PILE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final BaseTreeFeatureConfig PINK_PRESS_GARDEN_GROUND_BUSH_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(PRESS_GARDEN_LOG), new SimpleBlockStateProvider(PINK_PRESS_GARDEN_LEAVES))).baseHeight(4).setSapling(PINK_PRESS_GARDEN_SAPLING).build();
	
	/* DELTARUNE */
	public static final TreeFeatureConfig SCARLET_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().setSapling(SCARLET_SAPLING).build();
	public static final TreeFeatureConfig FANCY_SCARLET_TREE_CONFIG = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES), new BlobFoliagePlacer(0, 0))).setSapling(SCARLET_SAPLING).build();
	public static final HugeTreeFeatureConfig HUGE_SCARLET_TREE_CONFIG = (new HugeTreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES))).baseHeight(6).setSapling(SCARLET_SAPLING).build();
	public static final BlockClusterFeatureConfig SCARLET_LEAF_PILE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final BaseTreeFeatureConfig SCARLET_GROUND_BUSH_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_LOG), new SimpleBlockStateProvider(SCARLET_LEAVES))).baseHeight(4).setSapling(SCARLET_SAPLING).build();
	public static final BlockClusterFeatureConfig SCARLET_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.SCARLET_MUSHROOM);
	public static final BlockClusterFeatureConfig SCARLET_ORCHID_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(SCARLET_ORCHID), new SimpleBlockPlacer())).tries(64).build();
	
	public static BlockClusterFeatureConfig mushroomConfig(Block block)
	{
		return (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(block.getDefaultState()), new SimpleBlockPlacer())).tries(64).cannotProject().build();
	}
}