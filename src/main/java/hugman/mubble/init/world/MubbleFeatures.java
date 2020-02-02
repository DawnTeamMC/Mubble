package hugman.mubble.init.world;

import hugman.mubble.objects.world.feature.PalmTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class MubbleFeatures
{
	public static final Feature<TreeFeatureConfig> PALM_TREE = new PalmTreeFeature(TreeFeatureConfig::deserialize);
}