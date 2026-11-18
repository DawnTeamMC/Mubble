package fr.hugman.mubble.super_mario.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.animal.bee.BeeStingerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

/**
 * One bee stinger on the lower back, with vanilla's own model and texture.
 */
@Environment(EnvType.CLIENT)
public class StingerLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_stinger.png");
    private static final float HEIGHT = 10.0F / 16.0F;
    private static final float BACK = 3.0F / 16.0F;
    private static final float SIZE = 2.0F;
    private static final float TILT = 20.0F;

    private final BeeStingerModel model;

    public StingerLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BeeStingerModel(modelSet.bakeLayer(ModelLayers.BEE_STINGER));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (state.getData(SuperMarioRenderStateDataKeys.STINGER) == null || state.isInvisible) {
            return;
        }
        poseStack.pushPose();
        this.getParentModel().root().translateAndRotate(poseStack);
        this.getParentModel().body.translateAndRotate(poseStack);
        poseStack.translate(0.0F, HEIGHT, BACK);
        // the model points along X: turned to point backwards, and slightly down
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(TILT));
        poseStack.scale(SIZE, SIZE, SIZE);
        submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, TEXTURE, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
    }
}
