package hugman.mubble.util.creator.block;

import hugman.mubble.object.block.FungusBlock;
import hugman.mubble.util.creator.BlockTemplate;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;

import java.util.function.Supplier;

public class FungusCreator extends PottedPlantCreator {
	public FungusCreator(String name, Supplier<ConfiguredFeature<HugeFungusFeatureConfig, ?>> supplier) {
		super(name + "_fungus", new FungusBlock(BlockTemplate.fungusSettings, supplier));
	}

	public Block getFungus() {
		return getPlant();
	}
}
