package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.gen.feature.HugeNetherBrownMushroomFeature;
import hugman.mubble.objects.world.gen.feature.HugeNetherRedMushroomFeature;
import hugman.mubble.objects.world.gen.feature.TallHugeFungusFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;

public class MubbleFeatures
{
	public static final Feature<HugeFungusFeatureConfig> TALL_HUGE_FUNGUS = register("tall_huge_fungus", new TallHugeFungusFeature(HugeFungusFeatureConfig.CODEC));
	public static final Feature<HugeMushroomFeatureConfig> HUGE_NETHER_BROWN_MUSHROOM = register("huge_nether_brown_mushroom", new HugeNetherBrownMushroomFeature(HugeMushroomFeatureConfig.CODEC));
	public static final Feature<HugeMushroomFeatureConfig> HUGE_NETHER_RED_MUSHROOM = register("red_nether_brown_mushroom", new HugeNetherRedMushroomFeature(HugeMushroomFeatureConfig.CODEC));

	private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature)
	{
		return Registry.register(Registry.FEATURE, new Identifier(Mubble.MOD_ID, name), feature);
	}
}
