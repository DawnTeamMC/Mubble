package hugman.mubble.objects.world.biome;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.world.MubbleCarvers;
import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.placement.ChanceRangeConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class PermafrostBiome extends Biome
{
	private static final BlockState BISMUTH_ORE = MubbleBlocks.PERMAFROST_BISMUTH_ORE.getDefaultState();
	private static final BlockState SEA_LANTERN = Blocks.SEA_LANTERN.getDefaultState();
	
    public PermafrostBiome()
    {
		super((new Biome.Builder()).surfaceBuilder(MubbleSurfaceBuilders.PERMAFROST_SURFACE_BUILDER, MubbleSurfaceBuilders.PERMAROCK_SURFACE)
				.precipitation(Biome.RainType.NONE)
				.category(Biome.Category.NETHER)
				.depth(0.5F)
				.scale(0.5F)
				.temperature(0.0F)
				.downfall(0.0F)
				.waterColor(4159204)
				.waterFogColor(329011)
				.parent((String) null));
		this.addCarver(GenerationStage.Carving.AIR, createCarver(MubbleCarvers.PERMAFROST_CAVE_WORLD_CARVER, new ProbabilityConfig(0.2F)));
        this.addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.HELL_CAVE, new ProbabilityConfig(0.2F)));
        DefaultBiomeFeatures.addMushrooms(this);
        //this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.NETHER_BRIDGE.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(MubbleFeatureConfigs.PERMAFROST_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 4, 8, 128))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_RANGE.configure(new ChanceRangeConfig(0.5F, 0, 0, 128))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.field_227248_z_.configure(MubbleFeatureConfigs.LIGHT_BLUE_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_RANGE.configure(new ChanceRangeConfig(0.5F, 0, 0, 128))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(new OreFeatureConfig(MubbleFeatureConfigs.PERMAROCK_FILLER, BISMUTH_ORE, 14)).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(16, 10, 20, 128))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(new OreFeatureConfig(MubbleFeatureConfigs.PERMAROCK_FILLER, SEA_LANTERN, 33)).createDecoratedFeature(Placement.MAGMA.configure(new FrequencyConfig(4))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(MubbleFeatureConfigs.ENCLOSED_PERMAFROST_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(16, 10, 20, 128))));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(MubbleEntities.ZOMBIE_COWMAN, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.GUARDIAN, 100, 4, 4));
    }
}