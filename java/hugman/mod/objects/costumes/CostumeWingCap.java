package hugman.mod.objects.costumes;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BiomeInit;
import hugman.mod.init.CostumeInit;
import hugman.mod.init.ItemInit;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CostumeWingCap extends CostumeHeadBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public CostumeWingCap()
	{
		super("wing_cap", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
		this.setMaxDamage(600);
	}
	
	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		return repair.getItem() == Items.FEATHER;
    }
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack armor)
	{
		if(this.isUsable(armor) && playerIn.isSprinting()) armor.damageItem(1, playerIn);
		if(!worldIn.isRemote && this.isUsable(armor) && playerIn.isSprinting())
		{
			playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(25), 1, 2));
			playerIn.fallDistance = 0f;
		}
	}
}
