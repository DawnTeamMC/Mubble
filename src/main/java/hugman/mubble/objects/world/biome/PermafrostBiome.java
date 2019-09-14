package hugman.mubble.objects.world.biome;

import hugman.mubble.Mubble;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.world.MubbleBiomes;
import hugman.mubble.init.world.MubbleCarvers;
import hugman.mubble.init.world.MubbleFeatures;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import hugman.mubble.objects.world.feature_config.ReplaceBlockGroupConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HellLavaConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.placement.ChanceRangeConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class PermafrostBiome extends Biome
{
    public PermafrostBiome()
    {
        super((new Biome.Builder()).surfaceBuilder(MubbleSurfaceBuilders.PERMAFROST_SURFACE_BUILDER, MubbleSurfaceBuilders.PERMAROCK_SURFACE).precipitation(Biome.RainType.NONE).category(Biome.Category.NETHER).depth(0.1F).scale(0.2F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent((String)null));
        this.addStructure(Feature.NETHER_BRIDGE, IFeatureConfig.NO_FEATURE_CONFIG);
        this.addCarver(GenerationStage.Carving.AIR, createCarver(MubbleCarvers.PERMAFROST_CAVE_WORLD_CARVER, new ProbabilityConfig(0.2F)));
        this.addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.HELL_CAVE, new ProbabilityConfig(0.2F)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.SPRING_FEATURE, new LiquidsConfig(Fluids.LAVA.getDefaultState()), Placement.COUNT_VERY_BIASED_RANGE, new CountRangeConfig(20, 8, 16, 256)));
        DefaultBiomeFeatures.addMushrooms(this);
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(Feature.NETHER_BRIDGE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(MubbleFeatures.PERMAFROST_SPRING, new HellLavaConfig(false), Placement.COUNT_RANGE, new CountRangeConfig(8, 4, 8, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(Feature.BUSH, new BushConfig(Blocks.BROWN_MUSHROOM.getDefaultState()), Placement.CHANCE_RANGE, new ChanceRangeConfig(0.5F, 0, 0, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(Feature.BUSH, new BushConfig(MubbleBlocks.LIGHT_BLUE_MUSHROOM.getDefaultState()), Placement.CHANCE_RANGE, new ChanceRangeConfig(0.5F, 0, 0, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(MubbleFeatures.REPLACE_BLOCK_GROUP, new ReplaceBlockGroupConfig(MubbleBlocks.PERMAROCK.getDefaultState(), MubbleBlocks.PERMAFROST_BISMUTH_ORE.getDefaultState(), 14), Placement.COUNT_RANGE, new CountRangeConfig(16, 10, 20, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(MubbleFeatures.REPLACE_BLOCK_GROUP, new ReplaceBlockGroupConfig(MubbleBlocks.PERMAROCK.getDefaultState(), Blocks.SEA_LANTERN.getDefaultState(), 33), Placement.MAGMA, new FrequencyConfig(4)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createDecoratedFeature(MubbleFeatures.PERMAFROST_SPRING, new HellLavaConfig(true), Placement.COUNT_RANGE, new CountRangeConfig(16, 10, 20, 128)));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(MubbleEntities.ZOMBIE_COWMAN, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.GUARDIAN, 100, 4, 4));
        
        this.setRegistryName(Mubble.MOD_ID, "permafrost");
        MubbleBiomes.register(this);
    }
}