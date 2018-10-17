package hugman.mod.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import hugman.mod.world.biomes.BiomeMushroomKingdom;
import hugman.mod.world.gen.generators.WorldGenStructure;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCustomStructures implements IWorldGenerator
{
	public static final WorldGenStructure RED_TOAD_HOUSE = new WorldGenStructure("toad_house/red");
	public static final WorldGenStructure BLUE_TOAD_HOUSE = new WorldGenStructure("toad_house/blue");
	public static final WorldGenStructure GREEN_TOAD_HOUSE = new WorldGenStructure("toad_house/green");
	public static final WorldGenStructure STARSHROOM0 = new WorldGenStructure("starshroom/0");
	public static final WorldGenStructure STARSHROOM1 = new WorldGenStructure("starshroom/1");
	public static final WorldGenStructure STARSHROOM2 = new WorldGenStructure("starshroom/2");
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		if(world.provider.getDimension() == 0)
		{
			//Total for Toad House must be 90 or 100
			generateStructure(RED_TOAD_HOUSE, world, random, chunkX, chunkZ, 30, Blocks.GRASS, BiomeMushroomKingdom.class);
			generateStructure(BLUE_TOAD_HOUSE, world, random, chunkX, chunkZ, 30, Blocks.GRASS, BiomeMushroomKingdom.class);
			generateStructure(GREEN_TOAD_HOUSE, world, random, chunkX, chunkZ, 30, Blocks.GRASS, BiomeMushroomKingdom.class);
			//Total for Starshroom must be 500
			generateStructure(STARSHROOM0, world, random, chunkX, chunkZ, 350, Blocks.GRASS, BiomeMushroomKingdom.class);
			generateStructure(STARSHROOM1, world, random, chunkX, chunkZ, 95, Blocks.GRASS, BiomeMushroomKingdom.class);
			generateStructure(STARSHROOM2, world, random, chunkX, chunkZ, 55, Blocks.GRASS, BiomeMushroomKingdom.class);
		}
	}
	
	private void generateStructure(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, int chance, Block topBlock, Class<?>... classes)
	{
		ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));
		
		int x = (chunkX * 16) + random.nextInt(15) + 8;
		int z = (chunkZ * 16) + random.nextInt(15) + 8;
		int y = calculateGenerationHeight(world, x, z, topBlock) - 1;
		BlockPos pos = new BlockPos(x,y,z);
		
		Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();
		
		if(world.getWorldType() != WorldType.FLAT)
		{
			if(classesList.contains(biome))
			{
				if(random.nextInt(chance) == 0)
				{
					generator.generate(world, random, pos);
				}
			}
		}
	}
	
	private void runGenerator(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, int chance, Block topBlock, Class<?>... classes)
	{
		ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));
		
		int x = (chunkX * 16) + random.nextInt(15) + 8;
		int z = (chunkZ * 16) + random.nextInt(15) + 8;
		int y = calculateGenerationHeight(world, x, z, topBlock);
		BlockPos pos = new BlockPos(x,y,z);
		
		Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();
		
		if(world.getWorldType() != WorldType.FLAT)
		{
			if(classesList.contains(biome))
			{
				if(random.nextInt(chance) == 0)
				{
					generator.generate(world, random, pos);
				}
			}
		}
	}
	
	private static int calculateGenerationHeight(World world, int x, int z, Block topBlock)
	{
		int y = world.getHeight();
		boolean foundGround = false;
		
		while(!foundGround && y-- >= 0)
		{
			Block block = world.getBlockState(new BlockPos(x,y,z)).getBlock();
			foundGround = block == topBlock;
		}
		
		return y;
	}
}