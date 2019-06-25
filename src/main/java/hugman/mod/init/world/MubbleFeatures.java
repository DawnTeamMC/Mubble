package hugman.mod.init.world;

import hugman.mod.init.MubbleBlocks;
import hugman.mod.objects.world.feature.PermafrostSpringFeature;
import hugman.mod.objects.world.feature.ScarletFlowersFeature;
import hugman.mod.objects.world.feature.tree.autumn_oak.AutumnOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.pink.PinkCherryOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.pink.PinkCherryOakTreeTallFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.white.WhiteCherryOakTreeFeature;
import hugman.mod.objects.world.feature.tree.cherry_oak.white.WhiteCherryOakTreeTallFeature;
import hugman.mod.objects.world.feature.tree.palm.PalmTreeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeLargeFeature;
import hugman.mod.objects.world.feature.tree.scarlet.ScarletTreeTallFeature;
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
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_TREE = new ScarletTreeFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_SCARLET_TREE = new ScarletTreeTallFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> LARGE_SCARLET_TREE = new ScarletTreeLargeFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> AUTUMN_OAK_TREE = new AutumnOakTreeFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_AUTUMN_OAK_TREE = new AutumnOakTreeFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> PINK_CHERRY_OAK_TREE = new PinkCherryOakTreeFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_PINK_CHERRY_OAK_TREE = new PinkCherryOakTreeTallFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> WHITE_CHERRY_OAK_TREE = new WhiteCherryOakTreeFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_WHITE_CHERRY_OAK_TREE = new WhiteCherryOakTreeTallFeature(NoFeatureConfig::deserialize, false);
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_SHRUB = new ShrubFeature(NoFeatureConfig::deserialize, MubbleBlocks.SCARLET_LOG.getDefaultState(), MubbleBlocks.SCARLET_LEAVES.getDefaultState()).setSapling((IPlantable)MubbleBlocks.SCARLET_SAPLING);
	
	public static final FlowersFeature SCARLET_FLOWERS = new ScarletFlowersFeature(NoFeatureConfig::deserialize);

	public static final Feature<HellLavaConfig> PERMAFROST_SPRING = new PermafrostSpringFeature(HellLavaConfig::deserialize);
}