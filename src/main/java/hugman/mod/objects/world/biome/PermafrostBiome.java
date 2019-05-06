package hugman.mod.objects.world.biome;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleEntities;
import hugman.mod.init.world.MubbleBiomes;
import hugman.mod.init.world.MubbleCarvers;
import hugman.mod.init.world.MubbleFeatures;
import hugman.mod.init.world.MubbleSurfaceBuilders;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HellLavaConfig;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.structure.FortressConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.ChanceRangeConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;

public class PermafrostBiome extends Biome
{
	public PermafrostBiome()
	{
        super((new Biome.BiomeBuilder()).surfaceBuilder(new CompositeSurfaceBuilder<>(MubbleSurfaceBuilders.PERMAFROST_SURFACE_BUILDER, MubbleSurfaceBuilders.PERMAROCK_SURFACE)).precipitation(Biome.RainType.NONE).category(Biome.Category.NETHER).depth(0.1F).scale(0.2F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent((String)null));
        this.addStructure(Feature.FORTRESS, new FortressConfig());
        this.addCarver(GenerationStage.Carving.AIR, createWorldCarverWrapper(MubbleCarvers.PERMAFROST_CAVE_WORLD_CARVER, new ProbabilityConfig(0.2F)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.LIQUIDS, new LiquidsConfig(Fluids.WATER), HEIGHT_VERY_BIASED_RANGE, new CountRangeConfig(20, 8, 16, 256)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.BUSH, new BushConfig(Blocks.BROWN_MUSHROOM), TWICE_SURFACE_WITH_CHANCE, new ChanceConfig(4)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.BUSH, new BushConfig(MubbleBlocks.LIGHT_BLUE_MUSHROOM), TWICE_SURFACE_WITH_CHANCE, new ChanceConfig(8)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature(Feature.FORTRESS, new FortressConfig(), PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature(MubbleFeatures.PERMAFROST_WATER, new HellLavaConfig(false), COUNT_RANGE, new CountRangeConfig(8, 4, 8, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature(Feature.BUSH, new BushConfig(Blocks.BROWN_MUSHROOM), CHANCE_RANGE, new ChanceRangeConfig(0.5F, 0, 0, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature(Feature.BUSH, new BushConfig(MubbleBlocks.LIGHT_BLUE_MUSHROOM), CHANCE_RANGE, new ChanceRangeConfig(0.5F, 0, 0, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature(Feature.MINABLE, new MinableConfig(BlockMatcher.forBlock(MubbleBlocks.PERMAROCK), MubbleBlocks.PERMAFROST_BISMUTH_ORE.getDefaultState(), 6), COUNT_RANGE, new CountRangeConfig(5, 10, 20, 128)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature(Feature.MINABLE, new MinableConfig(BlockMatcher.forBlock(MubbleBlocks.PERMAROCK), Blocks.PACKED_ICE.getDefaultState(), 33), NETHER_MAGMA, new FrequencyConfig(4)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, createCompositeFeature(MubbleFeatures.PERMAFROST_WATER, new HellLavaConfig(true), COUNT_RANGE, new CountRangeConfig(16, 10, 20, 128)));
        this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.GHAST, 50, 4, 4));
        this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(MubbleEntities.ZOMBIE_COWMAN, 100, 4, 4));
        this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.MAGMA_CUBE, 2, 4, 4));
        this.addSpawn(EnumCreatureType.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 1, 4, 4));
        
        this.setRegistryName(Mubble.MOD_ID, "permafrost");
        MubbleBiomes.register(this);
	}
}