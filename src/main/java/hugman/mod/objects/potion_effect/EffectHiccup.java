package hugman.mod.objects.potion_effect;

import net.minecraft.entity.EntityLivingBase;

public class EffectHiccup extends EffectSimple
{	
	public EffectHiccup()
	{
		super("hiccup", true, 9198906);
	}
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		if(entityLivingBaseIn.onGround) entityLivingBaseIn.motionY =+ 0.2D;
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
        int k = 100 >> amplifier;
        if (k > 0) return duration % k == 0;
        else return true;
	}
}