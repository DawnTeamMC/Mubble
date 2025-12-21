package fr.hugman.mubble.client.renderer.entity;

import fr.hugman.mubble.client.model.GoombaModel;
import fr.hugman.mubble.client.model.MubbleModelLayers;
import fr.hugman.mubble.client.renderer.entity.state.GoombaRenderState;
import fr.hugman.mubble.world.entity.monster.goomba.Goomba;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class GoombaRenderer extends MobRenderer<Goomba, GoombaRenderState, GoombaModel> {
    public GoombaRenderer(EntityRendererProvider.Context context) {
        super(context, new GoombaModel(context.bakeLayer(MubbleModelLayers.GOOMBA)), 0.4f);
    }

    @Override
    public GoombaRenderState createRenderState() {
        return new GoombaRenderState();
    }

    @Override
    public Identifier getTextureLocation(GoombaRenderState goombaRenderState) {
        return goombaRenderState.texture;
    }

    @Override
    public void extractRenderState(Goomba goomba, GoombaRenderState state, float f) {
        super.extractRenderState(goomba, state, f);
        state.surprisedAnimationState.copyFrom(goomba.surprisedAnimationState);
        state.crushAnimationState.copyFrom(goomba.crushAnimationState);
        state.texture = goomba.getTexture();
        state.stomped = goomba.isStomped();
    }
}