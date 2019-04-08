package hugman.mod.init;

import hugman.mod.objects.world.feature.ScarletFlowersFeature;
import hugman.mod.objects.world.feature.tree.palm.PalmTreeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeFeature;
import hugman.mod.objects.world.feature.tree.template.TreeFeature;
import hugman.mod.objects.world.feature.tree.template.TreeLargeFeature;
import hugman.mod.objects.world.feature.tree.template.TreeTallFeature;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class MubbleFeatures
{
	public static final AbstractTreeFeature<NoFeatureConfig> PALM_TREE = new PalmTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_TREE = new ScarletTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_SCARLET_TREE = new TreeTallFeature(false, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> LARGE_SCARLET_TREE = new TreeLargeFeature(false, MubbleBlocks.SCARLET_LOG, MubbleBlocks.SCARLET_LEAVES, MubbleBlocks.SCARLET_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> AUTUMN_OAK_TREE = new TreeFeature(false, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_AUTUMN_OAK_TREE = new TreeTallFeature(false, Blocks.OAK_LOG, MubbleBlocks.AUTUMN_OAK_LEAVES, MubbleBlocks.AUTUMN_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> CHERRY_OAK_TREE = new TreeFeature(false, Blocks.OAK_LOG, MubbleBlocks.CHERRY_OAK_LEAVES, MubbleBlocks.CHERRY_OAK_SAPLING);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_CHERRY_OAK_TREE = new TreeTallFeature(false, Blocks.OAK_LOG, MubbleBlocks.CHERRY_OAK_LEAVES, MubbleBlocks.CHERRY_OAK_SAPLING);
	public static final AbstractFlowersFeature SCARLET_FLOWERS = new ScarletFlowersFeature();
}