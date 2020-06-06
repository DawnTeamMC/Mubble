package hugman.mubble.objects.world.biome.nether;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceRangeDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomBooleanFeatureConfig;

public class AchromatopsianGalleryBiome extends GalleryBiome
{
	public AchromatopsianGalleryBiome()
	{
		super(new MixedNoisePoint(0.1F, 0.05F, 0.0F, 0.0F, 0.025F));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.WHITE_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.LIGHT_GRAY_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.GRAY_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.BLACK_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.GRAY_HUGE_NETHER_MUSHROOM),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BLACK_HUGE_NETHER_MUSHROOM)))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR
						.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.WHITE_HUGE_NETHER_MUSHROOM.setUpsideDown()),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.LIGHT_GRAY_HUGE_NETHER_MUSHROOM.setUpsideDown())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.GRAY_HUGE_NETHER_MUSHROOM.setFlatHat()),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BLACK_HUGE_NETHER_MUSHROOM.setFlatHat())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.WHITE_HUGE_NETHER_MUSHROOM.setFlatHat().setUpsideDown()),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.LIGHT_GRAY_HUGE_NETHER_MUSHROOM.setFlatHat().setUpsideDown())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
	}
}
