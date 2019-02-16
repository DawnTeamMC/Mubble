package hugman.mod.objects.item;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBandage extends ItemBase implements IHasModel
{	
	/**
	 * Static class - can only be initialized once.
	 */
	public ItemBandage()
	{
		super("bandage", 16);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(playerIn.isPotionActive(Potion.getPotionById(4))
        || playerIn.isPotionActive(Potion.getPotionById(9))
        || playerIn.isPotionActive(Potion.getPotionById(19))
        || playerIn.isPotionActive(Potion.getPotionById(20)))
        {
            playerIn.removePotionEffect(Potion.getPotionById(4));
            playerIn.removePotionEffect(Potion.getPotionById(9));
            playerIn.removePotionEffect(Potion.getPotionById(19));
            playerIn.removePotionEffect(Potion.getPotionById(29));
            if (!playerIn.capabilities.isCreativeMode) stack.shrink(1);
            playerIn.addStat(StatList.getObjectUseStats(this));
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 0.5F, 1F);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }
}
