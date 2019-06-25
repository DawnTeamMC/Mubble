package hugman.mod.objects.world.dimension;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DimensionUltimatum extends Dimension
{
	private final DimensionType type;
	
	public DimensionUltimatum(World worldIn, DimensionType typeIn) 
	{
		super(worldIn, typeIn);
		this.type = typeIn;
		//this.setSpawnPoint(new BlockPos(0, 158, 0));
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
        FlatGenerationSettings settings = new FlatGenerationSettings();
        settings.setBiome(Biomes.PLAINS);
        settings.getFlatLayers().add(new FlatLayerInfo(1, Blocks.AIR));
        settings.updateLayers();
        
		BiomeProvider biomeProvider = BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings().setBiome(settings.getBiome()));
		
		return ChunkGeneratorType.FLAT.create(world, biomeProvider, settings);
	}
	
	@Override
	public BlockPos findSpawn(ChunkPos p_206920_1_, boolean checkValid)
	{
		return new BlockPos(0, 158, 0);
	}
	
	@Override
	public BlockPos findSpawn(int p_206921_1_, int p_206921_2_, boolean checkValid)
	{
		return new BlockPos(0, 158, 0);
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
	@OnlyIn(Dist.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
	      float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
	      f = MathHelper.clamp(f, 0.0F, 1.0F);
	      float f1 = 0.7529412F;
	      float f2 = 0.84705883F;
	      float f3 = 1.0F;
	      f1 = f1 * (f * 0.94F + 0.06F);
	      f2 = f2 * (f * 0.94F + 0.06F);
	      f3 = f3 * (f * 0.91F + 0.09F);
	      return new Vec3d((double)f1, (double)f2, (double)f3);
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