package hugman.mod.objects.costumes;

import hugman.mod.init.CreativeTabInit;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class CostumeWingCap extends CostumeHeadBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public CostumeWingCap()
	{
		super("wing_cap", CreativeTabInit.SUPER_MARIO, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
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
