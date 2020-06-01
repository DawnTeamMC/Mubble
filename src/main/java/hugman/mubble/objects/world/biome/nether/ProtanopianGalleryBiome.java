package hugman.mubble.objects.world.biome.nether;

import com.google.common.collect.ImmutableList;
import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceRangeDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomFeatureConfig;

public class ProtanopianGalleryBiome extends GalleryBiome
{
	public ProtanopianGalleryBiome()
	{
		super();
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.YELLOW_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.CYAN_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION,
				Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.BLUE_MUSHROOM_PATCHES)
						.createDecoratedFeature(Decorator.CHANCE_RANGE
								.configure(new ChanceRangeDecoratorConfig(0.25F, 0, 0, 128))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BROWN_HUGE_NETHER_MUSHROOM.setFlatHat()).withChance(0.25F),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.YELLOW_HUGE_NETHER_MUSHROOM).withChance(0.25F),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM).withChance(0.25F)),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BLUE_HUGE_NETHER_MUSHROOM)))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BROWN_HUGE_NETHER_MUSHROOM.setFlatHat().setUpsideDown()).withChance(0.25F),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.YELLOW_HUGE_NETHER_MUSHROOM.setUpsideDown()).withChance(0.25F),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM.setUpsideDown()).withChance(0.25F)),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BLUE_HUGE_NETHER_MUSHROOM.setUpsideDown())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.YELLOW_HUGE_NETHER_MUSHROOM.setFlatHat()).withChance(1F / 3F),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM.setFlatHat()).withChance(1F / 3F)),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BLUE_HUGE_NETHER_MUSHROOM.setFlatHat())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION,
				Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.YELLOW_HUGE_NETHER_MUSHROOM.setFlatHat().setUpsideDown()).withChance(1F / 3F),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.CYAN_HUGE_NETHER_MUSHROOM.setFlatHat().setUpsideDown()).withChance(1F / 3F)),
						MubbleFeatures.HUGE_NETHER_MUSHROOM.configure(MubbleFeatureConfigs.BLUE_HUGE_NETHER_MUSHROOM.setFlatHat().setUpsideDown())))
						.createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(2))));
	}
}
