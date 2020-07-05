package hugman.mubble.object.block;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;

import java.util.function.Supplier;

public class FungusBlock extends net.minecraft.block.FungusBlock {
	/* Extension for internal publicity */
	public FungusBlock(Settings settings, Supplier<ConfiguredFeature<HugeFungusFeatureConfig, ?>> supplier) {
		super(settings, supplier);
	}
}
