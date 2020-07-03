package hugman.mubble.object.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.object.entity.ToadEntity;
import hugman.mubble.object.entity.model.ToadEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ToadEntityRenderer extends MobEntityRenderer<ToadEntity, ToadEntityModel<ToadEntity>> {
	public ToadEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher, new ToadEntityModel<>(), 0.5F);
	}

	@Override
	public Identifier getTexture(ToadEntity entity) {
		return new Identifier(Mubble.MOD_ID, "textures/entity/toad/" + entity.getVariant().getName() + ".png");
	}
}