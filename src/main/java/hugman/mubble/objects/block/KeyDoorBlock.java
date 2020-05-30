package hugman.mubble.objects.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.init.data.MubbleBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class KeyDoorBlock extends DoorBlock
{
	public static final BooleanProperty LOCKED = MubbleBlockStateProperties.LOCKED;

	public KeyDoorBlock(Block.Settings builder)
	{
		super(builder);
		this.setDefaultState(this.stateManager.getDefaultState().with(LOCKED, true).with(FACING, Direction.NORTH).with(OPEN, Boolean.valueOf(false)).with(HINGE, DoorHinge.LEFT).with(POWERED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(HALF, FACING, OPEN, HINGE, POWERED, LOCKED);
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit)
	{
		if (state.get(LOCKED))
		{
			this.playFailedOpenSound(worldIn, pos);
			player.swingHand(handIn);
			return ActionResult.PASS;
		}
		else
		{
			state = state.cycle(OPEN);
			worldIn.setBlockState(pos, state, 10);
			this.playToggleSound(worldIn, pos, state.get(OPEN));
			return ActionResult.SUCCESS;
		}
	}

	public SoundEvent getOpenSound(Block block)
	{
		if (block == MubbleBlocks.SMB_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_OPEN_SMB;
		}
		else if (block == MubbleBlocks.SMB3_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_OPEN_SMB3;
		}
		else if (block == MubbleBlocks.SMW_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_OPEN_SMW;
		}
		else if (block == MubbleBlocks.NSMBU_KEY_DOOR)
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
		if (block == MubbleBlocks.SMB_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_CLOSE_SMB;
		}
		else if (block == MubbleBlocks.SMB3_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_CLOSE_SMB3;
		}
		else if (block == MubbleBlocks.SMW_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_CLOSE_SMW;
		}
		else if (block == MubbleBlocks.NSMBU_KEY_DOOR)
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
		if (block == MubbleBlocks.SMB_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_KEY_FAIL_SMB;
		}
		else if (block == MubbleBlocks.SMB3_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_KEY_FAIL_SMB3;
		}
		else if (block == MubbleBlocks.SMW_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_KEY_FAIL_SMW;
		}
		else if (block == MubbleBlocks.NSMBU_KEY_DOOR)
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
		worldIn.playSound(null, pos, flag ? this.getOpenSound(this) : this.getCloseSound(this), SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public void playFailedOpenSound(World worldIn, BlockPos pos)
	{
		worldIn.playSound(null, pos, this.getKeyFailSound(this), SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		BlockPos otherBlockPos = pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
		BlockState otherBlockState = worldIn.getBlockState(otherBlockPos);
		boolean flag1 = worldIn.isReceivingRedstonePower(pos) || worldIn.isReceivingRedstonePower(otherBlockPos);
		boolean flag2;
		if (otherBlockState.getBlock() instanceof KeyDoorBlock)
		{
			flag2 = !state.get(LOCKED) || !otherBlockState.get(LOCKED);
		}
		else
		{
			flag2 = !state.get(LOCKED);
		}
		if (blockIn != this && flag1 != state.get(POWERED) && !state.get(LOCKED))
		{
			if (flag1 != state.get(OPEN))
			{
				this.playToggleSound(worldIn, pos, flag1);
			}
			worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag1)).with(OPEN, Boolean.valueOf(flag1)).with(LOCKED, Boolean.valueOf(!flag2)), 2);
		}
		else if (blockIn != this)
		{
			worldIn.setBlockState(pos, state.with(LOCKED, Boolean.valueOf(!flag2)), 2);
		}
	}
}