package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.DuckEntity;
import hugman.mubble.objects.entity.render.model.DuckModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DuckRenderer extends MobRenderer<DuckEntity, DuckModel<DuckEntity>>
{
    private static final ResourceLocation DUCK_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/duck.png");
	
	public DuckRenderer(EntityRendererManager manager)
	{
		super(manager, new DuckModel<>(), 0.5F);
	}
	
	@Override
	public ResourceLocation getEntityTexture(DuckEntity entity)
    {
		return DUCK_TEXTURES;
    }
	
	protected float handleRotationFloat(DuckEntity entity, float p_77044_2_)
	{
		float f = MathHelper.lerp(p_77044_2_, entity.oFlap, entity.wingRotation);
		float f1 = MathHelper.lerp(p_77044_2_, entity.oFlapSpeed, entity.destPos);
		return (MathHelper.sin(f) + 1.0F) * f1;
	}
}