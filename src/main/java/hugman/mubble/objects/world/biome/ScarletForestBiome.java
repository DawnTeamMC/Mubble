package hugman.mubble.objects.world.biome;

import com.google.common.collect.ImmutableList;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleFeatures;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.MegaTreeFeatureConfig;
import net.minecraft.world.gen.feature.MineshaftFeature;
import net.minecraft.world.gen.feature.MineshaftFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class ScarletForestBiome extends Biome
{
    public ScarletForestBiome()
    {
		super((new Biome.Settings()).configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG)
				.precipitation(Biome.Precipitation.RAIN)
				.category(Biome.Category.FOREST)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(0.7F)
				.downfall(0.8F)
				.waterColor(4159204)
				.waterFogColor(329011)
				.parent((String) null));
		this.addStructureFeature(Feature.MINESHAFT.configure(new MineshaftFeatureConfig(0.004D, MineshaftFeature.Type.NORMAL)));
		this.addStructureFeature(Feature.STRONGHOLD.configure(FeatureConfig.DEFAULT));
        DefaultBiomeFeatures.addLandCarvers(this);
		DefaultBiomeFeatures.addDefaultStructures(this);
		DefaultBiomeFeatures.addDefaultLakes(this);
		DefaultBiomeFeatures.addDungeons(this);
        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(MubbleFeatures.LARGE_SCARLET_TREE.configure(new MegaTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState())).build()).withChance(0.6666667F), MubbleFeatures.TALL_SCARLET_TREE.configure(new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build()).withChance(0.1F), MubbleFeatures.SCARLET_GROUND_BUSH.configure(new TreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState())).build()).withChance(0.1F)), MubbleFeatures.SCARLET_TREE.configure(new BranchedTreeFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LOG.getDefaultState()), new SimpleStateProvider(MubbleBlocks.SCARLET_LEAVES.getDefaultState()), new BlobFoliagePlacer(2, 0)).build()).createDecoratedFeature(Decorator.DARK_OAK_TREE.configure(DecoratorConfig.DEFAULT)))));
        DefaultBiomeFeatures.addMineables(this);
		DefaultBiomeFeatures.addDefaultOres(this);
        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MubbleFeatures.SCARLET_FLOWERS.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(4))));
        DefaultBiomeFeatures.addForestGrass(this);
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_LEAF_PILE.getDefaultState()), new SimpleBlockPlacer()).build()).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(4))));
        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new SimpleStateProvider(MubbleBlocks.SCARLET_MUSHROOM.getDefaultState()), new SimpleBlockPlacer()).build()).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(8))));
        DefaultBiomeFeatures.addDefaultVegetation(this);
		DefaultBiomeFeatures.addSprings(this);
		DefaultBiomeFeatures.addFrozenTopLayer(this);
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.SHEEP, 12, 4, 4));
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.PIG, 10, 4, 4));
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.COW, 8, 4, 4));
        this.addSpawn(EntityCategory.AMBIENT, new Biome.SpawnEntry(EntityType.BAT, 10, 8, 8));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE, 95, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SKELETON, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.CREEPER, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SLIME, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.WITCH, 5, 1, 1));
    }
    
    @Override
    public int getGrassColorAt(double x, double z)
    {
        return 8720466;
    }
    
    @Override
    public int getFoliageColor()
    {
        return 10622269;
    }
}