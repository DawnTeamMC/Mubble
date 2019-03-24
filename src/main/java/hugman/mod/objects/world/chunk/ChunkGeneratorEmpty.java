package hugman.mod.objects.world.chunk;

import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.AbstractChunkGenerator;
import net.minecraft.world.gen.FlatGenSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;

public class ChunkGeneratorEmpty extends AbstractChunkGenerator<FlatGenSettings>
{
   public ChunkGeneratorEmpty(IWorld worldIn, BiomeProvider biomeProviderIn)
   {
      super(worldIn, biomeProviderIn);
   }

   @Override
   public void makeBase(IChunk chunkIn) {}

   @Override
   public void carve(WorldGenRegion region, GenerationStage.Carving carvingStage) {}

   @Override
   public double[] generateNoiseRegion(int x, int z)
   {
	   return null;
   }

   @Override
   public void spawnMobs(WorldGenRegion region) {}
   
   @Override
   public int spawnMobs(World worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs)
   {
	   return 0;
   }

   @Override
   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
   {
	   return null;
   }

   @Override
   public int getGroundHeight()
   {
	   return 0;
   }

   @Override
   public FlatGenSettings getSettings()
   {
	   return null;
   }
}