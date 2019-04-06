package hugman.mod.init;

import hugman.mod.objects.world.structure.LargeScarletTreeFeature;
import hugman.mod.objects.world.structure.PalmTreeFeature;
import hugman.mod.objects.world.structure.ScarletFlowersFeature;
import hugman.mod.objects.world.structure.ScarletTreeFeature;
import hugman.mod.objects.world.structure.TallScarletTreeFeature;
import net.minecraft.world.gen.feature.AbstractFlowersFeature;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class MubbleFeatures
{
	public static final AbstractTreeFeature<NoFeatureConfig> PALM_TREE = new PalmTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> SCARLET_TREE = new ScarletTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> TALL_SCARLET_TREE = new TallScarletTreeFeature(false);
	public static final AbstractTreeFeature<NoFeatureConfig> LARGE_SCARLET_TREE = new LargeScarletTreeFeature(false);
	public static final AbstractFlowersFeature SCARLET_FLOWERS = new ScarletFlowersFeature();
}