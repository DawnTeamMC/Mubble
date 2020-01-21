package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.GoombaEntity;
import hugman.mubble.objects.entity.render.model.GoombaModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GoombaRenderer extends MobRenderer<GoombaEntity, GoombaModel<GoombaEntity>>
{
    private static final ResourceLocation NORMAL_GOOMBA_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/goomba/normal.png");
    private static final ResourceLocation GOLDEN_GOOMBA_TEXTURES = new ResourceLocation(Mubble.MOD_ID + ":textures/entity/goomba/golden.png");
	
	public GoombaRenderer(EntityRendererManager manager)
	{
		super(manager, new GoombaModel<>(), 0.5F);
	}
	
	@Override
	public ResourceLocation getEntityTexture(GoombaEntity entity)
    {
		switch(entity.getVariant())
		{
        case 0:
            return NORMAL_GOOMBA_TEXTURES;
        case 1:
            return GOLDEN_GOOMBA_TEXTURES;
        default:
        	return NORMAL_GOOMBA_TEXTURES;
		}
	}
}