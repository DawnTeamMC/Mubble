package hugman.mod.objects.costume;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;

public class HeadCostume extends Costume
{    
    public HeadCostume(Properties builder, SoundEvent sound, EffectInstance... potionEffects)
    {
        super(builder, sound, EquipmentSlotType.HEAD, potionEffects);
    }
}