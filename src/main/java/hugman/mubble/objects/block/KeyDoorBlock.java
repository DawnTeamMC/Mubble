package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlockStateProperties;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class KeyDoorBlock extends DoorBlock
{
	public static final BooleanProperty LOCKED = MubbleBlockStateProperties.LOCKED;
	
    public KeyDoorBlock(Block.Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(LOCKED, true).with(FACING, Direction.NORTH).with(OPEN, Boolean.valueOf(false)).with(HINGE, DoorHingeSide.LEFT).with(POWERED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
    	builder.add(HALF, FACING, OPEN, HINGE, POWERED, LOCKED);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
    	if(state.get(LOCKED))
    	{
    		if(player.getHeldItem(handIn).getItem() == MubbleItems.BLUEBERRIES)
    		{
            	BlockPos otherBlockPos = pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
            	BlockState otherBlockState = worldIn.getBlockState(otherBlockPos);
                worldIn.setBlockState(pos, state.with(LOCKED, false));
                worldIn.setBlockState(otherBlockPos, otherBlockState.with(LOCKED, false));
                BlockPos hitPos = hit.getPos();
                worldIn.addParticle(ParticleTypes.FLASH, false, hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0, 0, 0);
                worldIn.playSound((PlayerEntity)null, pos, MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.BLOCKS, 0.8f, 1f);
        		return true;
    		}
    		else
    		{
        		return false;
    		}
    	}
    	else
    	{
    		state = state.cycle(OPEN);
    		worldIn.setBlockState(pos, state, 10);
    		worldIn.playEvent(player, state.get(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
    		return true;
    	}
    }
    
    public int getCloseSound()
    {
    	return this.material == Material.IRON ? 1011 : 1012;
    }

    public int getOpenSound()
    {
    	return this.material == Material.IRON ? 1005 : 1006;
    }
    
    public void playSound(World world, BlockPos pos, boolean flag)
    {
    	world.playEvent((PlayerEntity)null, flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
	}

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
    	BlockPos otherBlockPos = pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
    	BlockState otherBlockState = worldIn.getBlockState(otherBlockPos);

        boolean flag1 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(otherBlockPos);
        boolean flag2;
        if(otherBlockState.getBlock() instanceof KeyDoorBlock)
        {
        	flag2 = !state.get(LOCKED) || !otherBlockState.get(LOCKED);
        }
        else
        {
        	flag2 = !state.get(LOCKED);
        }
        if(blockIn != this && flag1 != state.get(POWERED) && !state.get(LOCKED))
        {
        	if (flag1 != state.get(OPEN))
        	{
        		this.playSound(worldIn, pos, flag1);
        	}
        	worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag1)).with(OPEN, Boolean.valueOf(flag1)).with(LOCKED, Boolean.valueOf(!flag2)), 2);
        }
        else if(blockIn != this)
        {
        	worldIn.setBlockState(pos, state.with(LOCKED, Boolean.valueOf(!flag2)), 2);
        }
	}
}