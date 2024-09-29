package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.MubbleRenderLayers;
import fr.hugman.mubble.client.render.entity.model.GoombaModel;
import fr.hugman.mubble.client.render.entity.state.GoombaEntityRenderState;
import fr.hugman.mubble.entity.GoombaEntity;
import fr.hugman.mubble.entity.GoombaVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.CamelEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GoombaRenderer extends MobEntityRenderer<GoombaEntity, GoombaEntityRenderState, GoombaModel> {
    public GoombaRenderer(EntityRendererFactory.Context context) {
        super(context, new GoombaModel(context.getPart(MubbleRenderLayers.GOOMBA)), 0.4f);
    }

    @Override
    public Identifier getTexture(GoombaEntityRenderState state) {
        return state.texture;
    }

    public GoombaEntityRenderState getRenderState() {
        return new GoombaEntityRenderState();
    }

    @Override
    public void updateRenderState(GoombaEntity goomba, GoombaEntityRenderState state, float f) {
        super.updateRenderState(goomba, state, f);
        state.surprisedAnimationState.copyFrom(goomba.surprisedAnimationState);
        state.crushAnimationState.copyFrom(goomba.crushAnimationState);
        state.texture = goomba.getTexture();
        state.surprised = goomba.isSurprised();
        state.stomped = goomba.isStomped();

        // Force the rotation when the Goomba is surprised
        if(state.surprised) {
            state.bodyYaw = goomba.headYaw;
            state.yawDegrees = MathHelper.wrapDegrees(goomba.headYaw - goomba.bodyYaw);
            state.pitch = goomba.getPitch();
        }
    }
}