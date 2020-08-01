package com.hugman.mubble.object.entity.render;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.entity.GoombaEntity;
import com.hugman.mubble.object.entity.model.GoombaEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GoombaEntityRenderer extends MobEntityRenderer<GoombaEntity, GoombaEntityModel<GoombaEntity>> {
	private static final Identifier NORMAL_GOOMBA_TEXTURES = Mubble.MOD_DATA.id("textures/entity/goomba/normal.png");
	private static final Identifier GOLDEN_GOOMBA_TEXTURES = Mubble.MOD_DATA.id("textures/entity/goomba/golden.png");

	public GoombaEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher, new GoombaEntityModel<>(), 0.3F);
	}

	@Override
	public Identifier getTexture(GoombaEntity entity) {
		switch(entity.getVariant()) {
			default:
			case 0:
				return NORMAL_GOOMBA_TEXTURES;
			case 1:
				return GOLDEN_GOOMBA_TEXTURES;
		}
	}
}