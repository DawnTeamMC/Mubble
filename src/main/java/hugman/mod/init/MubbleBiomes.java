package hugman.mod.init;

import static net.minecraftforge.common.BiomeDictionary.Type.DENSE;
import static net.minecraftforge.common.BiomeDictionary.Type.FOREST;
import static net.minecraftforge.common.BiomeDictionary.Type.SPOOKY;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.world.biome.ScarletForestBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class MubbleBiomes
{	
	public static final List<Biome> BIOMES = new ArrayList<Biome>();
	
	public static final Biome SCARLET_FOREST = new ScarletForestBiome();
	
    public static void register(Biome biome)
    {
    	BIOMES.add(biome);
    }
    
    public static void registerGenerations()
    {
    	BiomeDictionary.addTypes(SCARLET_FOREST, SPOOKY, DENSE, FOREST);
    	BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(SCARLET_FOREST, 10));
    	BiomeManager.addSpawnBiome(SCARLET_FOREST);
	}
}