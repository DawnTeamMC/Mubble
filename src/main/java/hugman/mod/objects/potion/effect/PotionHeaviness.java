package hugman.mod.objects.potion.effect;

import hugman.mod.Mubble;
import hugman.mod.init.MubblePotionEffects;
import net.minecraft.potion.Potion;

public class PotionHeaviness extends Potion
{	
	public PotionHeaviness()
	{
		super(true, 7838381);
		setRegistryName(Mubble.MOD_ID, "heaviness");
		MubblePotionEffects.register(this);
	}
	
	@Override
	protected Potion setIconIndex(int x, int y)
	{
		return super.setIconIndex(1, 0);
	}
}