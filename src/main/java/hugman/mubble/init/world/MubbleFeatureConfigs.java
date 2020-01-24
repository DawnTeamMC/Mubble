package hugman.mubble.init.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import hugman.mubble.init.MubbleBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.gen.decorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;

public class MubbleFeatureConfigs
{
	private static final BlockState OAK_LOG = Blocks.OAK_LOG.getDefaultState();
	
	private static final BlockState AUTUMN_OAK_LEAVES = MubbleBlocks.AUTUMN_OAK_LEAVES.getDefaultState();
	private static final BlockState AUTUMN_OAK_LEAF_PILE = MubbleBlocks.AUTUMN_OAK_LEAF_PILE.getDefaultState();
	
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
	
	/* MINECRAFT */
	public static final BranchedTreeFeatureConfig AUTUMN_OAK_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(OAK_LOG), new SimpleStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).noVines().build();
	public static final BranchedTreeFeatureConfig AUTUMN_OAK_TREE_BEEHIVED_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(OAK_LOG), new SimpleStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).noVines().treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).build();
	public static final BranchedTreeFeatureConfig FANCY_AUTUMN_OAK_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(OAK_LOG), new SimpleStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).build();
	public static final BranchedTreeFeatureConfig FANCY_AUTUMN_OAK_BEEHIVED_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(OAK_LOG), new SimpleStateProvider(AUTUMN_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).build();
	public static final RandomPatchFeatureConfig AUTUMN_OAK_LEAF_PILE_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(AUTUMN_OAK_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final RandomPatchFeatureConfig YELLOW_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.YELLOW_MUSHROOM);
	public static final RandomPatchFeatureConfig ORANGE_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.ORANGE_MUSHROOM);
	
	public static final BranchedTreeFeatureConfig PINK_CHERRY_OAK_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(PINK_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().build();
	public static final BranchedTreeFeatureConfig PINK_CHERRY_OAK_TREE_BEEHIVED_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(PINK_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).build();
	public static final BranchedTreeFeatureConfig FANCY_PINK_CHERRY_OAK_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(PINK_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).build();
	public static final BranchedTreeFeatureConfig FANCY_PINK_CHERRY_OAK_TREE_BEEHIVED_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(PINK_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).build();
	public static final RandomPatchFeatureConfig PINK_CHERRY_OAK_LEAF_PILE_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(PINK_CHERRY_OAK_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final RandomPatchFeatureConfig PINK_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.WHITE_MUSHROOM);
	public static final RandomPatchFeatureConfig MAGENTA_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.LIGHT_GRAY_MUSHROOM);
	
	public static final BranchedTreeFeatureConfig WHITE_CHERRY_OAK_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(WHITE_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().build();
	public static final BranchedTreeFeatureConfig WHITE_CHERRY_OAK_TREE_BEEHIVED_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(WHITE_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).build();
	public static final BranchedTreeFeatureConfig FANCY_WHITE_CHERRY_OAK_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(WHITE_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).build();
	public static final BranchedTreeFeatureConfig FANCY_WHITE_CHERRY_OAK_TREE_BEEHIVED_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(CHERRY_OAK_LOG), new SimpleStateProvider(WHITE_CHERRY_OAK_LEAVES), new BlobFoliagePlacer(0, 0))).treeDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))).build();
	public static final RandomPatchFeatureConfig WHITE_CHERRY_OAK_LEAF_PILE_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(WHITE_CHERRY_OAK_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final RandomPatchFeatureConfig WHITE_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.WHITE_MUSHROOM);
	public static final RandomPatchFeatureConfig LIGHT_GRAY_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.LIGHT_GRAY_MUSHROOM);

	public static final BranchedTreeFeatureConfig PALM_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(PALM_LOG), new SimpleStateProvider(PALM_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(14).heightRandA(4).noVines().build();

	public static final RandomPatchFeatureConfig BLUEBLERRY_BUSH_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(BLUEBERRY_BUSH), new SimpleBlockPlacer())).tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build();

	public static final SpringFeatureConfig PERMAFROST_SPRING_CONFIG = new SpringFeatureConfig(Fluids.WATER.getDefaultState(), true, 4, 1, ImmutableSet.of(MubbleBlocks.PERMAROCK));
	public static final SpringFeatureConfig ENCLOSED_PERMAFROST_SPRING_CONFIG = new SpringFeatureConfig(Fluids.WATER.getDefaultState(), false, 5, 0, ImmutableSet.of(MubbleBlocks.PERMAROCK));
	public static final RandomPatchFeatureConfig LIGHT_BLUE_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.LIGHT_BLUE_MUSHROOM);
	
	/* SONIC */
	public static final BranchedTreeFeatureConfig RED_PRESS_GARDEN_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(PRESS_GARDEN_LOG), new SimpleStateProvider(RED_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().build();
	public static final BranchedTreeFeatureConfig FANCY_RED_PRESS_GARDEN_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(PRESS_GARDEN_LOG), new SimpleStateProvider(RED_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(0, 0))).build();
	public static final MegaTreeFeatureConfig MEGA_RED_PRESS_GARDEN_TREE_CONFIG = (new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(PRESS_GARDEN_LOG), new SimpleStateProvider(RED_PRESS_GARDEN_LEAVES))).baseHeight(30).heightInterval(20).build();
	public static final RandomPatchFeatureConfig RED_PRESS_GARDEN_LEAF_PILE_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(RED_PRESS_GARDEN_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final BranchedTreeFeatureConfig PINK_PRESS_GARDEN_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(PRESS_GARDEN_LOG), new SimpleStateProvider(PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().build();
	public static final BranchedTreeFeatureConfig FANCY_PINK_PRESS_GARDEN_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(PRESS_GARDEN_LOG), new SimpleStateProvider(PINK_PRESS_GARDEN_LEAVES), new BlobFoliagePlacer(0, 0))).build();
	public static final MegaTreeFeatureConfig MEGA_PINK_PRESS_GARDEN_TREE_CONFIG = (new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(PRESS_GARDEN_LOG), new SimpleStateProvider(PINK_PRESS_GARDEN_LEAVES))).baseHeight(30).heightInterval(20).build();
	public static final RandomPatchFeatureConfig PINK_PRESS_GARDEN_LEAF_PILE_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(PINK_PRESS_GARDEN_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final TreeFeatureConfig PINK_PRESS_GARDEN_GROUND_BUSH_CONFIG = (new TreeFeatureConfig.Builder(new SimpleStateProvider(PRESS_GARDEN_LOG), new SimpleStateProvider(PINK_PRESS_GARDEN_LEAVES))).baseHeight(4).build();
	
	/* DELTARUNE */
	public static final BranchedTreeFeatureConfig SCARLET_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(SCARLET_LOG), new SimpleStateProvider(SCARLET_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().build();
	public static final BranchedTreeFeatureConfig FANCY_SCARLET_TREE_CONFIG = (new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(SCARLET_LOG), new SimpleStateProvider(SCARLET_LEAVES), new BlobFoliagePlacer(0, 0))).build();
	public static final MegaTreeFeatureConfig HUGE_SCARLET_TREE_CONFIG = (new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(SCARLET_LOG), new SimpleStateProvider(SCARLET_LEAVES))).baseHeight(6).build();
	public static final RandomPatchFeatureConfig SCARLET_LEAF_PILE_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(SCARLET_LEAF_PILE), new SimpleBlockPlacer())).tries(32).build();
	public static final TreeFeatureConfig SCARLET_GROUND_BUSH_CONFIG = (new TreeFeatureConfig.Builder(new SimpleStateProvider(SCARLET_LOG), new SimpleStateProvider(SCARLET_LEAVES))).baseHeight(4).build();
	public static final RandomPatchFeatureConfig SCARLET_MUSHROOM_CONFIG = mushroomConfig(MubbleBlocks.SCARLET_MUSHROOM);
	public static final RandomPatchFeatureConfig SCARLET_ORCHID_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(SCARLET_ORCHID), new SimpleBlockPlacer())).tries(64).build();
	
	public static RandomPatchFeatureConfig mushroomConfig(Block block)
	{
		return (new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(block.getDefaultState()), new SimpleBlockPlacer())).tries(64).cannotProject().build();
	}
}
