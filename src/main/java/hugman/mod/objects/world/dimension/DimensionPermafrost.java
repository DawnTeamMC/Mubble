package hugman.mod.objects.world.dimension;

import hugman.mod.init.MubbleBiomes;
import hugman.mod.init.MubbleBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NetherGenSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DimensionPermafrost extends Dimension
{
	private final DimensionType type;
	
	public DimensionPermafrost(DimensionType type) 
	{
		this.type = type;
	}
	
	@Override
	public IChunkGenerator<?> createChunkGenerator()
	{
		NetherGenSettings settings = ChunkGeneratorType.CAVES.createSettings();
		settings.setDefautBlock(MubbleBlocks.PERMAROCK.getDefaultState());
		settings.setDefaultFluid(Blocks.WATER.getDefaultState());
		
		BiomeProvider biomeProvider = BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings().setBiome(MubbleBiomes.PERMAFROST));
		
		return ChunkGeneratorType.CAVES.create(this.world, biomeProvider, settings);
	}
	
	@Override
	protected void init()
	{
		this.hasSkyLight = false;
	}
	
	@Override
	public BlockPos findSpawn(ChunkPos p_206920_1_, boolean checkValid)
	{
		return null;
	}
	
	@Override
	public BlockPos findSpawn(int p_206921_1_, int p_206921_2_, boolean checkValid)
	{
		return null;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0.5f;
	}
	
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3d((double)0.03F, (double)0.2F, (double)0.2F);
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		for(int i = 0; i <= 15; ++i)
		{
			float f1 = 1.0F - (float)i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.9F + 0.1F;
		}
	}
	
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	
	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return true;
	}
	
	@Override
	public WorldBorder createWorldBorder()
	{
		return new WorldBorder()
		{
			public double getCenterX()
			{
				return super.getCenterX() / 8.0D;
			}

			public double getCenterZ()
			{
				return super.getCenterZ() / 8.0D;
			}
		};
	}
	
	@Override
	public DimensionType getType()
	{
		return this.type;
	}
}