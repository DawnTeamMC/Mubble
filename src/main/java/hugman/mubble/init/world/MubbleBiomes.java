package hugman.mubble.init.world;

import com.google.gson.JsonSyntaxException;
import hugman.mubble.Mubble;
import net.fabricmc.fabric.api.biomes.v1.NetherBiomes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;

public class MubbleBiomes {
	/*
	public static final Biome PUMPKIN_PASTURES = register("pumpkin_pastures", new PumpkinPasturesBiome());
	public static final Biome PINK_CHERRY_OAK_FOREST = register("pink_cherry_oak_forest", new PinkCherryOakForestBiome());
	public static final Biome WHITE_CHERRY_OAK_FOREST = register("white_cherry_oak_forest", new WhiteCherryOakForestBiome());
	public static final Biome TALL_CRIMSON_FOREST = register("tall_crimson_forest", new TallCrimsonForestBiome());
	public static final Biome TALL_WARPED_FOREST = register("tall_warped_forest", new TallWarpedForestBiome());
	public static final Biome TRITANOPIAN_GALLERY = register("tritanopian_gallery", new TritanopianGalleryBiome());
	public static final Biome ACHROMATOPSIAN_GALLERY = register("achromatopsian_gallery", new AchromatopsianGalleryBiome());
	public static final Biome PROTANOPIAN_GALLERY = register("protanopian_gallery", new ProtanopianGalleryBiome());
	public static final Biome DARK_AMARANTH_FOREST = register("dark_amaranth_forest", new DarkAmaranthForest());
	public static final Biome TALL_DARK_AMARANTH_FOREST = register("tall_dark_amaranth_forest", new TallDarkAmaranthForest());

	public static final Biome SMW_DESERT = register("smw_desert", new SMWDesertBiome());

	public static final Biome PRESS_GARDEN = register("press_garden", new PressGardenBiome());

	public static final Biome SCARLET_FOREST = register("scarlet_forest", new ScarletForestBiome());

	private static Biome register(String name, Biome biome) {
		return Registry.register(BuiltinRegistries.BIOME, new Identifier(Mubble.MOD_ID, name), biome);
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
		NetherBiomes.addNetherBiome(TALL_CRIMSON_FOREST, new Biome.MixedNoisePoint(0.4F, 0.0F, 0.1F, 0.0F, 0.0F));
		NetherBiomes.addNetherBiome(TALL_WARPED_FOREST, new Biome.MixedNoisePoint(0.0F, 0.5F, 0.1F, 0.0F, 0.375F));
		NetherBiomes.addNetherBiome(TRITANOPIAN_GALLERY, new Biome.MixedNoisePoint(0.05F, 0.025F, 0.0F, 0.0F, 0.05F));
		NetherBiomes.addNetherBiome(ACHROMATOPSIAN_GALLERY, new Biome.MixedNoisePoint(0.1F, 0.05F, 0.0F, 0.0F, 0.025F));
		NetherBiomes.addNetherBiome(PROTANOPIAN_GALLERY, new Biome.MixedNoisePoint(0.025F, 0.1F, 0.0F, 0.0F, 0.05F));
	}

	 */

	public static void addNetherBiome(String name)
	{
		Identifier id = new Identifier(Mubble.MOD_ID, name);
		NetherBiomes.addNetherBiome(BuiltinRegistries.BIOME.getOrEmpty(id).orElseThrow(() -> {
			return new JsonSyntaxException("Unknown biome '" + id + "'");
		}), new Biome.MixedNoisePoint(0.4F, 0.0F, 0.1F, 0.0F, 0.0F));

	}
}