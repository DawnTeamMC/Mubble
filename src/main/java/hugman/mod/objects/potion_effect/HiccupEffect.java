package hugman.mod.objects.potion_effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

public class HiccupEffect extends SimpleEffect
{	
	public HiccupEffect(EffectType typeIn, int liquidColorIn)
	{
		super(typeIn, liquidColorIn);
	}
	
	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if(entityLivingBaseIn.onGround) entityLivingBaseIn.getMotion().add(0.0D, 0.2D, 0.0D);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
        int k = 100 >> amplifier;
        if (k > 0) return duration % k == 0;
        else return true;
	}
}