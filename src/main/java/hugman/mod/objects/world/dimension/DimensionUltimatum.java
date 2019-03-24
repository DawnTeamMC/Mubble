package hugman.mod.objects.world.dimension;

import hugman.mod.objects.world.chunk.ChunkGeneratorEmpty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionUltimatum extends Dimension
{
	private final DimensionType type;
	
	public DimensionUltimatum(DimensionType type) 
	{
		this.type = type;
	}
	
	@Override
	public IChunkGenerator<?> createChunkGenerator()
	{
		return new ChunkGeneratorEmpty(world, null);
	}
	@Override
	protected void init()
	{
		this.hasSkyLight = true;
	}
	
	@Override
	public BlockPos findSpawn(ChunkPos p_206920_1_, boolean checkValid)
	{
		return new BlockPos(0, 50, 0);
	}
	@Override
	public BlockPos findSpawn(int p_206921_1_, int p_206921_2_, boolean checkValid)
	{
		return new BlockPos(0, 50, 0);
	}
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0;
	}
	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}
	@Override
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return null;
	}
	@Override
	public boolean canRespawnHere()
	{
		return true;
	}
	
	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}
	@Override
	public DimensionType getType()
	{
		return this.type;
	}
}