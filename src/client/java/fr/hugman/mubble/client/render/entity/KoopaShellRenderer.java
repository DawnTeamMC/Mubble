package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.entity.model.KoopaShellModel;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.render.entity.state.KoopaShellEntityRenderState;
import fr.hugman.mubble.entity.KoopaShellEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class KoopaShellRenderer<K extends KoopaShellEntity> extends EntityRenderer<K, KoopaShellEntityRenderState> {
    protected KoopaShellModel model;

    public KoopaShellRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new KoopaShellModel(context.getPart(MubbleModelLayers.KOOPA_SHELL));
        this.shadowRadius = 0.5f;
    }

    @Override
    public KoopaShellEntityRenderState createRenderState() {
        return new KoopaShellEntityRenderState();
    }

    @Override
    public void render(KoopaShellEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();

        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.translate(0.0F, -1.501F, 0.0F);
        this.model.setAngles(state);

        boolean showBody = !state.invisible;
        boolean translucent = !showBody && !state.invisibleToPlayer;
        RenderLayer renderLayer = this.getRenderLayer(state, showBody, translucent, state.hasOutline);
        if (renderLayer != null) {
            int i = OverlayTexture.packUv(OverlayTexture.getU(0.0f), OverlayTexture.getV(false));
            queue.submitModel(this.model, state, matrices, renderLayer, state.light, i, translucent ? 654311423 : Colors.WHITE, null, state.outlineColor, null);
        }

        matrices.pop();
        super.render(state, matrices, queue, cameraState);
    }

    @Nullable
    protected RenderLayer getRenderLayer(KoopaShellEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        if (translucent) {
            return RenderLayers.itemEntityTranslucentCull(state.texture);
        } else if (showBody) {
            return this.model.getLayer(state.texture);
        } else {
            return showOutline ? RenderLayers.outlineNoCull(state.texture) : null;
        }
    }

    @Override
    public void updateRenderState(K entity, KoopaShellEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);

        state.texture = entity.getTexture();

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        state.invisibleToPlayer = state.invisible && entity.isInvisibleTo(minecraftClient.player);
        state.hasOutline = minecraftClient.hasOutline(entity);

        state.horizontalRotation = entity.getHorizontalRotation(tickDelta);
    }
}
