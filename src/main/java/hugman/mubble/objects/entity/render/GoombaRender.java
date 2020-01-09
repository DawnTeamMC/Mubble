package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.GoombaEntity;
import hugman.mubble.objects.entity.render.model.GoombaModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GoombaRender extends MobEntityRenderer<GoombaEntity, GoombaModel<GoombaEntity>>
{
    private static final Identifier NORMAL_GOOMBA_TEXTURES = new Identifier(Mubble.MOD_ID, "textures/entity/goomba/normal.png");
    //private static final Identifier GOLDEN_GOOMBA_TEXTURES = new Identifier(Mubble.MOD_ID, "textures/entity/goomba/golden.png");
	
	public GoombaRender(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher, new GoombaModel<>(), 0.5F);
	}
	
	public Identifier getTexture(GoombaEntity entity)
    {
		return NORMAL_GOOMBA_TEXTURES;
    }
}