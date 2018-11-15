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

public class CostumeVanishCap extends CostumeHeadBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public CostumeVanishCap()
	{
		super("vanish_cap", SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	}
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack armor)
	{
		if(!worldIn.isRemote && playerIn.isSneaking())
		{
			playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 2, 0));
		}
	}
}
