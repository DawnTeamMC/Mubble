package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.util.MoreWordUtils;
import hugman.mubble.util.MubbleBiomeCreator;
import hugman.mubble.util.entry.BiomeEntry;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class MubbleBiomes {
	//public static final Biome PUMPKIN_PASTURES = register("pumpkin_pastures", new PumpkinPasturesBiome());
	//public static final Biome PINK_CHERRY_OAK_FOREST = register("pink_cherry_oak_forest", new PinkCherryOakForestBiome());
	//public static final Biome WHITE_CHERRY_OAK_FOREST = register("white_cherry_oak_forest", new WhiteCherryOakForestBiome());
	public static final Biome TALL_CRIMSON_FOREST = new BiomeEntry.Builder("tall_crimson_forest", MubbleBiomeCreator.createTallNetherForest(false)).addToNether(0.4F, 0.0F, 0.1F, 0.0F, 0.0F).build();
	public static final Biome TALL_WARPED_FOREST = new BiomeEntry.Builder("tall_warped_forest", MubbleBiomeCreator.createTallNetherForest(true)).addToNether(0.0F, 0.5F, 0.1F, 0.0F, 0.375F).build();
	public static final Biome TRITANOPIAN_GALLERY = new BiomeEntry.Builder("tritanopian_gallery", MubbleBiomeCreator.createTritanopianGallery()).addToNether(0.05F, 0.025F, 0.0F, 0.0F, 0.05F).build();
	public static final Biome ACHROMATOPSIAN_GALLERY = new BiomeEntry.Builder("achromatopsian_gallery", MubbleBiomeCreator.createAchromatopsianGallery()).addToNether(0.1F, 0.05F, 0.0F, 0.0F, 0.025F).build();
	public static final Biome PROTANOPIAN_GALLERY = new BiomeEntry.Builder("protanopian_gallery", MubbleBiomeCreator.createProtanopianGallery()).addToNether(0.025F, 0.1F, 0.0F, 0.0F, 0.05F).build();
	//public static final Biome DARK_AMARANTH_FOREST = register("dark_amaranth_forest", new DarkAmaranthForest());
	//public static final Biome TALL_DARK_AMARANTH_FOREST = register("tall_dark_amaranth_forest", new TallDarkAmaranthForest());
	//public static final Biome PRESS_GARDEN = register("press_garden", new PressGardenBiome());
	//public static final Biome SCARLET_FOREST = register("scarlet_forest", new ScarletForestBiome());

	public static void initBiomeGeneration() {
		//OverworldBiomes.addContinentalBiome(PUMPKIN_PASTURES, OverworldClimate.TEMPERATE, 1D);
		//FabricBiomes.addSpawnBiome(PUMPKIN_PASTURES);
		//OverworldBiomes.addContinentalBiome(PINK_CHERRY_OAK_FOREST, OverworldClimate.COOL, 1D);
		//FabricBiomes.addSpawnBiome(PINK_CHERRY_OAK_FOREST);
		//OverworldBiomes.addContinentalBiome(WHITE_CHERRY_OAK_FOREST, OverworldClimate.COOL, 1D);
		//FabricBiomes.addSpawnBiome(WHITE_CHERRY_OAK_FOREST);
		//OverworldBiomes.addContinentalBiome(PRESS_GARDEN, OverworldClimate.SNOWY, 0.25D);
		//FabricBiomes.addSpawnBiome(PRESS_GARDEN);
		//OverworldBiomes.addContinentalBiome(SCARLET_FOREST, OverworldClimate.COOL, 1D);
	}
}