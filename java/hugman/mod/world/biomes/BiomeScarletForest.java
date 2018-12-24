package hugman.mod.world.biomes;

import java.util.Random;

import hugman.mod.init.BlockInit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;

public class BiomeScarletForest extends Biome 
{
    protected static final WorldGenCanopyTree ROOF_TREE = new WorldGenCanopyTree(false);

	public BiomeScarletForest() 
	{
		super(new BiomeProperties("Scarlet Forest").setBaseHeight(0.005F).setHeightVariation(0.05F).setTemperature(0.8F).setRainfall(0.4F).setWaterColor(65535));
		
		topBlock = BlockInit.SCARLET_GRASS_BLOCK.getDefaultState();
		fillerBlock = Blocks.DIRT.getDefaultState();

		this.decorator.treesPerChunk = -999;
        this.decorator.treesPerChunk = 10;
        this.decorator.grassPerChunk = 0;
        this.decorator.flowersPerChunk = 0;
	}
	
    @Override
    public void addDefaultFlowers()
    {
        addFlower(BlockInit.SCARLET_ORCHID.getDefaultState(), 20);
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand)
    {
    	return ROOF_TREE;
    }
    
    public void addFlowersAndMushrooms(World worldIn, Random rand1, BlockPos posIn)
    {
        IBlockState orchid = BlockInit.SCARLET_ORCHID.getDefaultState();
        IBlockState mushroom = BlockInit.SCARLET_MUSHROOM.getDefaultState();
        for (int i = 0; i < 2; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                int x = i * 4 + 1 + 8 + rand1.nextInt(3);
                int z = j * 4 + 1 + 8 + rand1.nextInt(3);
                BlockPos blockPos = worldIn.getHeight(posIn.add(x, 0, z));
                int block = rand1.nextInt(4);
                if(worldIn.getBlockState(blockPos.down()).getBlock() == BlockInit.SCARLET_GRASS_BLOCK || worldIn.getBlockState(blockPos.down()).getBlock() == Blocks.DIRT)
                {
                	switch (block)
                    {
                      case 0:
                    	  worldIn.setBlockState(blockPos, orchid);
                      break;
                      case 1:
                    	  worldIn.setBlockState(blockPos, mushroom);
                      break;
                      default:
                    }
                }
            }
        }
    }
    
    public void addGrass(World worldIn, Random rand1, BlockPos posIn)
    {
    	IBlockState grass = BlockInit.SCARLET_GRASS.getDefaultState();
    	for (int a = 0; a < 16; ++a)
    	{
    		for (int i = 0; i < 2; ++i)
            {
                for (int j = 0; j < 2; ++j)
                {
                    int x = i * 4 + 1 + 8 + rand1.nextInt(3);
                    int z = j * 4 + 1 + 8 + rand1.nextInt(3);
                    BlockPos blockPos = worldIn.getHeight(posIn.add(x, 0, z));
                    if(worldIn.getBlockState(blockPos.down()).getBlock() == BlockInit.SCARLET_GRASS_BLOCK || worldIn.getBlockState(blockPos.down()).getBlock() == Blocks.DIRT)
                    {
                    	worldIn.setBlockState(blockPos, grass);
                    }
                }
            }
    	}
    }
    
    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        this.addGrass(worldIn, rand, pos);
        this.addFlowersAndMushrooms(worldIn, rand, pos);
        super.decorate(worldIn, rand, pos);
    }
}