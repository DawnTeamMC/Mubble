package hugman.mod.objects.costume;

import java.util.Random;

import hugman.mod.util.interfaces.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CostumeSimpleEffect extends CostumeBaseHead implements IHasModel
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
