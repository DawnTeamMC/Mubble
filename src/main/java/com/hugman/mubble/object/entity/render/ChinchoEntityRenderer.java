package com.hugman.mubble.object.entity.render;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.entity.ChinchoEntity;
import com.hugman.mubble.object.entity.model.ChinchoEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ChinchoEntityRenderer extends MobEntityRenderer<ChinchoEntity, ChinchoEntityModel<ChinchoEntity>> {
	private static final Identifier CHINCHO_TEXTURES = Mubble.MOD_DATA.id("textures/entity/chincho.png");

	public ChinchoEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher, new ChinchoEntityModel<>(), 0.5F);
	}

	@Override
	public Identifier getTexture(ChinchoEntity entity) {
		return CHINCHO_TEXTURES;
	}
}