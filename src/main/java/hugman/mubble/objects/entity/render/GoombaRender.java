package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.GoombaEntity;
import hugman.mubble.objects.entity.render.model.GoombaModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class GoombaRender extends MobRenderer<GoombaEntity, GoombaModel<GoombaEntity>>
{
    private static final ResourceLocation NORMAL_GOOMBA_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/goomba/normal.png");
    //private static final ResourceLocation GOLDEN_GOOMBA_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/goomba/golden.png");
	
	public GoombaRender(EntityRendererManager manager)
	{
		super(manager, new GoombaModel<>(), 0.5F);
	}
	
	protected ResourceLocation getEntityTexture(GoombaEntity entity)
    {
		return NORMAL_GOOMBA_TEXTURES;
    }

	@Override
	protected void applyRotations(GoombaEntity entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}