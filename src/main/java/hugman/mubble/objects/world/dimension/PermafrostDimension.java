package hugman.mubble.objects.world.dimension;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.world.MubbleBiomes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

public class PermafrostDimension extends Dimension
{
	private final DimensionType type;
	
	public PermafrostDimension(World worldIn, DimensionType typeIn)
	{
		super(worldIn, typeIn, 0.1F);
		this.type = typeIn;
		
		for(int i = 0; i <= 15; ++i)
		{
			float f1 = 1.0F - (float)i / 15.0F;
			this.lightLevelToBrightness[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.9F + 0.1F;
		}
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
		CavesChunkGeneratorConfig settings = ChunkGeneratorType.CAVES.createSettings();
		settings.setDefaultBlock(MubbleBlocks.PERMAROCK.getDefaultState());
		settings.setDefaultFluid(Blocks.WATER.getDefaultState());
		
		FixedBiomeSource biomeProvider = BiomeSourceType.FIXED.applyConfig(BiomeSourceType.FIXED.getConfig(this.world.getLevelProperties()).setBiome(MubbleBiomes.PERMAFROST));
		
		return ChunkGeneratorType.CAVES.create(this.world, biomeProvider, settings);
	}
	
	@Override
	public BlockPos getSpawningBlockInChunk(ChunkPos chunkPosIn, boolean checkValid)
	{
		return null;
	}
	
	@Override
	public BlockPos getTopSpawningBlockPosition(int posX, int posZ, boolean checkValid)
	{
		return null;
	}
	
	@Override
	public float getSkyAngle(long worldTime, float partialTicks)
	{
		return 0.5f;
	}
	
	@Override
	public boolean hasVisibleSky()
	{
		return false;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Vec3d getFogColor(float celestialAngle, float partialTicks)
	{
		return new Vec3d((double) 0.03F, (double) 0.2F, (double) 0.2F);
	}
	
	@Override
	public boolean canPlayersSleep()
	{
		return false;
	}
	
	@Override
	public boolean isFogThick(int x, int z)
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