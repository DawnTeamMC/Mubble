package hugman.mubble.object.block.sapling_generator;

import hugman.mubble.init.world.MubbleConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class AutumnOakSaplingGenerator extends SaplingGenerator {
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean beeHive) {
		return random.nextInt(10) == 0 ? beeHive ? MubbleConfiguredFeatures.FANCY_AUTUMN_BIRCH_BEES_002 : MubbleConfiguredFeatures.FANCY_AUTUMN_BIRCH : beeHive ? MubbleConfiguredFeatures.AUTUMN_OAK_BEES_002 : MubbleConfiguredFeatures.AUTUMN_OAK;
	}
}