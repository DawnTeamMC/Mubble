package hugman.mod.world.dimension;

import hugman.mod.init.MubbleBiomes;
import hugman.mod.init.MubbleDimensions;
import hugman.mod.util.Reference;
import hugman.mod.world.gen.chunks.EmptyChunkGenerator;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionUltimatum extends WorldProvider
{
	public DimensionUltimatum()
	{
		this.biomeProvider = new BiomeProviderSingle(MubbleBiomes.MUSHROOM_KINGDOM);
	}
	
	@Override
	public DimensionType getDimensionType() 
	{
		return MubbleDimensions.ULTIMATUM;
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
		return true;
	}
}