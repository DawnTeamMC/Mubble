package hugman.mubble.init.world;

import static net.minecraftforge.common.BiomeDictionary.Type.COLD;
import static net.minecraftforge.common.BiomeDictionary.Type.DENSE;
import static net.minecraftforge.common.BiomeDictionary.Type.DRY;
import static net.minecraftforge.common.BiomeDictionary.Type.FOREST;
import static net.minecraftforge.common.BiomeDictionary.Type.HOT;
import static net.minecraftforge.common.BiomeDictionary.Type.SANDY;
import static net.minecraftforge.common.BiomeDictionary.Type.SPOOKY;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.Mubble;
import hugman.mubble.objects.world.biome.AutumnOakForestBiome;
import hugman.mubble.objects.world.biome.PermafrostBiome;
import hugman.mubble.objects.world.biome.PinkCherryOakForestBiome;
import hugman.mubble.objects.world.biome.PressGardenBiome;
import hugman.mubble.objects.world.biome.SMWDesertBiome;
import hugman.mubble.objects.world.biome.ScarletForestBiome;
import hugman.mubble.objects.world.biome.WhiteCherryOakForestBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class MubbleBiomes
{
	public static final List<Biome> BIOMES = new ArrayList<Biome>();

	public static final Biome AUTUMN_OAK_FOREST = register("autumn_oak_forest", new AutumnOakForestBiome());
	public static final Biome PINK_CHERRY_OAK_FOREST = register("pink_cherry_oak_forest", new PinkCherryOakForestBiome());
	public static final Biome WHITE_CHERRY_OAK_FOREST = register("white_cherry_oak_forest", new WhiteCherryOakForestBiome());
	public static final Biome PERMAFROST = register("permafrost", new PermafrostBiome());

	public static final Biome SMW_DESERT = register("smw_desert", new SMWDesertBiome());

	public static final Biome PRESS_GARDEN = register("press_garden", new PressGardenBiome());
	
	public static final Biome SCARLET_FOREST = register("scarlet_forest", new ScarletForestBiome());
	
    public static Biome register(String name, Biome biome)
    {
    	Biome fBiome = biome.setRegistryName(Mubble.MOD_ID, name);
    	BIOMES.add(fBiome);
		return fBiome;
    }
    
    public static void registerGenerations()
    {
    	
    	BiomeDictionary.addTypes(AUTUMN_OAK_FOREST, FOREST);
    	BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(AUTUMN_OAK_FOREST, 10));
    	BiomeManager.addSpawnBiome(AUTUMN_OAK_FOREST);
    	
    	BiomeDictionary.addTypes(PINK_CHERRY_OAK_FOREST, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(PINK_CHERRY_OAK_FOREST, 5));
    	BiomeManager.addSpawnBiome(PINK_CHERRY_OAK_FOREST);
    	
    	BiomeDictionary.addTypes(WHITE_CHERRY_OAK_FOREST, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(WHITE_CHERRY_OAK_FOREST, 5));
    	BiomeManager.addSpawnBiome(WHITE_CHERRY_OAK_FOREST);
    	
    	BiomeDictionary.addTypes(PRESS_GARDEN, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(PRESS_GARDEN, 1));
    	BiomeManager.addSpawnBiome(PRESS_GARDEN);
    	
    	BiomeDictionary.addTypes(SMW_DESERT, HOT, DRY, SANDY);
    	
    	BiomeDictionary.addTypes(PERMAFROST, COLD, DRY);
    	
    	BiomeDictionary.addTypes(SCARLET_FOREST, SPOOKY, DENSE, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(SCARLET_FOREST, 6));
    }
}