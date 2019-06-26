package hugman.mod.objects.block;

import java.util.Random;

import hugman.mod.init.MubbleTags;
import hugman.mod.objects.entity.FlyingBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class FlyingBlock extends Block
{
	public static boolean flyInstantly;
	
    public FlyingBlock(Block.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean p_220082_5_)
    {
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
    }
    
    @Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, this.tickRate(worldIn));
        return stateIn;
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
    {
        if (!worldIn.isRemote) this.checkFlyable(worldIn, pos);
    }
    
    public void checkFlyable(World worldIn, BlockPos pos)
    {
       if (worldIn.isAirBlock(pos.up()) || canFlyThrough(worldIn.getBlockState(pos.up())) && pos.getY() >= 0)
       {
          if (!worldIn.isRemote)
          {
        	 FlyingBlockEntity flyingblockentity = new FlyingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
             this.onStartFlying(flyingblockentity);
             worldIn.addEntity(flyingblockentity);
          }
       }
    }

	protected void onStartFlying(FlyingBlockEntity flyingEntity)
	{
	}
    
	public int tickRate(IWorldReader worldIn)
	{
    	 return 2;
	}
	
	public static boolean canFlyThrough(BlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.AIR || block == Blocks.FIRE || block.isIn(MubbleTags.Blocks.CLOUD_BLOCKS) || material.isLiquid() || material.isReplaceable();
     }

	public void onEndFlying(World worldIn, BlockPos pos, BlockState flyingState, BlockState hitState)
	{
	}
    
	public void onBroken(World worldIn, BlockPos pos)
    {
    }
}
