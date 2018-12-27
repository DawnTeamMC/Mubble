package hugman.mod.world.dimension.smash;

import hugman.mod.init.BiomeInit;
import hugman.mod.init.DimensionInit;
import hugman.mod.util.Reference;
import hugman.mod.world.dimension.EmptyChunkGenerator;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionWorldOfLight extends WorldProvider
{
	public DimensionWorldOfLight()
	{
		this.biomeProvider = new BiomeProviderSingle(BiomeInit.MUSHROOM_KINGDOM);
	}
	
	@Override
	public DimensionType getDimensionType() 
	{
		return DimensionInit.WORLD_OF_LIGHT;
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
	
	@Override
	public String getSaveFolder()
	{
		return Reference.MODID + ":worlds/wol";
	}
}