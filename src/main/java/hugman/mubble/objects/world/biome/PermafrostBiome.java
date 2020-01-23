package hugman.mubble.objects.world.biome;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.world.MubbleCarvers;
import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import hugman.mubble.objects.world.feature_config.MubbleOreFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.decorator.ChanceRangeDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;

public class PermafrostBiome extends Biome
{
	private static final BlockState BISMUTH_ORE = MubbleBlocks.PERMAFROST_BISMUTH_ORE.getDefaultState();
	private static final BlockState SEA_LANTERN = Blocks.SEA_LANTERN.getDefaultState();
	
    public PermafrostBiome()
    {
		super((new Biome.Settings()).configureSurfaceBuilder(MubbleSurfaceBuilders.PERMAFROST_SURFACE_BUILDER, MubbleSurfaceBuilders.PERMAROCK_SURFACE)
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.NETHER)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(0.0F)
				.downfall(0.0F)
				.waterColor(4159204)
				.waterFogColor(329011)
				.parent((String) null));
		this.addCarver(GenerationStep.Carver.AIR, configureCarver(MubbleCarvers.PERMAFROST_CAVE_WORLD_CARVER, new ProbabilityConfig(0.2F)));
        this.addCarver(GenerationStep.Carver.AIR, configureCarver(Carver.HELL_CAVE, new ProbabilityConfig(0.2F)));
        DefaultBiomeFeatures.addDefaultMushrooms(this);
        //this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.NETHER_BRIDGE.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(MubbleFeatureConfigs.PERMAFROST_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 4, 8, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.5F, 0, 0, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.LIGHT_BLUE_MUSHROOM_CONFIG).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.5F, 0, 0, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleFeatures.ORE.configure(new MubbleOreFeatureConfig(MubbleOreFeatureConfig.Target.PERMAROCK, BISMUTH_ORE, 14)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 10, 20, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleFeatures.ORE.configure(new MubbleOreFeatureConfig(MubbleOreFeatureConfig.Target.PERMAROCK, SEA_LANTERN, 33)).createDecoratedFeature(Decorator.MAGMA.configure(new CountDecoratorConfig(4))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(MubbleFeatureConfigs.ENCLOSED_PERMAFROST_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 10, 20, 128))));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(MubbleEntities.ZOMBIE_COWMAN, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.GUARDIAN, 100, 4, 4));
    }
}