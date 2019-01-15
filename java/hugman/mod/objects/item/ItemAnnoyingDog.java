package hugman.mod.objects.item;

import java.util.Random;

import hugman.mod.init.MubbleTabs;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemAnnoyingDog extends ItemBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public ItemAnnoyingDog()
	{
		super("annoying_dog", MubbleTabs.MUBBLE_ITEMS);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Random rand = new Random();
        if(!worldIn.isRemote)
        {
            int luck = rand.nextInt(20);
            if (playerIn.capabilities.isCreativeMode) luck = 1;
            if(luck == 0)
            {
            	stack.setCount(0);
                worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.ITEM_ANNOYING_DOG_DISAPPEAR, SoundCategory.PLAYERS, 0.5F, 1F);	
            }
            else
            {
            	if (!playerIn.capabilities.isCreativeMode && stack.getCount() <= 63) stack.grow(1);
            	worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.ITEM_ANNOYING_DOG_WAF, SoundCategory.PLAYERS, 0.5F, 1F);
            }
            playerIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }
}
