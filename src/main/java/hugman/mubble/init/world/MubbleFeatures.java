package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.PermafrostSpringFeature;
import hugman.mubble.objects.world.feature.ScarletFlowersFeature;
import hugman.mubble.objects.world.feature.TomatoFeature;
import hugman.mubble.objects.world.feature.tree.PalmTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.LargeTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.MegaTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TallTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.HellLavaConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ScatteredPlantFeature;
import net.minecraft.world.gen.feature.ShrubFeature;

public class MubbleFeatures
{
	public static final Feature<NoFeatureConfig> PALM_TREE = new PalmTreeFeature(NoFeatureConfig::deserialize, false);
	public static final Feature<NoFeatureConfig> SCARLET_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final Feature<NoFeatureConfig> TALL_SCARLET_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final Feature<NoFeatureConfig> LARGE_SCARLET_TREE = new LargeTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final Feature<NoFeatureConfig> AUTUMN_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> TALL_AUTUMN_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> PINK_CHERRY_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> TALL_PINK_CHERRY_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> WHITE_CHERRY_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> TALL_WHITE_CHERRY_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	public static final Feature<NoFeatureConfig> RED_PRESS_GARDEN_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_SAPLING);
	public static final Feature<NoFeatureConfig> MEGA_RED_PRESS_GARDEN_TREE = new MegaTreeFeature(NoFeatureConfig::deserialize, true, 30, 40, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_SAPLING);
	public static final Feature<NoFeatureConfig> PINK_PRESS_GARDEN_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_SAPLING);
	public static final Feature<NoFeatureConfig> MEGA_PINK_PRESS_GARDEN_TREE = new MegaTreeFeature(NoFeatureConfig::deserialize, true, 30, 40, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_SAPLING);
	
	public static final Feature<NoFeatureConfig> PINK_PRESS_GARDEN_GROUND_BUSH = new ShrubFeature(NoFeatureConfig::deserialize, MubbleBlocks.PRESS_GARDEN_LOG.getDefaultState(), MubbleBlocks.PINK_PRESS_GARDEN_LEAVES.getDefaultState());
	
	public static final Feature<NoFeatureConfig> SCARLET_GROUND_BUSH = new ShrubFeature(NoFeatureConfig::deserialize, MubbleBlocks.SCARLET_LOG.getDefaultState(), MubbleBlocks.SCARLET_LEAVES.getDefaultState());
	public static final FlowersFeature SCARLET_FLOWERS = new ScarletFlowersFeature(NoFeatureConfig::deserialize);

	public static final Feature<NoFeatureConfig> TOMATOES = new TomatoFeature(NoFeatureConfig::deserialize);
	public static final Feature<NoFeatureConfig> BLUEBERRY_BUSH = new ScatteredPlantFeature(NoFeatureConfig::deserialize, MubbleBlocks.BLUEBERRY_BUSH.getDefaultState().with(SweetBerryBushBlock.AGE, Integer.valueOf(3)));
	
	public static final Feature<HellLavaConfig> PERMAFROST_SPRING = new PermafrostSpringFeature(HellLavaConfig::deserialize);
}