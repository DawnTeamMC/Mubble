package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.ChinchoEntity;
import hugman.mubble.objects.entity.render.model.ChinchoModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChinchoRender extends MobRenderer<ChinchoEntity, ChinchoModel<ChinchoEntity>>
{
    private static final ResourceLocation CHINCHO_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/chincho.png");
	
	public ChinchoRender(EntityRendererManager manager)
	{
		super(manager, new ChinchoModel<>(), 0.5F);
	}
	
	protected ResourceLocation getEntityTexture(ChinchoEntity entity)
    {
		return CHINCHO_TEXTURES;
    }

	@Override
	protected void applyRotations(ChinchoEntity entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}