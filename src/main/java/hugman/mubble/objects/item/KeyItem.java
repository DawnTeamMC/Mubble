package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.objects.block.KeyDoorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class KeyItem extends Item
{
	private final Block block;

	public KeyItem(Item.Settings builder, Block blockIn)
	{
		super(builder);
		this.block = blockIn;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		World worldIn = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = worldIn.getBlockState(pos);
		if (state.getBlock() instanceof KeyDoorBlock && state.getBlock() == block)
		{
			if (state.get(KeyDoorBlock.LOCKED))
			{
				if (!worldIn.isClient())
				{
					BlockPos otherBlockPos = pos.offset(state.get(KeyDoorBlock.HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
					BlockState otherBlockState = worldIn.getBlockState(otherBlockPos);
					worldIn.setBlockState(pos, state.with(KeyDoorBlock.LOCKED, false), 2);
					worldIn.setBlockState(otherBlockPos, otherBlockState.with(KeyDoorBlock.LOCKED, false), 2);
					worldIn.playSound((PlayerEntity) null, pos, this.getKeySuccessSound(state.getBlock(), !state.get(KeyDoorBlock.LOCKED)), SoundCategory.BLOCKS, 1.0F, 1.0F);
					context.getStack().decrement(1);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.FAIL;
	}

	public SoundEvent getKeySuccessSound(Block block, boolean success)
	{
		if (block == MubbleBlocks.SMB_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_KEY_SUCCESS_SMB;
		}
		else if (block == MubbleBlocks.SMB3_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_KEY_SUCCESS_SMB3;
		}
		else if (block == MubbleBlocks.SMW_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_KEY_SUCCESS_SMW;
		}
		else if (block == MubbleBlocks.NSMBU_KEY_DOOR)
		{
			return MubbleSounds.BLOCK_DOOR_KEY_SUCCESS_NSMBU;
		}
		else
		{
			return MubbleSounds.BLOCK_DOOR_KEY_SUCCESS_SMB;
		}
	}
}
