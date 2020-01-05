package hugman.mubble.init.world;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.biome.AutumnOakForestBiome;
import hugman.mubble.objects.world.biome.PermafrostBiome;
import hugman.mubble.objects.world.biome.PinkCherryOakForestBiome;
import hugman.mubble.objects.world.biome.PressGardenBiome;
import hugman.mubble.objects.world.biome.SMWDesertBiome;
import hugman.mubble.objects.world.biome.ScarletForestBiome;
import hugman.mubble.objects.world.biome.WhiteCherryOakForestBiome;
import net.fabricmc.fabric.api.biomes.v1.FabricBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class MubbleBiomes
{
	public static final Biome AUTUMN_OAK_FOREST = register("autumn_oak_forest", new AutumnOakForestBiome());
	public static final Biome PINK_CHERRY_OAK_FOREST = register("pink_cherry_oak_forest", new PinkCherryOakForestBiome());
	public static final Biome WHITE_CHERRY_OAK_FOREST = register("white_cherry_oak_forest", new WhiteCherryOakForestBiome());
	public static final Biome PERMAFROST = register("permafrost", new PermafrostBiome());

	public static final Biome SMW_DESERT = register("smw_desert", new SMWDesertBiome());

	public static final Biome PRESS_GARDEN = register("press_garden", new PressGardenBiome());
	
	public static final Biome SCARLET_FOREST = register("scarlet_forest", new ScarletForestBiome());
	
	private static Biome register(String name, Biome biome)
    {
		return Registry.register(Registry.BIOME, new Identifier(Mubble.MOD_ID, name), biome);
    }
    
    public static void initBiomeGeneration()
    {
    	OverworldBiomes.addContinentalBiome(AUTUMN_OAK_FOREST, OverworldClimate.TEMPERATE, 10);
    	FabricBiomes.addSpawnBiome(AUTUMN_OAK_FOREST);
    	
    	OverworldBiomes.addContinentalBiome(PINK_CHERRY_OAK_FOREST, OverworldClimate.COOL, 5);
    	FabricBiomes.addSpawnBiome(PINK_CHERRY_OAK_FOREST);
    	
    	OverworldBiomes.addContinentalBiome(WHITE_CHERRY_OAK_FOREST, OverworldClimate.COOL, 5);
    	FabricBiomes.addSpawnBiome(WHITE_CHERRY_OAK_FOREST);
    	
    	OverworldBiomes.addContinentalBiome(PRESS_GARDEN, OverworldClimate.COOL, 1);
    	FabricBiomes.addSpawnBiome(PRESS_GARDEN);
    	
    	OverworldBiomes.addContinentalBiome(SCARLET_FOREST, OverworldClimate.COOL, 6);
    }
}