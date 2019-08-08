package hugman.mubble.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mubble.objects.potion_effect.HiccupEffect;
import hugman.mubble.objects.potion_effect.SimpleEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class MubbleEffects
{
    public static final List<Effect> EFFECTS = new ArrayList<Effect>();

    public static final Effect HEAVINESS = register("heaviness", new SimpleEffect(EffectType.HARMFUL, 9198906));
    //public static final Effect SNEEZING = new EffectSimple("sneezing", true, 9753716);
    public static final Effect HICCUP = register("hiccup", new HiccupEffect(EffectType.NEUTRAL, 9198906));
    
    public static Effect register(String name, Effect effect)
    {
    	Effect fEffect = effect.setRegistryName(name);
    	EFFECTS.add(fEffect);
    	return fEffect;
    }
}