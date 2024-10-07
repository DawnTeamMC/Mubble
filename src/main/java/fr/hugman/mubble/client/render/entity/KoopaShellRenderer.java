package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.MubbleRenderLayers;
import fr.hugman.mubble.client.render.entity.model.KoopaShellModel;
import fr.hugman.mubble.client.render.entity.state.KoopaShellEntityRenderState;
import fr.hugman.mubble.entity.KoopaShellEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class KoopaShellRenderer<K extends KoopaShellEntity> extends EntityRenderer<K, KoopaShellEntityRenderState> {
    protected KoopaShellModel model;

    public KoopaShellRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new KoopaShellModel(context.getPart(MubbleRenderLayers.KOOPA_SHELL));
        this.shadowRadius = 0.5f;
    }

    @Override
    public KoopaShellEntityRenderState getRenderState() {
        return new KoopaShellEntityRenderState();
    }

    @Override
    public void render(KoopaShellEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.translate(0.0F, -1.501F, 0.0F);
        this.model.setAngles(state);

        boolean showBody = !state.invisible;
        boolean translucent = !showBody && !state.invisibleToPlayer;
        RenderLayer renderLayer = this.getRenderLayer(state, showBody, translucent, state.hasOutline);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
            int q = OverlayTexture.packUv(OverlayTexture.getU(0.0f), OverlayTexture.getV(false));
            this.model.render(matrices, vertexConsumer, light, q, translucent ? 654311423 : -1);
        }

        matrices.pop();
        super.render(state, matrices, vertexConsumers, light);
    }

    @Nullable
    protected RenderLayer getRenderLayer(KoopaShellEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(state.texture);
        } else if (showBody) {
            return this.model.getLayer(state.texture);
        } else {
            return showOutline ? RenderLayer.getOutline(state.texture) : null;
        }
    }

    @Override
    public void updateRenderState(K entity, KoopaShellEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);

        state.texture = entity.getTexture();

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        state.invisibleToPlayer = state.invisible && entity.isInvisibleTo(minecraftClient.player);
        state.hasOutline = minecraftClient.hasOutline(entity);

        float velocityLength = (float) entity.getVelocity().horizontalLength();
        float increment = velocityLength * 0.35f; // Adjust the scaling factor as needed
        float newRotation = MathHelper.lerp(tickDelta, entity.getHorizontalRotation(), entity.getHorizontalRotation() + increment);
        entity.setHorizontalRotation(newRotation);
        state.horizontalRotation = newRotation;
    }
}
