package hugman.mod.world.dimension.nintendo;

import hugman.mod.init.DimensionInit;
import hugman.mod.world.dimension.EmptyChunkGenerator;
import net.minecraft.init.Biomes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionNintendo extends WorldProvider
{
	public DimensionNintendo()
	{
		this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
	}
	
	@Override
	public DimensionType getDimensionType() 
	{
		return DimensionInit.NINTENDO;
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() 
	{
		return new EmptyChunkGenerator(world);
	}
	
	@Override
	public boolean canRespawnHere() 
	{
		return true;
	}
	
	@Override
	public boolean isSurfaceWorld() 
	{
		return false;
	}
}