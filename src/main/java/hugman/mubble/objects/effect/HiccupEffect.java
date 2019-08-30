package hugman.mubble.objects.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.Vec3d;

public class HiccupEffect extends SimpleEffect
{	
	public HiccupEffect(EffectType typeIn, int liquidColorIn)
	{
		super(typeIn, liquidColorIn);
	}
	
	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
	{
		Vec3d baseMotion = entityLivingBaseIn.getMotion();
		Vec3d finalMotion = new Vec3d(baseMotion.getX(), 0.2D, baseMotion.getZ());
		entityLivingBaseIn.setMotion(finalMotion);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
        int k = 200 >> amplifier;
        if (k > 0) return duration % k == 0;
        else return true;
	}
}