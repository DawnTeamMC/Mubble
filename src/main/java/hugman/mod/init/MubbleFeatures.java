package hugman.mod.init;

import hugman.mod.objects.world.feature.ScarletFlowersFeature;
import hugman.mod.objects.world.feature.tree.autumn_oak.AutumnOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.CherryOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.CherryOakTreeTallFeature;
import hugman.mod.objects.world.feature.tree.palm.PalmTreeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeLargeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeTallFeature;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ShrubFeature;
import net.minecraftforge.common.IPlantable;

public class MubbleFeatures
{
	public static final AbstractTreeFeature<NoFeatureConfig> PALM_TREE = new PalmTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_TREE = new ScarletTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_SCARLET_TREE = new ScarletTreeTallFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> LARGE_SCARLET_TREE = new ScarletTreeLargeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> AUTUMN_OAK_TREE = new AutumnOakTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_AUTUMN_OAK_TREE = new AutumnOakTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> CHERRY_OAK_TREE = new CherryOakTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_CHERRY_OAK_TREE = new CherryOakTreeTallFeature(false);
	public static final AbstractFlowersFeature SCARLET_FLOWERS = new ScarletFlowersFeature();
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_SHRUB = new ShrubFeature(MubbleBlocks.SCARLET_LOG.getDefaultState(), MubbleBlocks.SCARLET_LEAVES.getDefaultState()).setSapling((IPlantable)MubbleBlocks.SCARLET_SAPLING);
}