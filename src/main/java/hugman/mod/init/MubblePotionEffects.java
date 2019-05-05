package hugman.mod.init;

import java.util.ArrayList;
import java.util.List;

import hugman.mod.objects.potion.effect.PotionSimple;
import net.minecraft.potion.Potion;

public class MubblePotionEffects
{
    public static final List<Potion> EFFECTS = new ArrayList<Potion>();

    public static final Potion HEAVINESS = new PotionSimple("heaviness", true, 9198906);
    
    public static void register(Potion potion)
    {
    	EFFECTS.add(potion);
    }
}