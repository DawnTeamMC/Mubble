package fr.hugman.mubble.client.render;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.render.model.GoombaEntityModel;
import fr.hugman.mubble.entity.GoombaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class GoombaEntityRenderer extends MobEntityRenderer<GoombaEntity, GoombaEntityModel> {

    public GoombaEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GoombaEntityModel(context.getPart(MubbleRenderLayers.GOOMBA)), 0.5f);
    }

    @Override
    public Identifier getTexture(GoombaEntity entity) {
        return Mubble.id("textures/entity/goomba.png");
    }
}