package hugman.mubble.object.entity.render;

import hugman.mubble.Mubble;
import hugman.mubble.object.entity.ZombieCowmanEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ZombieCowmanEntityRenderer extends BipedEntityRenderer<ZombieCowmanEntity, ZombieEntityModel<ZombieCowmanEntity>> {
	private static final Identifier ZOMBIE_COWMAN_TEXTURE = new Identifier(Mubble.MOD_ID, "textures/entity/zombie_cowman.png");

	public ZombieCowmanEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher, new ZombieEntityModel<>(0.0F, false), 0.5F);
		this.addFeature(new ArmorFeatureRenderer(this, new ZombieEntityModel<>(0.5F, true), new ZombieEntityModel<>(1.0F, true)));
	}

	@Override
	public Identifier getTexture(ZombieCowmanEntity entity) {
		return ZOMBIE_COWMAN_TEXTURE;
	}
}