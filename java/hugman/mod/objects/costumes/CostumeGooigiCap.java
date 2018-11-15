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

public class CostumeGooigiCap extends CostumeHeadBase implements IHasModel
{
	/**
	 * Static class - can only be initialized once.
	 */
	public CostumeGooigiCap()
	{
		super("gooigi_cap", SoundEvents.BLOCK_SLIME_PLACE);
	}
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack armor)
	{
        Random rand = new Random();
		if(!worldIn.isRemote && rand.nextInt(51) == 0)
		{
	        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_SLIME_HIT, SoundCategory.PLAYERS, 1f, 1f);
		    playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(8), (rand.nextInt(3) + 1) * 20, rand.nextInt(3)));
		}
	}
}
