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
public class ChinchoRenderer extends MobRenderer<ChinchoEntity, ChinchoModel<ChinchoEntity>>
{
    private static final ResourceLocation CHINCHO_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/chincho.png");
	
	public ChinchoRenderer(EntityRendererManager manager)
	{
		super(manager, new ChinchoModel<>(), 0.5F);
	}
	
	@Override
	public ResourceLocation getEntityTexture(ChinchoEntity entity)
    {
		return CHINCHO_TEXTURES;
    }
}