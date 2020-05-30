package hugman.mubble.objects.world.biome;

import com.google.common.collect.ImmutableList;
import hugman.mubble.init.world.MubbleFeatureConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class WhiteCherryOakForestBiome extends Biome
{
	public WhiteCherryOakForestBiome()
	{
		super((new Biome.Settings()).configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG)
				.precipitation(Biome.Precipitation.RAIN)
				.category(Biome.Category.FOREST)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(0.6F)
				.downfall(0.4F)
				.effects((new BiomeEffects.Builder()
						.waterColor(6459391)
						.waterFogColor(2170954))
						.fogColor(12638463)
						.moodSound(BiomeMoodSound.CAVE)
						.build())
				.parent(null));
		this.addStructureFeature(DefaultBiomeFeatures.field_24687);
		DefaultBiomeFeatures.method_28440(this);
		this.addStructureFeature(DefaultBiomeFeatures.field_24711);
		DefaultBiomeFeatures.addLandCarvers(this);
		DefaultBiomeFeatures.addDefaultLakes(this);
		DefaultBiomeFeatures.addDungeons(this);
		DefaultBiomeFeatures.addForestFlowers(this);
		DefaultBiomeFeatures.addMineables(this);
		DefaultBiomeFeatures.addDefaultOres(this);
		DefaultBiomeFeatures.addDefaultDisks(this);
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(Feature.TREE.configure(MubbleFeatureConfigs.PINK_CHERRY_OAK_TREE_CONFIG).withChance(0.2F), Feature.TREE.configure(MubbleFeatureConfigs.FANCY_WHITE_CHERRY_OAK_TREE_CONFIG).withChance(0.1F)), Feature.TREE.configure(MubbleFeatureConfigs.WHITE_CHERRY_OAK_TREE_CONFIG))).createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(10, 0.1F, 1))));
		DefaultBiomeFeatures.addDefaultFlowers(this);
		DefaultBiomeFeatures.addForestGrass(this);
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.WHITE_CHERRY_OAK_LEAF_PILE_CONFIG).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_DOUBLE.configure(new CountDecoratorConfig(4))));
		DefaultBiomeFeatures.addDefaultMushrooms(this);
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.WHITE_MUSHROOM_CONFIG).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(8))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(MubbleFeatureConfigs.LIGHT_GRAY_MUSHROOM_CONFIG).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(8))));
		DefaultBiomeFeatures.addDefaultVegetation(this);
		DefaultBiomeFeatures.addSprings(this);
		DefaultBiomeFeatures.addFrozenTopLayer(this);
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.SHEEP, 12, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.PIG, 10, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.COW, 8, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.WOLF, 5, 4, 4));
		this.addSpawn(SpawnGroup.AMBIENT, new Biome.SpawnEntry(EntityType.BAT, 10, 8, 8));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE, 95, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SKELETON, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.CREEPER, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SLIME, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.WITCH, 5, 1, 1));
	}

	@Override
	public int getFoliageColor()
	{
		return 15786729;
	}
}