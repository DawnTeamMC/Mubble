package com.hugman.mubble.object.entity.render;

import com.hugman.mubble.Mubble;
import com.hugman.mubble.object.entity.ToadEntity;
import com.hugman.mubble.object.entity.model.ToadEntityModel;
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
		return Mubble.MOD_DATA.id("textures/entity/toad/" + entity.getVariant().getName() + ".png");
	}
}