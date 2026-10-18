package fr.hugman.mubble.zelda.client.renderer.entity;

import fr.hugman.mubble.zelda.client.model.LoftwingModel;
import fr.hugman.mubble.zelda.client.model.ZeldaModelLayers;
import fr.hugman.mubble.zelda.client.renderer.entity.state.LoftwingRenderState;
import fr.hugman.mubble.zelda.world.entity.animal.loftwing.Loftwing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class LoftwingRenderer extends MobRenderer<Loftwing, LoftwingRenderState, LoftwingModel> {
    public LoftwingRenderer(EntityRendererProvider.Context context) {
        super(context, new LoftwingModel(context.bakeLayer(ZeldaModelLayers.LOFTWING)), 1.2f);
    }

    @Override
    public LoftwingRenderState createRenderState() {
        return new LoftwingRenderState();
    }

    @Override
    public Identifier getTextureLocation(LoftwingRenderState state) {
        return state.texture;
    }

    @Override
    public void extractRenderState(Loftwing loftwing, LoftwingRenderState state, float partialTick) {
        super.extractRenderState(loftwing, state, partialTick);
        state.flapAnimationState.copyFrom(loftwing.flapAnimationState);
        state.texture = loftwing.getTexture();
        state.flying = loftwing.isFlying();
    }
}
