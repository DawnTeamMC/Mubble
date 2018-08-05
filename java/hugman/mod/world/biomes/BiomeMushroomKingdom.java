package hugman.mod.world.biomes;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeMushroomKingdom extends Biome 
{
	public BiomeMushroomKingdom() 
	{
		super(new BiomeProperties("Mushroom Kingdom").setBaseHeight(0.125F).setHeightVariation(0.5F).setTemperature(0.8F).setRainfall(0.4F).setWaterColor(65535));
		
		topBlock = Blocks.GRASS.getDefaultState();
		fillerBlock = Blocks.DIRT.getDefaultState();
		
		this.decorator.treesPerChunk = 0;
		
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
	}
}