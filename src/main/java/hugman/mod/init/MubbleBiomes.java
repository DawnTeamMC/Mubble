package hugman.mod.init;

import hugman.mod.world.biomes.BiomeMushroomKingdom;
import hugman.mod.world.biomes.BiomeScarletForest;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/** 
 * Init class - used to initialize biomes.
 */
public class MubbleBiomes 
{
	public static final Biome MUSHROOM_KINGDOM = new BiomeMushroomKingdom();
	public static final Biome SCARLET_FOREST = new BiomeScarletForest();
	
	public static void registerBiomes()
	{
		initBiome(MUSHROOM_KINGDOM, "mushroom_kingdom", BiomeType.COOL, Type.SPARSE);
		initBiome(SCARLET_FOREST, "scarlet_forest", BiomeType.WARM, Type.FOREST);
	}
	
	private static Biome initBiome(Biome biome, String name, BiomeType biomeType, Type... types)
	{
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		BiomeManager.addBiome(biomeType, new BiomeEntry(biome, 10));
		BiomeManager.addSpawnBiome(biome);
		return biome;
	}
}