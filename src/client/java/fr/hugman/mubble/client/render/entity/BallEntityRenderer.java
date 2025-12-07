package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.entity.model.BallEntityModel;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.render.entity.state.BallRenderState;
import fr.hugman.mubble.entity.BallEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BallEntityRenderer extends EntityRenderer<BallEntity, BallRenderState> {
    private final BallEntityModel model;

    public BallEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new BallEntityModel(ctx.getPart(MubbleModelLayers.BALL));
    }

    @Override
    public BallRenderState createRenderState() {
        return new BallRenderState();
    }

	@Override
	public void render(BallRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(state.pitch));
        var size = 4;
        matrices.scale(state.width * size, state.height * size, state.width * size);
		queue.submitModel(this.model, state, matrices, RenderLayers.entityCutout(state.texture.texturePath()), state.light, OverlayTexture.DEFAULT_UV, state.outlineColor, null);
		matrices.pop();

		super.render(state, matrices, queue, cameraState);
	}

	@Override
    public void updateRenderState(BallEntity bullet, BallRenderState state, float f) {
        super.updateRenderState(bullet, state, f);
        state.pitch = bullet.getLerpedPitch(f);
        state.yaw = bullet.getLerpedYaw(f);
        state.texture = bullet.getTexture();
        state.light = 15728880;
    }
}