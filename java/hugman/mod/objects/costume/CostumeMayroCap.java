package hugman.mod.objects.costume;

import hugman.mod.init.MubbleTabs;
import hugman.mod.init.MubbleItems;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CostumeMayroCap extends CostumeHeadBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public CostumeMayroCap()
	{
		super("mayro_cap", MubbleTabs.MUBBLE_COSTUMES, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack armor)
	{
		playerIn.inventory.clearMatchingItems(MubbleItems.YELLOW_COIN, 0, 1, null);
		playerIn.inventory.clearMatchingItems(MubbleItems.RED_COIN, 0, 1, null);
		playerIn.inventory.clearMatchingItems(MubbleItems.BLUE_COIN, 0, 1, null);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityEquipmentSlot.HEAD;
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);
        if (itemstack1.isEmpty())
        {
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1f, 1f);
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundHandler.COSTUME_CAPPY_EQUIP, SoundCategory.VOICE, 1f, 1f);
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            if (!playerIn.capabilities.isCreativeMode) itemstack.setCount(0);
            playerIn.addStat(StatList.getObjectUseStats(this));
            if("MayroSMM".equals(playerIn.getName())) playerIn.sendStatusMessage(new TextComponentTranslation("item.mayro_cap.secret_status", new Object[0]), true);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }
}
