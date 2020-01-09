package hugman.mubble.objects.world.biome;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.world.MubbleCarvers;
import hugman.mubble.init.world.MubbleFeatures;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import hugman.mubble.objects.world.feature_config.ReplaceBlockGroupConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.world.gen.feature.SpringFeatureConfig;

public class PermafrostBiome extends Biome
{
	private final Set<Block> carvableBlocks = ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.GRASS_BLOCK, MubbleBlocks.PERMAROCK);
	
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
        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(new SpringFeatureConfig(Fluids.LAVA.getDefaultState(), true, 4, 1, carvableBlocks)).createDecoratedFeature(Decorator.COUNT_VERY_BIASED_RANGE.configure(new RangeDecoratorConfig(20, 8, 16, 256))));
        DefaultBiomeFeatures.addDefaultMushrooms(this);
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleFeatures.PERMAFROST_SPRING.configure(new SpringFeatureConfig(Fluids.LAVA.getDefaultState(), false, 4, 1, carvableBlocks)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 4, 8, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(MubbleFeatures.Config.BROWN_MUSHROOM).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.5F, 0, 0, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(MubbleFeatures.Config.LIGHT_BLUE_MUSHROOM).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.5F, 0, 0, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleFeatures.REPLACE_BLOCK_GROUP.configure(new ReplaceBlockGroupConfig(MubbleBlocks.PERMAROCK.getDefaultState(), MubbleBlocks.PERMAFROST_BISMUTH_ORE.getDefaultState(), 14)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 10, 20, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleFeatures.REPLACE_BLOCK_GROUP.configure(new ReplaceBlockGroupConfig(MubbleBlocks.PERMAROCK.getDefaultState(), Blocks.SEA_LANTERN.getDefaultState(), 33)).createDecoratedFeature(Decorator.MAGMA.configure(new CountDecoratorConfig(4))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, MubbleFeatures.PERMAFROST_SPRING.configure(new SpringFeatureConfig(Fluids.LAVA.getDefaultState(), true, 4, 1, carvableBlocks)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(16, 10, 20, 128))));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(MubbleEntities.ZOMBIE_COWMAN, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.GUARDIAN, 100, 4, 4));
    }
}