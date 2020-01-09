package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.ChinchoEntity;
import hugman.mubble.objects.entity.render.model.ChinchoModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ChinchoRender extends MobEntityRenderer<ChinchoEntity, ChinchoModel<ChinchoEntity>>
{
    private static final Identifier CHINCHO_TEXTURES = new Identifier(Mubble.MOD_ID, "textures/entity/chincho.png");
	
	public ChinchoRender(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher, new ChinchoModel<>(), 0.5F);
	}
	
	public Identifier getTexture(ChinchoEntity entity)
    {
		return CHINCHO_TEXTURES;
    }
}