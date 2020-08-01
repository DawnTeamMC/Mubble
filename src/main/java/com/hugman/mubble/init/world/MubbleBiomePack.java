package com.hugman.mubble.init.world;

import com.hugman.dawn.api.creator.BiomeCreator.Builder;
import com.hugman.mubble.init.MubblePack;
import com.hugman.mubble.util.MubbleBiomeCreator;
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate;
import net.minecraft.world.biome.Biome;

public class MubbleBiomePack extends MubblePack {
	public static final Biome PUMPKIN_PASTURES = register(new Builder("pumpkin_pastures", MubbleBiomeCreator.createPumpkinPastures()).addToOverworldContinental(OverworldClimate.TEMPERATE, 1D, true));
	public static final Biome PINK_CHERRY_OAK_FOREST = register(new Builder("pink_cherry_oak_forest", MubbleBiomeCreator.createCherryOakForest(true)).addToOverworldContinental(OverworldClimate.COOL, 1D, true));
	public static final Biome WHITE_CHERRY_OAK_FOREST = register(new Builder("white_cherry_oak_forest", MubbleBiomeCreator.createCherryOakForest(false)).addToOverworldContinental(OverworldClimate.COOL, 1D, true));
	public static final Biome TALL_CRIMSON_FOREST = register(new Builder("tall_crimson_forest", MubbleBiomeCreator.createTallCrimsonForest()).addToNether(0.4F, 0.0F, 0.1F, 0.0F, 0.0F));
	public static final Biome TALL_WARPED_FOREST = register(new Builder("tall_warped_forest", MubbleBiomeCreator.createTallWarpedForest()).addToNether(0.0F, 0.5F, 0.1F, 0.0F, 0.375F));
	public static final Biome TRITANOPIAN_GALLERY = register(new Builder("tritanopian_gallery", MubbleBiomeCreator.createGallery(MubbleBiomeCreator.createTritanopianGalleryGenerationSettings())).addToNether(0.05F, 0.025F, 0.0F, 0.0F, 0.05F));
	public static final Biome ACHROMATOPSIAN_GALLERY = register(new Builder("achromatopsian_gallery", MubbleBiomeCreator.createGallery(MubbleBiomeCreator.createAchromatopsianGalleryGenerationSettings())).addToNether(0.1F, 0.05F, 0.0F, 0.0F, 0.025F));
	public static final Biome PROTANOPIAN_GALLERY = register(new Builder("protanopian_gallery", MubbleBiomeCreator.createGallery(MubbleBiomeCreator.createProtanopianGalleryGenerationSettings())).addToNether(0.025F, 0.1F, 0.0F, 0.0F, 0.05F));
	//public static final Biome DARK_AMARANTH_FOREST = register("dark_amaranth_forest", new DarkAmaranthForest());
	//public static final Biome TALL_DARK_AMARANTH_FOREST = register("tall_dark_amaranth_forest", new TallDarkAmaranthForest());
	//public static final Biome PRESS_GARDEN = register("press_garden", new PressGardenBiome());
	//public static final Biome SCARLET_FOREST = register("scarlet_forest", new ScarletForestBiome());

	public static void initBiomeGeneration() {
		//OverworldBiomes.addContinentalBiome(PRESS_GARDEN, OverworldClimate.SNOWY, 0.25D);
		//FabricBiomes.addSpawnBiome(PRESS_GARDEN);
		//OverworldBiomes.addContinentalBiome(SCARLET_FOREST, OverworldClimate.COOL, 1D);
	}
}