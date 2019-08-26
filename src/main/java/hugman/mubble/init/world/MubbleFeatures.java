package hugman.mubble.init.world;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.objects.world.feature.PermafrostSpringFeature;
import hugman.mubble.objects.world.feature.ScarletFlowersFeature;
import hugman.mubble.objects.world.feature.tree.PalmTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.LargeTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.MegaTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TallTreeFeature;
import hugman.mubble.objects.world.feature.tree.template.TreeFeature;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.HellLavaConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ShrubFeature;
import net.minecraftforge.common.IPlantable;

public class MubbleFeatures
{
	public static final AbstractTreeFeature<NoFeatureConfig> PALM_TREE = new PalmTreeFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_SCARLET_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> LARGE_SCARLET_TREE = new LargeTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> AUTUMN_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_AUTUMN_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> PINK_CHERRY_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_PINK_CHERRY_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.PINK_CHERRY_OAK_LEAVES, MubbleBlocks.PINK_CHERRY_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> WHITE_CHERRY_OAK_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_WHITE_CHERRY_OAK_TREE = new TallTreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.CHERRY_OAK_LOG, MubbleBlocks.WHITE_CHERRY_OAK_LEAVES, MubbleBlocks.WHITE_CHERRY_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> RED_PRESS_GARDEN_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> MEGA_RED_PRESS_GARDEN_TREE = new MegaTreeFeature(NoFeatureConfig::deserialize, true, 30, 40, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.RED_PRESS_GARDEN_LEAVES, MubbleBlocks.RED_PRESS_GARDEN_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> PINK_PRESS_GARDEN_TREE = new TreeFeature(NoFeatureConfig::deserialize, true, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> MEGA_PINK_PRESS_GARDEN_TREE = new MegaTreeFeature(NoFeatureConfig::deserialize, true, 30, 40, MubbleBlocks.PRESS_GARDEN_LOG, MubbleBlocks.PINK_PRESS_GARDEN_LEAVES, MubbleBlocks.PINK_PRESS_GARDEN_SAPLING);
	
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_SHRUB = new ShrubFeature(NoFeatureConfig::deserialize, MubbleBlocks.SCARLET_LOG.getDefaultState(), MubbleBlocks.SCARLET_LEAVES.getDefaultState()).setSapling((IPlantable)MubbleBlocks.SCARLET_SAPLING);
	
	public static final FlowersFeature SCARLET_FLOWERS = new ScarletFlowersFeature(NoFeatureConfig::deserialize);

	public static final Feature<HellLavaConfig> PERMAFROST_SPRING = new PermafrostSpringFeature(HellLavaConfig::deserialize);
}