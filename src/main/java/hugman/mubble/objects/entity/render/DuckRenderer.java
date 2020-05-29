package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.DuckEntity;
import hugman.mubble.objects.entity.render.model.DuckModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DuckRenderer extends MobEntityRenderer<DuckEntity, DuckModel<DuckEntity>>
{
	public DuckRenderer(EntityRenderDispatcher manager)
	{
		super(manager, new DuckModel<>(), 0.3F);
	}

	@Override
	public Identifier getTexture(DuckEntity entity)
	{
		return new Identifier(Mubble.MOD_ID, "textures/entity/duck/" + entity.getVariantType().getName() + ".png");
	}

	protected float getAnimationProgress(DuckEntity entity, float p_77044_2_)
	{
		float f = MathHelper.lerp(p_77044_2_, entity.oFlap, entity.wingRotation);
		float f1 = MathHelper.lerp(p_77044_2_, entity.oFlapSpeed, entity.destPos);
		return (MathHelper.sin(f) + 1.0F) * f1;
	}
}