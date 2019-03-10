package hugman.mod.objects.costume;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CostumeEffect extends CostumeSimple
{
	protected final PotionEffect[] effects;
    
    public CostumeEffect(String name, SoundEvent sound, EntityEquipmentSlot armorType, PotionEffect...potionEffect)
    {
        super(name, sound, armorType);
	    this.effects = potionEffect;
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player)
    {
    	if(!world.isRemote) for(PotionEffect effect : effects)
    	{
    		player.addPotionEffect(new PotionEffect(effect));
    	}
    }
}