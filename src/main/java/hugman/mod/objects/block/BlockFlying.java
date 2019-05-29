package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.Mubble;
import hugman.mod.init.MubbleBlocks;
import hugman.mod.init.MubbleTags;
import hugman.mod.objects.entity.EntityFlyingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

public class BlockFlying extends Block
{
	public static boolean flyInstantly;
	
    public BlockFlying(String name, Properties properties, ItemGroup group)
    {
        super(properties);
        setRegistryName(Mubble.MOD_ID, name);
        MubbleBlocks.register(this, group);
    }
    
    @Override
    public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState)
    {
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
    }
    
    @Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, this.tickRate(worldIn));
        return stateIn;
    }

    @Override
    public void tick(IBlockState state, World worldIn, BlockPos pos, Random random)
    {
        if (!worldIn.isRemote) this.checkFlyable(worldIn, pos);
    }
    
	private void checkFlyable(World worldIn, BlockPos pos)
	{
        if (canFlyThrough(worldIn.getBlockState(pos.up())) && pos.getY() >= 0)
        {
           if (!flyInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
           {
              if (!worldIn.isRemote)
              {
                 EntityFlyingBlock entityflyingblock = new EntityFlyingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                 this.onStartFlying(entityflyingblock);
                 worldIn.spawnEntity(entityflyingblock);
              }
           }
           else
           {
              IBlockState state = getDefaultState();
              if (worldIn.getBlockState(pos).getBlock() == this)
              {
                 state = worldIn.getBlockState(pos);
                 worldIn.removeBlock(pos);
              }
              BlockPos blockpos;
              for(blockpos = pos.up(); canFlyThrough(worldIn.getBlockState(blockpos)) && blockpos.getY() > 0; blockpos = blockpos.up())
              {
                 ;
              }
              if (blockpos.getY() > 0) worldIn.setBlockState(blockpos.up(), state);
           }
        }
     }

	protected void onStartFlying(EntityFlyingBlock flyingEntity)
	{
	}
    
	public int tickRate(IWorldReaderBase worldIn)
	{
    	 return 2;
	}
	
	public static boolean canFlyThrough(IBlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.AIR || block == Blocks.FIRE || block.isIn(MubbleTags.Blocks.CLOUD_BLOCKS) || material.isLiquid() || material.isReplaceable();
     }

	public void onEndFlying(World worldIn, BlockPos pos, IBlockState flyingState, IBlockState hitState)
	{
	}
    
	public void onBroken(World worldIn, BlockPos pos)
    {
    }
}
