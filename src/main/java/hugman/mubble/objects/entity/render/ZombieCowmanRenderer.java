package hugman.mubble.objects.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.objects.entity.ZombieCowmanEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ZombieCowmanRenderer extends BipedEntityRenderer<ZombieCowmanEntity, ZombieEntityModel<ZombieCowmanEntity>>
{
	private static final Identifier ZOMBIE_COWMAN_TEXTURE = new Identifier(Mubble.MOD_ID, "textures/entity/zombie_cowman.png");

	public ZombieCowmanRenderer(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher, new ZombieEntityModel<>(0.0F, false), 0.5F);
		this.addFeature(new ArmorBipedFeatureRenderer<>(this, new ZombieEntityModel<>(0.5F, true), new ZombieEntityModel<>(1.0F, true)));
	}

	@Override
	public Identifier getTexture(ZombieCowmanEntity entity)
	{
		return ZOMBIE_COWMAN_TEXTURE;
	}
}