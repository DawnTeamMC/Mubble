package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.feature.MubbleOreFeature;
import hugman.mubble.objects.world.feature.tree.PalmTreeFeature;
import hugman.mubble.objects.world.feature_config.MubbleOreFeatureConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class MubbleFeatures
{
	public static final Feature<BranchedTreeFeatureConfig> PALM_TREE = register("palm_tree", new PalmTreeFeature(BranchedTreeFeatureConfig::deserialize));
	
	public static final Feature<MubbleOreFeatureConfig> ORE = register("ore", new MubbleOreFeature(MubbleOreFeatureConfig::deserialize));
	
	private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature)
	{
		return Registry.register(Registry.FEATURE, new Identifier(Mubble.MOD_ID, name), feature);
	}
}