package hugman.mubble.init;

import hugman.mubble.Mubble;
import hugman.mubble.objects.effect.HiccupEffect;
import hugman.mubble.objects.effect.SimpleEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MubbleEffects
{

    public static final StatusEffect HEAVINESS = register("heaviness", new SimpleEffect(StatusEffectType.HARMFUL, 9198906));
    //public static final StatusEffect SNEEZING = new SimpleEffect("sneezing", true, 9753716);
    public static final StatusEffect HICCUP = register("hiccup", new HiccupEffect(StatusEffectType.NEUTRAL, 9198906));
    
    private static StatusEffect register(String name, StatusEffect statusEffect)
    {
    	return Registry.register(Registry.STATUS_EFFECT, new Identifier(Mubble.MOD_ID, name), statusEffect);
    }
}