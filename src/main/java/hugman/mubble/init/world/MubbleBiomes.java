package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.biome.SMWDesertBiome;
import hugman.mubble.objects.world.biome.the_nether.*;
import hugman.mubble.objects.world.biome.overworld.*;
import net.fabricmc.fabric.api.biomes.v1.FabricBiomes;
import net.fabricmc.fabric.api.biomes.v1.NetherBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class MubbleBiomes {
	public static final Biome PUMPKIN_PASTURES = register("pumpkin_pastures", new PumpkinPasturesBiome());
	public static final Biome PINK_CHERRY_OAK_FOREST = register("pink_cherry_oak_forest", new PinkCherryOakForestBiome());
	public static final Biome WHITE_CHERRY_OAK_FOREST = register("white_cherry_oak_forest", new WhiteCherryOakForestBiome());
	public static final Biome TALL_CRIMSON_FOREST = register("tall_crimson_forest", new TallCrimsonForestBiome());
	public static final Biome TALL_WARPED_FOREST = register("tall_warped_forest", new TallWarpedForestBiome());
	public static final Biome TRITANOPIAN_GALLERY = register("tritanopian_gallery", new TritanopianGalleryBiome());
	public static final Biome ACHROMATOPSIAN_GALLERY = register("achromatopsian_gallery", new AchromatopsianGalleryBiome());
	public static final Biome PROTANOPIAN_GALLERY = register("protanopian_gallery", new ProtanopianGalleryBiome());

	public static final Biome SMW_DESERT = register("smw_desert", new SMWDesertBiome());

	public static final Biome PRESS_GARDEN = register("press_garden", new PressGardenBiome());

	public static final Biome SCARLET_FOREST = register("scarlet_forest", new ScarletForestBiome());

	private static Biome register(String name, Biome biome) {
		return Registry.register(Registry.BIOME, new Identifier(Mubble.MOD_ID, name), biome);
	}

	public static void initBiomeGeneration() {
		OverworldBiomes.addContinentalBiome(PUMPKIN_PASTURES, OverworldClimate.TEMPERATE, 1D);
		FabricBiomes.addSpawnBiome(PUMPKIN_PASTURES);
		OverworldBiomes.addContinentalBiome(PINK_CHERRY_OAK_FOREST, OverworldClimate.COOL, 1D);
		FabricBiomes.addSpawnBiome(PINK_CHERRY_OAK_FOREST);
		OverworldBiomes.addContinentalBiome(WHITE_CHERRY_OAK_FOREST, OverworldClimate.COOL, 1D);
		FabricBiomes.addSpawnBiome(WHITE_CHERRY_OAK_FOREST);
		OverworldBiomes.addContinentalBiome(PRESS_GARDEN, OverworldClimate.SNOWY, 0.25D);
		FabricBiomes.addSpawnBiome(PRESS_GARDEN);
		OverworldBiomes.addContinentalBiome(SCARLET_FOREST, OverworldClimate.COOL, 1D);
		NetherBiomes.addNetherBiome(TALL_CRIMSON_FOREST);
		NetherBiomes.addNetherBiome(TALL_WARPED_FOREST);
		NetherBiomes.addNetherBiome(TRITANOPIAN_GALLERY);
		NetherBiomes.addNetherBiome(ACHROMATOPSIAN_GALLERY);
		NetherBiomes.addNetherBiome(PROTANOPIAN_GALLERY);
	}
}