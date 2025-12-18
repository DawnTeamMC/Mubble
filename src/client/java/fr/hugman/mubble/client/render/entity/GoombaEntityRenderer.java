package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.entity.model.GoombaModel;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.render.entity.state.GoombaEntityRenderState;
import fr.hugman.mubble.entity.GoombaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class GoombaEntityRenderer extends MobRenderer<GoombaEntity, GoombaEntityRenderState, GoombaModel> {
    public GoombaEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GoombaModel(context.bakeLayer(MubbleModelLayers.GOOMBA)), 0.4f);
    }

    @Override
    public GoombaEntityRenderState createRenderState() {
        return new GoombaEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(GoombaEntityRenderState goombaRenderState) {
        return goombaRenderState.texture;
    }

    @Override
    public void extractRenderState(GoombaEntity goomba, GoombaEntityRenderState state, float f) {
        super.extractRenderState(goomba, state, f);
        state.surprisedAnimationState.copyFrom(goomba.surprisedAnimationState);
        state.crushAnimationState.copyFrom(goomba.crushAnimationState);
        state.texture = goomba.getTexture();
        state.stomped = goomba.isStomped();
    }
}