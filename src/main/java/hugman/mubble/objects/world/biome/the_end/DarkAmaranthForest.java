package hugman.mubble.objects.world.biome.the_end;

import hugman.mubble.init.world.MubbleFeatureConfigs;
import hugman.mubble.init.world.MubbleFeatures;
import hugman.mubble.init.world.MubbleSurfaceBuilders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class DarkAmaranthForest extends Biome {
	public DarkAmaranthForest() {
		super((new Biome.Settings())
				.configureSurfaceBuilder(SurfaceBuilder.DEFAULT, MubbleSurfaceBuilders.AMARANTH_DYLIUM_SURFACE)
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.THEEND)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(0.5F)
				.downfall(0.5F)
				.effects((new BiomeEffects.Builder())
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(10518688)
						.moodSound(BiomeMoodSound.CAVE)
						.build())
				.parent(null));
		this.addStructureFeature(DefaultBiomeFeatures.END_CITY);
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.HUGE_FUNGUS.configure(MubbleFeatureConfigs.AMARANTH_FUNGUS_NOT_PLANTED_CONFIG).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(8))));
		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.NETHER_FOREST_VEGETATION.configure(MubbleFeatureConfigs.AMARANTH_ROOTS_CONFIG).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP.configure(new CountDecoratorConfig(6))));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SPIDER, 10, 4, 4));
		this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.CAVE_SPIDER, 10, 4, 4));
	}

	@Environment(EnvType.CLIENT)
	public int getSkyColor() {
		return 0;
	}
}
