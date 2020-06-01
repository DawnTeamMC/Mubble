package hugman.mubble.objects.world.biome.nether;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceRangeDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomBooleanFeatureConfig;

public class TritanopianGalleryBiome extends GalleryBiome
{
	public TritanopianGalleryBiome()
	{
		super();
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.PINK_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.5F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.CYAN_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.5F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.PINK_HUGE_NETHER_MUSHROOM),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM)))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR
						.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.PINK_HUGE_NETHER_MUSHROOM.setUpsideDown()),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM.setUpsideDown())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.PINK_HUGE_NETHER_MUSHROOM.setFlatHat()),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM.setFlatHat(3))))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.PINK_HUGE_NETHER_MUSHROOM.setFlatHat().setUpsideDown()),
								MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM.setFlatHat(3).setUpsideDown())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
	}
}
