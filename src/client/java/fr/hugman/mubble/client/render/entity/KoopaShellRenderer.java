package fr.hugman.mubble.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.client.render.entity.model.KoopaShellModel;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.render.entity.state.KoopaShellEntityRenderState;
import fr.hugman.mubble.entity.KoopaShellEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.CommonColors;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class KoopaShellRenderer<K extends KoopaShellEntity> extends EntityRenderer<K, KoopaShellEntityRenderState> {
    protected final KoopaShellModel model;

    public KoopaShellRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new KoopaShellModel(context.bakeLayer(MubbleModelLayers.KOOPA_SHELL));
        this.shadowRadius = 0.5f;
    }

    @Override
    public KoopaShellEntityRenderState createRenderState() {
        return new KoopaShellEntityRenderState();
    }

    @Override
    public void submit(KoopaShellEntityRenderState koopaShellRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        this.model.setupAnim(koopaShellRenderState);

        boolean showBody = !koopaShellRenderState.isInvisible;
        boolean translucent = !showBody && !koopaShellRenderState.invisibleToPlayer;
        RenderType renderLayer = this.getRenderType(koopaShellRenderState, showBody, translucent, koopaShellRenderState.hasOutline);
        if (renderLayer != null) {
            int i = OverlayTexture.pack(OverlayTexture.u(0.0f), OverlayTexture.v(false));
            submitNodeCollector.submitModel(this.model, koopaShellRenderState, poseStack, renderLayer, koopaShellRenderState.lightCoords, i, translucent ? 654311423 : CommonColors.WHITE, null, koopaShellRenderState.outlineColor, null);
        }

        poseStack.popPose();
        super.submit(koopaShellRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    @Nullable
    protected RenderType getRenderType(KoopaShellEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
        if (translucent) {
            return RenderTypes.itemEntityTranslucentCull(state.texture);
        } else if (showBody) {
            return this.model.renderType(state.texture);
        } else {
            return showOutline ? RenderTypes.outline(state.texture) : null;
        }
    }

    @Override
    public void extractRenderState(K koopaShell, KoopaShellEntityRenderState koopaShellRenderState, float f) {
        super.extractRenderState(koopaShell, koopaShellRenderState, f);

        koopaShellRenderState.texture = koopaShell.getTexture();

        Minecraft minecraftClient = Minecraft.getInstance();
        koopaShellRenderState.invisibleToPlayer = koopaShellRenderState.isInvisible && koopaShell.isInvisibleTo(minecraftClient.player);
        koopaShellRenderState.hasOutline = minecraftClient.shouldEntityAppearGlowing(koopaShell);

        koopaShellRenderState.horizontalRotation = koopaShell.getHorizontalRotation(f);
    }
}
