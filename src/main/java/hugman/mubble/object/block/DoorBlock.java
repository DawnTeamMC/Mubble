package hugman.mubble.object.block;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DoorBlock extends net.minecraft.block.DoorBlock {
	/* Extension for internal publicity */
	public DoorBlock(Block.Settings builder) {
		super(builder);
	}

	@Override
	public void setOpen(World world, BlockState state, BlockPos pos, boolean open) {
		if(isSmm2Door()) {
			BlockState blockstate = world.getBlockState(pos);
			if(blockstate.getBlock() == this && blockstate.get(OPEN) != open) {
				world.setBlockState(pos, blockstate.with(OPEN, Boolean.valueOf(open)), 10);
				this.playToggleSound(world, pos, open);
			}
		}
		else {
			super.setOpen(world, state, pos, open);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if(isSmm2Door()) {
			boolean flag = worldIn.isReceivingRedstonePower(pos) || worldIn.isReceivingRedstonePower(pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
			if(blockIn != this && flag != state.get(POWERED)) {
				if(flag != state.get(OPEN)) {
					this.playToggleSound(worldIn, pos, flag);
				}
				worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)).with(OPEN, Boolean.valueOf(flag)), 2);
			}
		}
		else {
			super.neighborUpdate(state, worldIn, pos, blockIn, fromPos, isMoving);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit) {
		if(isSmm2Door()) {
			state = state.cycle(OPEN);
			worldIn.setBlockState(pos, state, 10);
			this.playToggleSound(worldIn, pos, state.get(OPEN));
			return ActionResult.SUCCESS;
		}
		return super.onUse(state, worldIn, pos, player, handIn, hit);
	}

	private boolean isSmm2Door() {
		return this == MubbleBlocks.SMB_DOOR.getBlock() || this == MubbleBlocks.SMB3_DOOR.getBlock() || this == MubbleBlocks.SMW_DOOR.getBlock() || this == MubbleBlocks.NSMBU_DOOR.getBlock();
	}

	public void playToggleSound(World worldIn, BlockPos pos, boolean flag) {
		worldIn.playSound(null, pos, flag ? this.getOpenSound(this) : this.getCloseSound(this), SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public SoundEvent getOpenSound(Block block) {
		if(block == MubbleBlocks.SMB_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_OPEN_SMB;
		}
		else if(block == MubbleBlocks.SMB3_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_OPEN_SMB3;
		}
		else if(block == MubbleBlocks.SMW_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_OPEN_SMW;
		}
		else if(block == MubbleBlocks.NSMBU_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_OPEN_NSMBU;
		}
		else {
			return MubbleSounds.BLOCK_DOOR_OPEN_SMB;
		}
	}

	public SoundEvent getCloseSound(Block block) {
		if(block == MubbleBlocks.SMB_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_CLOSE_SMB;
		}
		else if(block == MubbleBlocks.SMB3_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_CLOSE_SMB3;
		}
		else if(block == MubbleBlocks.SMW_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_CLOSE_SMW;
		}
		else if(block == MubbleBlocks.NSMBU_DOOR.getBlock()) {
			return MubbleSounds.BLOCK_DOOR_CLOSE_NSMBU;
		}
		else {
			return MubbleSounds.BLOCK_DOOR_CLOSE_SMB;
		}
	}
}