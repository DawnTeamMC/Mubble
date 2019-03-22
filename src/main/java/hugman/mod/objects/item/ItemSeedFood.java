package hugman.mod.objects.item;

import hugman.mod.Mubble;
import hugman.mod.init.elements.MubbleBlocks;
import hugman.mod.init.elements.MubbleItems;
import hugman.mod.init.technical.MubbleTabs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemSeedFood extends net.minecraft.item.ItemSeedFood implements IPlantable
{
    public ItemSeedFood(String name, int heal, float saturation)
    {
        super(heal, saturation, Blocks.STONE, new Item.Properties().group(MubbleTabs.MUBBLE_ITEMS));
        setRegistryName(Mubble.MOD_ID, name);
		MubbleItems.register(this);
    }
    
    @Override
    public EnumActionResult onItemUse(ItemUseContext p_195939_1_)
    {
        IWorld iworld = p_195939_1_.getWorld();
        BlockPos blockpos = p_195939_1_.getPos().up();
        if (p_195939_1_.getFace() == EnumFacing.UP && iworld.isAirBlock(blockpos) && getCrop().isValidPosition(iworld, blockpos) && iworld.getBlockState(p_195939_1_.getPos()).canSustainPlant(iworld, p_195939_1_.getPos(), EnumFacing.UP, this)) {
           iworld.setBlockState(blockpos, getCrop(), 11);
           EntityPlayer entityplayer = p_195939_1_.getPlayer();
           ItemStack itemstack = p_195939_1_.getItem();
           if (entityplayer instanceof EntityPlayerMP)
           {
        	   CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)entityplayer, blockpos, itemstack);
           }
           itemstack.shrink(1);
           return EnumActionResult.SUCCESS;
        }
        else
        {
           return EnumActionResult.PASS;
        }
	}
    
    public IBlockState getCrop()
    {
    	if(this == MubbleItems.TOMATO) return MubbleBlocks.TOMATO.getDefaultState();
    	if(this == MubbleItems.SALAD) return MubbleBlocks.SALAD.getDefaultState();
    	else return null;
    }

    @Override
	public IBlockState getPlant(net.minecraft.world.IBlockReader world, BlockPos pos)
	{
        return getCrop();
	}

    @Override
	public EnumPlantType getPlantType(net.minecraft.world.IBlockReader world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}
}
