package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.potion_effect.EffectHiccup;
import hugman.mod.objects.potion_effect.EffectSimple;
import net.minecraft.potion.Potion;

public class MubblePotionEffects
{
    public static final List<Potion> EFFECTS = new ArrayList<Potion>();

    public static final Potion HEAVINESS = new EffectSimple("heaviness", true, 9198906);
    //public static final Potion SNEEZING = new EffectSimple("sneezing", true, 9753716);
    public static final Potion HICCUP = new EffectHiccup();
    
    public static void register(Potion potion)
    {
    	EFFECTS.add(potion);
    }
}