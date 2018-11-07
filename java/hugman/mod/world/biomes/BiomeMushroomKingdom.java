package hugman.mod.world.biomes;

import java.util.Random;

import hugman.mod.init.BlockInit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BiomeMushroomKingdom extends Biome 
{
	public BiomeMushroomKingdom() 
	{
		super(new BiomeProperties("Mushroom Kingdom").setBaseHeight(0.005F).setHeightVariation(0.05F).setTemperature(0.8F).setRainfall(0.4F).setWaterColor(65535));
		
		topBlock = Blocks.GRASS.getDefaultState();
		fillerBlock = Blocks.DIRT.getDefaultState();

        this.decorator.treesPerChunk = 0;
        this.decorator.extraTreeChance = 0.1F;
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 10;
		
		this.spawnableWaterCreatureList.clear();
	}
	
    @Override
    public void addDefaultFlowers()
    {
        addFlower(BlockInit.FIRE_FLOWER.getDefaultState(), 20);
        addFlower(BlockInit.ICE_FLOWER.getDefaultState(), 20);
        addFlower(BlockInit.CLOUD_FLOWER.getDefaultState(), 20);
        addFlower(BlockInit.BOOMERANG_FLOWER.getDefaultState(), 20);
        addFlower(BlockInit.GOLD_FLOWER.getDefaultState(), 20);
    }
    
    public void addBlocks(World worldIn, Random rand, BlockPos pos)
    {
        IBlockState question_block = BlockInit.QUESTION_BLOCK.getDefaultState();
        IBlockState brick_block = BlockInit.BRICK_BLOCK.getDefaultState();
        for (int i = 0; i < 2; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                int x = i * 4 + 1 + 8 + rand.nextInt(3);
                int z = j * 4 + 1 + 8 + rand.nextInt(3);
                BlockPos blockPos = worldIn.getHeight(pos.add(x, 0, z));
                int y = rand.nextInt(1) + 3;
                int luck = rand.nextInt(10);
                if (luck == 0)
                {
                	worldIn.setBlockState(blockPos.add(0, y, 0), question_block);
                	for (int loop = 0; loop < rand.nextInt(6) + 1; loop++)
                	{
                    	luck = rand.nextInt(10) + 1;
                    	if(luck == 0) worldIn.setBlockState(blockPos.add(0, y, 0).north(), brick_block);
                    	else if(luck == 1) worldIn.setBlockState(blockPos.add(0, y, 0).south(), brick_block);
                    	else if(luck == 2) worldIn.setBlockState(blockPos.add(0, y, 0).east(), brick_block);
                    	else if(luck == 3) worldIn.setBlockState(blockPos.add(0, y, 0).west(), brick_block);
                	}
                }
            }
        }
    }
    
    public void addFlowers(World worldIn, Random rand1, BlockPos posIn)
    {
        IBlockState fire=BlockInit.FIRE_FLOWER.getDefaultState();
        IBlockState ice=BlockInit.ICE_FLOWER.getDefaultState();
        IBlockState boomerang=BlockInit.BOOMERANG_FLOWER.getDefaultState();
        IBlockState cloud=BlockInit.CLOUD_FLOWER.getDefaultState();
        for (int i = 0; i < 2; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                int x = i * 4 + 1 + 8 + rand1.nextInt(3);
                int z = j * 4 + 1 + 8 + rand1.nextInt(3);
                BlockPos blockPos = worldIn.getHeight(posIn.add(x, 0, z));
                int flowerType = rand1.nextInt(4);
                if(worldIn.getBlockState(blockPos.down()).getBlock() == Blocks.GRASS || worldIn.getBlockState(blockPos.down()).getBlock() == Blocks.DIRT)
                {
                	switch (flowerType)
                    {
                      case 0:
                    	  worldIn.setBlockState(blockPos, fire);
                      break;
                      case 1:
                    	  worldIn.setBlockState(blockPos, ice);
                      break;
                      case 2:
                    	  worldIn.setBlockState(blockPos, boomerang);
                      break;
                      case 3:
                    	  worldIn.setBlockState(blockPos, cloud);
                      break;
                      default:
                    }
                }
            }
        }
    }
    
    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        this.addBlocks(worldIn, rand, pos);
        this.addFlowers(worldIn, rand, pos);
        super.decorate(worldIn, rand, pos);
    }
}