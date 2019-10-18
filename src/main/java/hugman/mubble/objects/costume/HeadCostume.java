package hugman.mubble.objects.costume;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class HeadCostume extends Costume
{
    public HeadCostume(Properties builder, SoundEvent sound, EffectInstance... potionEffects)
    {
        super(builder, sound, EquipmentSlotType.HEAD, potionEffects);
    }
	
    public HeadCostume(Properties builder, SoundEvent sound, ResourceLocation shader, EffectInstance... potionEffects)
    {
        super(builder, sound, EquipmentSlotType.HEAD, shader, potionEffects);
    }
}