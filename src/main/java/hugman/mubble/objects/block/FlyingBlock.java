package hugman.mubble.objects.block;

import java.util.Random;

import hugman.mubble.init.data.MubbleTags;
import hugman.mubble.objects.entity.FlyingBlockEntity;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class FlyingBlock extends Block
{
	public static boolean flyInstantly;
	
    public FlyingBlock(Block.Settings builder)
    {
        super(builder);
    }
    
    @Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.getBlockTickScheduler().schedule(pos, this, this.getFlyDelay());
	}

    @Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		world.getBlockTickScheduler().schedule(pos, this, this.getFlyDelay());
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

    @Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
		if(canFlyThrough(world.getBlockState(pos.down())) && pos.getY() >= 0)
		{
			FlyingBlockEntity flyingBlockEntity = new FlyingBlockEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, world.getBlockState(pos));
			this.configureFlyingBlockEntity(flyingBlockEntity);
			world.spawnEntity(flyingBlockEntity);
		}
	}
    
    protected void configureFlyingBlockEntity(FlyingBlockEntity entity)
    {
    	
    }
	
	protected int getFlyDelay()
	{
		return 2;
	}
	
	public static boolean canFlyThrough(BlockState state)
	{
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return block instanceof AirBlock || block == Blocks.FIRE || block.isIn(MubbleTags.Blocks.CLOUD_BLOCKS) || material.isLiquid() || material.isReplaceable();
	}
	
	public void onLanding(World world, BlockPos pos, BlockState flyingBlockState, BlockState currentStateInPos, FlyingBlockEntity flyingBlockEntity)
	{
		
	}

	public void onDestroyedOnLanding(World world, BlockPos pos, FlyingBlockEntity flyingBlockEntity)
	{
		
	}
}
