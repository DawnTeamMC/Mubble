package hugman.mod.objects.world.biome;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.world.MubbleBiomes;
import hugman.mod.init.world.MubbleFeatures;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GrassFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class ScarletForestBiome extends Biome
{
    public ScarletForestBiome()
    {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG).precipitation(Biome.RainType.RAIN).category(Biome.Category.FOREST).depth(0.1F).scale(0.2F).temperature(0.7F).downfall(0.8F).waterColor(4159204).waterFogColor(329011).parent((String)null));
        this.addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL));
        this.addStructure(Feature.STRONGHOLD, IFeatureConfig.NO_FEATURE_CONFIG);
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(new Feature[]{MubbleFeatures.LARGE_SCARLET_TREE, MubbleFeatures.TALL_SCARLET_TREE, MubbleFeatures.SCARLET_SHRUB}, new IFeatureConfig[]{IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG, IFeatureConfig.NO_FEATURE_CONFIG}, new float[]{0.6666667F, 0.1F, 0.1F}, MubbleFeatures.SCARLET_TREE, IFeatureConfig.NO_FEATURE_CONFIG), Placement.DARK_OAK_TREE, IPlacementConfig.NO_PLACEMENT_CONFIG));
        DefaultBiomeFeatures.func_222338_N(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(MubbleFeatures.SCARLET_FLOWERS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(4)));
        DefaultBiomeFeatures.func_222298_O(this);
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.GRASS, new GrassFeatureConfig(MubbleBlocks.SCARLET_LEAVES_CARPET.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(4)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(MubbleBlocks.SCARLET_MUSHROOM.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(8)));
        DefaultBiomeFeatures.func_222311_aa(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.func_222297_ap(this);
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.SHEEP, 12, 4, 4));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.PIG, 10, 4, 4));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.CHICKEN, 10, 4, 4));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.COW, 8, 4, 4));
        this.addSpawn(EntityClassification.AMBIENT, new Biome.SpawnListEntry(EntityType.BAT, 10, 8, 8));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SPIDER, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE, 95, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SKELETON, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.CREEPER, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.WITCH, 5, 1, 1));
        
        this.setRegistryName(Mubble.MOD_ID, "scarlet_forest");
        MubbleBiomes.register(this);
    }
    
    @Override
    public int getGrassColor(BlockPos pos)
    {
        return 8720466;
    }
    
    @Override
    public int getFoliageColor(BlockPos pos)
    {
        return 10622269;
    }
}