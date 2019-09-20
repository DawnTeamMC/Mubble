package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlockStateProperties;
import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
    		this.playFailedOpenSound(worldIn, pos);
    		player.swingArm(handIn);
    		return false;
    	}
    	else
    	{
    		state = state.cycle(OPEN);
    		worldIn.setBlockState(pos, state, 10);
    		this.playToggleSound(worldIn, pos, state.get(OPEN));
    		return true;
    	}
    }

    public SoundEvent getOpenSound(Block block)
    {
    	if(block == MubbleBlocks.SMB_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_OPEN_SMB;
    	}
    	else if(block == MubbleBlocks.SMB3_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_OPEN_SMB3;
    	}
    	else if(block == MubbleBlocks.SMW_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_OPEN_SMW;
    	}
    	else if(block == MubbleBlocks.NSMBU_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_OPEN_NSMBU;
    	}
    	else
    	{
        	return MubbleSounds.BLOCK_DOOR_OPEN_SMB;
    	}
    }
    
    public SoundEvent getCloseSound(Block block)
    {
    	if(block == MubbleBlocks.SMB_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_CLOSE_SMB;
    	}
    	else if(block == MubbleBlocks.SMB3_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_CLOSE_SMB3;
    	}
    	else if(block == MubbleBlocks.SMW_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_CLOSE_SMW;
    	}
    	else if(block == MubbleBlocks.NSMBU_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_CLOSE_NSMBU;
    	}
    	else
    	{
        	return MubbleSounds.BLOCK_DOOR_CLOSE_SMB;
    	}
    }
    
    public SoundEvent getKeyFailSound(Block block)
    {
    	if(block == MubbleBlocks.SMB_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_KEY_FAIL_SMB;
    	}
    	else if(block == MubbleBlocks.SMB3_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_KEY_FAIL_SMB3;
    	}
    	else if(block == MubbleBlocks.SMW_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_KEY_FAIL_SMW;
    	}
    	else if(block == MubbleBlocks.NSMBU_KEY_DOOR)
    	{
    		return MubbleSounds.BLOCK_DOOR_KEY_FAIL_NSMBU;
    	}
    	else
    	{
        	return MubbleSounds.BLOCK_DOOR_KEY_FAIL_SMB;
    	}
    }
    
    public void playToggleSound(World worldIn, BlockPos pos, boolean flag)
    {
    	worldIn.playSound((PlayerEntity)null, pos, flag ? this.getOpenSound(this) : this.getCloseSound(this), SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
    
    public void playFailedOpenSound(World worldIn, BlockPos pos)
    {
    	worldIn.playSound((PlayerEntity)null, pos, this.getKeyFailSound(this), SoundCategory.BLOCKS, 1.0F, 1.0F);
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
        		this.playToggleSound(worldIn, pos, flag1);
        	}
        	worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag1)).with(OPEN, Boolean.valueOf(flag1)).with(LOCKED, Boolean.valueOf(!flag2)), 2);
        }
        else if(blockIn != this)
        {
        	worldIn.setBlockState(pos, state.with(LOCKED, Boolean.valueOf(!flag2)), 2);
        }
	}
}