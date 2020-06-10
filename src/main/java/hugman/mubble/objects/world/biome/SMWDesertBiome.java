package hugman.mubble.objects.world.biome;

import com.google.common.collect.Lists;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleEntities;
import hugman.mubble.init.data.MubbleBlockStateProperties;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class SMWDesertBiome extends Biome
{
	private static final BlockState SMW_DESERT_TOP = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, true);
	private static final BlockState SMW_DESERT_DIRT = MubbleBlocks.SMW_DESERT_GROUND_BLOCK.getDefaultState().with(MubbleBlockStateProperties.OVER, false);

	public SMWDesertBiome()
	{
		super((new Biome.Settings())
				.configureSurfaceBuilder(SurfaceBuilder.DEFAULT, MubbleSurfaceBuilders.SMW_DESERT_SURFACE)
				.precipitation(Biome.Precipitation.RAIN)
				.category(Biome.Category.FOREST)
				.depth(0.3625F)
				.scale(1.225F)
				.temperature(2.0F)
				.downfall(0.6F)
				.effects((new BiomeEffects.Builder()
						.waterColor(4159204)
						.waterFogColor(329011))
						.fogColor(12638463)
						.moodSound(BiomeMoodSound.CAVE)
						.build())
				.parent(null));
		this.addStructureFeature(DefaultBiomeFeatures.PILLAGER_OUTPOST);
		DefaultBiomeFeatures.method_28440(this);
		this.addStructureFeature(DefaultBiomeFeatures.DESERT_RUINED_PORTAL);
		DefaultBiomeFeatures.addLandCarvers(this);
		DefaultBiomeFeatures.addDefaultLakes(this);
		DefaultBiomeFeatures.addDungeons(this);
		DefaultBiomeFeatures.addMineables(this);
		DefaultBiomeFeatures.addDefaultOres(this);
		this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.DISK.configure(new DiskFeatureConfig(Blocks.GRAVEL.getDefaultState(), 6, 2, Lists.newArrayList(SMW_DESERT_TOP, SMW_DESERT_DIRT))).createDecoratedFeature(Decorator.COUNT_TOP_SOLID.configure(new CountDecoratorConfig(1))));
		DefaultBiomeFeatures.addDesertDeadBushes(this);
		DefaultBiomeFeatures.addDefaultMushrooms(this);
		DefaultBiomeFeatures.addDefaultVegetation(this);
		DefaultBiomeFeatures.addSprings(this);
		this.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.FOSSIL.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.CHANCE_PASSTHROUGH.configure(new ChanceDecoratorConfig(64))));
		DefaultBiomeFeatures.addFrozenTopLayer(this);
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.SHEEP, 12, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.PIG, 10, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
		this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.COW, 8, 4, 4));
		this.addSpawn(SpawnGroup.AMBIENT, new Biome.SpawnEntry(EntityType.BAT, 10, 8, 8));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(MubbleEntities.GOOMBA, 95, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE, 95, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SKELETON, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.CREEPER, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SLIME, 100, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.WITCH, 5, 1, 1));
	}

	@Override
	public int getGrassColorAt(double x, double z)
	{
		return 16110261;
	}

	@Override
	public int getSkyColor()
	{
		return 15323816;
	}
}