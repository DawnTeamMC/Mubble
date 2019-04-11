package hugman.mod.init;

import static net.minecraftforge.common.BiomeDictionary.Type.COLD;
import static net.minecraftforge.common.BiomeDictionary.Type.DENSE;
import static net.minecraftforge.common.BiomeDictionary.Type.DRY;
import static net.minecraftforge.common.BiomeDictionary.Type.FOREST;
import static net.minecraftforge.common.BiomeDictionary.Type.SPOOKY;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.world.biome.AutumnOakForestBiome;
import hugman.mod.objects.world.biome.PermafrostBiome;
import hugman.mod.objects.world.biome.PinkCherryOakForestBiome;
import hugman.mod.objects.world.biome.ScarletForestBiome;
import hugman.mod.objects.world.biome.WhiteCherryOakForestBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class MubbleBiomes
{	
	public static final List<Biome> BIOMES = new ArrayList<Biome>();

	public static final Biome AUTUMN_OAK_FOREST = new AutumnOakForestBiome();
	public static final Biome PINK_CHERRY_OAK_FOREST = new PinkCherryOakForestBiome();
	public static final Biome WHITE_CHERRY_OAK_FOREST = new WhiteCherryOakForestBiome();
	public static final Biome PERMAFROST = new PermafrostBiome();
	
	public static final Biome SCARLET_FOREST = new ScarletForestBiome();
	
    public static void register(Biome biome)
    {
    	BIOMES.add(biome);
    }
    
    public static void registerGenerations()
    {
    	BiomeDictionary.addTypes(SCARLET_FOREST, SPOOKY, DENSE, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(SCARLET_FOREST, 3));
    	
    	BiomeDictionary.addTypes(AUTUMN_OAK_FOREST, FOREST);
    	BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(AUTUMN_OAK_FOREST, 5));
    	BiomeManager.addSpawnBiome(AUTUMN_OAK_FOREST);
    	
    	BiomeDictionary.addTypes(PINK_CHERRY_OAK_FOREST, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(PINK_CHERRY_OAK_FOREST, 5));
    	BiomeManager.addSpawnBiome(PINK_CHERRY_OAK_FOREST);
    	
    	BiomeDictionary.addTypes(WHITE_CHERRY_OAK_FOREST, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(WHITE_CHERRY_OAK_FOREST, 5));
    	BiomeManager.addSpawnBiome(WHITE_CHERRY_OAK_FOREST);
    	
    	BiomeDictionary.addTypes(PERMAFROST, COLD, DRY);
	}
}