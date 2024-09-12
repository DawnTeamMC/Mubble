package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.MubbleRenderLayers;
import fr.hugman.mubble.client.render.entity.model.GoombaModel;
import fr.hugman.mubble.entity.GoombaEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class GoombaRenderer extends MobEntityRenderer<GoombaEntity, GoombaModel> {
    public GoombaRenderer(EntityRendererFactory.Context context) {
        super(context, new GoombaModel(context.getPart(MubbleRenderLayers.GOOMBA)), 0.4f);
    }

    @Override
    public Identifier getTexture(GoombaEntity entity) {
        return entity.getTexture();
    }

    @Override
    public void render(GoombaEntity goomba, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if(goomba.isSurprised()) {
            this.forceRotation(goomba);
        }
        super.render(goomba, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private void forceRotation(GoombaEntity goomba) {
        goomba.prevHeadYaw = goomba.headYaw;
        goomba.bodyYaw = goomba.headYaw;
        goomba.prevBodyYaw = goomba.bodyYaw;
        goomba.prevPitch = goomba.getPitch();
        goomba.prevYaw = goomba.getYaw();
    }
}