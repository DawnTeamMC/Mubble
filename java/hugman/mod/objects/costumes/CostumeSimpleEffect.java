package hugman.mod.objects.costumes;

import java.util.Random;

import hugman.mod.Main;
import hugman.mod.init.BiomeInit;
import hugman.mod.init.CostumeInit;
import hugman.mod.init.ItemInit;
import hugman.mod.objects.items.ItemBase;
import hugman.mod.util.handlers.SoundHandler;
import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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

public class CostumeSimpleEffect extends CostumeHeadBase implements IHasModel
{
	private PotionEffect[] effects;
    
	/**
	 * Open class - can be initialized for multiple items with variables.
	 */
	public CostumeSimpleEffect(String name, SoundEvent sound, PotionEffect...potionEffect)
	{
		super(name, sound);
		this.effects = potionEffect;
	}
	
	@Override
	public void onArmorTick(World worldIn, EntityPlayer playerIn, ItemStack armor)
	{
        if(!worldIn.isRemote)
        {
            Random rand = new Random();
    		for(PotionEffect effect : effects)
    		{
    			playerIn.addPotionEffect(new PotionEffect(effect));
    		}
        }
	}
}
