package hugman.mod.world.gen;

import java.util.Random;

import hugman.mod.init.BlockInit;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCustomOres implements IWorldGenerator
{
	private WorldGenerator vanadium_ore, blunite, carbonite, unstable_stone;
	
	public WorldGenCustomOres()
	{
		vanadium_ore = new WorldGenMinable(BlockInit.VANADIUM_ORE.getDefaultState(), 6, BlockMatcher.forBlock(Blocks.STONE));
		blunite = new WorldGenMinable(BlockInit.BLUNITE.getDefaultState(), 33, BlockMatcher.forBlock(Blocks.STONE));
		carbonite = new WorldGenMinable(BlockInit.CARBONITE.getDefaultState(), 33, BlockMatcher.forBlock(Blocks.STONE));
		unstable_stone = new WorldGenMinable(BlockInit.UNSTABLE_STONE.getDefaultState(), 10, BlockMatcher.forBlock(Blocks.STONE));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		switch(world.provider.getDimension())
		{
		case 0:
			
			runGenerator(vanadium_ore, world, random, chunkX, chunkZ, 1, 0, 16);
			runGenerator(blunite, world, random, chunkX, chunkZ, 10, 0, 80);
			runGenerator(carbonite, world, random, chunkX, chunkZ, 10, 0, 80);
			runGenerator(unstable_stone, world, random, chunkX, chunkZ, 5, 0, 255);
		}
	}
	
	private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight)
	{
		if(minHeight > maxHeight || minHeight < 0 || maxHeight > 256) throw new IllegalArgumentException("Ore generated out of bounds.");
		
		int heightDiff = maxHeight - minHeight + 1;
		for(int i = 0; i < chance; i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunkZ * 16 + rand.nextInt(16);
			
			gen.generate(world, rand, new BlockPos(x,y,z));
		}
	}
}
