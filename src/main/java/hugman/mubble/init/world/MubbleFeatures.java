package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.PermafrostSpringFeature;
import hugman.mubble.objects.world.feature.ReplaceBlockGroupFeature;
import hugman.mubble.objects.world.feature.ScarletFlowersFeature;
import hugman.mubble.objects.world.feature.TomatoFeature;
import hugman.mubble.objects.world.feature.tree.PalmTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.LargeTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.MegaTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TallTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TreeFeature;
import hugman.mubble.objects.world.feature_config.ReplaceBlockGroupConfig;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;
import net.minecraft.world.gen.feature.JungleGroundBushFeature;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;

public class MubbleFeatures
{
	public static final Feature<BranchedTreeFeatureConfig> PALM_TREE = register("palm_tree", new PalmTreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> SCARLET_TREE = register("scarlet_tree", new TreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> TALL_SCARLET_TREE = register("tall_scarlet_tree", new TallTreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<MegaTreeFeatureConfig> LARGE_SCARLET_TREE = register("large_scarlet_tree", new LargeTreeFeature(MegaTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> AUTUMN_OAK_TREE = register("autumn_oak_tree", new TreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> TALL_AUTUMN_OAK_TREE = register("tall_autumn_oak_tree", new TallTreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> PINK_CHERRY_OAK_TREE = register("pink_cherry_oak_tree", new TreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> TALL_PINK_CHERRY_OAK_TREE = register("tall_pink_cherry_oak_tree", new TallTreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> WHITE_CHERRY_OAK_TREE = register("white_cherry_oak_tree", new TreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> TALL_WHITE_CHERRY_OAK_TREE = register("tall_white_cherry_oak_tree", new TallTreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> RED_PRESS_GARDEN_TREE = register("red_press_garden_tree", new TreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<MegaTreeFeatureConfig> MEGA_RED_PRESS_GARDEN_TREE = register("mega_red_press_garden_tree", new MegaTreeFeature(MegaTreeFeatureConfig::deserialize));
	public static final Feature<BranchedTreeFeatureConfig> PINK_PRESS_GARDEN_TREE = register("pink_press_garden_tree", new TreeFeature(BranchedTreeFeatureConfig::deserialize));
	public static final Feature<MegaTreeFeatureConfig> MEGA_PINK_PRESS_GARDEN_TREE = register("mega_pink_press_garden_tree", new MegaTreeFeature(MegaTreeFeatureConfig::deserialize));
	
	public static final Feature<TreeFeatureConfig> PINK_PRESS_GARDEN_GROUND_BUSH = register("pink_press_garden_ground_bush", new JungleGroundBushFeature(TreeFeatureConfig::deserialize));
	
	public static final Feature<TreeFeatureConfig> SCARLET_GROUND_BUSH = register("scarlet_ground_bush", new JungleGroundBushFeature(TreeFeatureConfig::deserialize));
	public static final FlowerFeature<DefaultFeatureConfig> SCARLET_FLOWERS = register("scarlet_flowers", new ScarletFlowersFeature(DefaultFeatureConfig::deserialize));

	public static final Feature<DefaultFeatureConfig> TOMATOES = register("tomatoes", new TomatoFeature(DefaultFeatureConfig::deserialize));
	public static final Feature<RandomPatchFeatureConfig> BLUEBERRY_BUSH = register("blueberry_bush", new RandomPatchFeature(RandomPatchFeatureConfig::deserialize));
	
	public static final Feature<SpringFeatureConfig> PERMAFROST_SPRING = register("permafrost_spring", new PermafrostSpringFeature(SpringFeatureConfig::deserialize));
	
	public static final Feature<ReplaceBlockGroupConfig> REPLACE_BLOCK_GROUP = register("replace_block_group", new ReplaceBlockGroupFeature(ReplaceBlockGroupConfig::deserialize));
	
	private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
		return Registry.register(Registry.FEATURE, new Identifier(Mubble.MOD_ID, name), feature);
	}
	
	public static class Config
	{
		public static final BranchedTreeFeatureConfig PALM_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PALM_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PALM_LEAVES.getDefaultState()), new AcaciaFoliagePlacer(2, 0)).baseHeight(5).heightRandA(2).heightRandB(2).trunkHeight(0).noVines().build();
		public static final BranchedTreeFeatureConfig SCARLET_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build();
		public static final BranchedTreeFeatureConfig TALL_SCARLET_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build();
		public static final MegaTreeFeatureConfig LARGE_SCARLET_TREE = new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState())).baseHeight(6).build();
		public static final BranchedTreeFeatureConfig AUTUMN_OAK_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.AUTUMN_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).baseHeight(4).heightRandA(2).foliageHeight(3).noVines().build();
		public static final BranchedTreeFeatureConfig TALL_AUTUMN_OAK_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.AUTUMN_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build();
		public static final BranchedTreeFeatureConfig PINK_CHERRY_OAK_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.CHERRY_OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PINK_CHERRY_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).baseHeight(4).heightRandA(2).foliageHeight(3).noVines().build();
		public static final BranchedTreeFeatureConfig TALL_PINK_CHERRY_OAK_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.CHERRY_OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PINK_CHERRY_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build();
		public static final BranchedTreeFeatureConfig WHITE_CHERRY_OAK_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.CHERRY_OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.WHITE_CHERRY_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).baseHeight(4).heightRandA(2).foliageHeight(3).noVines().build();
		public static final BranchedTreeFeatureConfig TALL_WHITE_CHERRY_OAK_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.CHERRY_OAK_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.WHITE_CHERRY_OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build();
		public static final BranchedTreeFeatureConfig PINK_PRESS_GARDEN_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().build();
		public static final MegaTreeFeatureConfig MEGA_PINK_PRESS_GARDEN_TREE = new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getDefaultState())).baseHeight(6).build();
		public static final BranchedTreeFeatureConfig RED_PRESS_GARDEN_TREE = new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).baseHeight(5).heightRandA(2).foliageHeight(3).noVines().build();
		public static final MegaTreeFeatureConfig MEGA_RED_PRESS_GARDEN_TREE = new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.RED_PRESS_GARDEN_LEAVES.getDefaultState())).baseHeight(6).build();
		
		public static final TreeFeatureConfig PINK_PRESS_GARDEN_GROUND_BUSH = new TreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getDefaultState())).baseHeight(4).build();
		public static final TreeFeatureConfig SCARLET_GROUND_BUSH = new TreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState())).baseHeight(4).build();
		
		public static final RandomPatchFeatureConfig BROWN_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(Blocks.BROWN_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig LIGHT_BLUE_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.LIGHT_BLUE_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig LIGHT_GRAY_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.LIGHT_GRAY_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig MAGENTA_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.MAGENTA_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig ORANGE_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.ORANGE_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig PINK_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.PINK_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig YELLOW_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.YELLOW_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig WHITE_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.WHITE_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
		public static final RandomPatchFeatureConfig SCARLET_MUSHROOM = new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).tries(64).cannotProject().build();
	}
}