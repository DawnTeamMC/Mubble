package hugman.mod.objects.costume;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CostumeVanishCap extends CostumeBaseHead implements IHasModel
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
