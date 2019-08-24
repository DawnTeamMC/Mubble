package hugman.mubble.objects.item;

import hugman.mubble.init.MubbleBlocks;
import hugman.mubble.init.MubbleItems;
import hugman.mubble.init.MubbleSounds;
import hugman.mubble.objects.block.KeyDoorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KeyItem extends Item
{    
    public KeyItem(Item.Properties builder)
    {
        super(builder);
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
    	World worldIn = context.getWorld();
    	BlockPos pos = context.getPos();
    	BlockState state = worldIn.getBlockState(pos);
		if(state.getBlock() instanceof KeyDoorBlock && state.getBlock() == getDoor(this))
		{
			if(state.get(KeyDoorBlock.LOCKED))
			{
				if(worldIn.isRemote)
				{
		            return ActionResultType.SUCCESS;
		        }
				else
				{
		        	BlockPos otherBlockPos = pos.offset(state.get(KeyDoorBlock.HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
		        	BlockState otherBlockState = worldIn.getBlockState(otherBlockPos);
		            worldIn.setBlockState(pos, state.with(KeyDoorBlock.LOCKED, false), 2);
		            worldIn.setBlockState(otherBlockPos, otherBlockState.with(KeyDoorBlock.LOCKED, false), 2);
		            worldIn.playSound((PlayerEntity)null, pos, MubbleSounds.COSTUME_CAPPY_EQUIP, SoundCategory.BLOCKS, 0.8f, 1f);
		            context.getItem().shrink(1);
		    		return ActionResultType.SUCCESS;
				}
			}
			else
			{
				return ActionResultType.FAIL;
			}
		}
		else return ActionResultType.FAIL;
    }
    
    private Block getDoor(Item item)
    {
    	if(item == MubbleItems.SMB_KEY) return MubbleBlocks.SMB_KEY_DOOR;
    	else if(item == MubbleItems.SMB3_KEY) return MubbleBlocks.SMB3_KEY_DOOR;
    	else if(item == MubbleItems.SMW_KEY) return MubbleBlocks.SMW_KEY_DOOR;
    	else return MubbleBlocks.NSMBU_KEY_DOOR;
	}
}
