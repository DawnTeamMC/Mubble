package hugman.mod.objects.costume;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;

public class CostumeHead extends Costume
{    
    public CostumeHead(Properties builder, SoundEvent sound, PotionEffect... potionEffects)
    {
        super(builder, sound, EntityEquipmentSlot.HEAD, potionEffects);
    }
}