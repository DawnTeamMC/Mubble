package fr.hugman.mubble.client.render.entity;

import fr.hugman.mubble.client.render.entity.model.BallEntityModel;
import fr.hugman.mubble.client.render.entity.model.MubbleModelLayers;
import fr.hugman.mubble.client.render.entity.state.BallRenderState;
import fr.hugman.mubble.entity.BallEntity;
import fr.hugman.mubble.entity.FireballEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.CowEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class BallEntityRenderer extends EntityRenderer<BallEntity, BallRenderState> {
    private final BallEntityModel model;
    private final float MIN_SQUISH = 0.45f;
    private final float MAX_SQUISH = 2.0f;
    private final float SPEED_SQUISH_SCALE = 0.5f;

    public BallEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new BallEntityModel(ctx.getPart(MubbleModelLayers.FIREBALL));
    }

    @Override
    public BallRenderState createRenderState() {
        return new BallRenderState();
    }

	@Override
	public void render(BallRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
		matrices.push();

		matrices.translate(0.0D, state.height / 2.0D, 0.0D);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(state.age * 0.15F) * 360.0F));

		var newScale = (state.width * 2) / (BallEntityModel.SIZE / 16) - MAX_SQUISH * 2;
		matrices.scale(newScale, newScale, newScale);

		float squish = (float) Math.max(Math.min(1 - state.speed * SPEED_SQUISH_SCALE, MAX_SQUISH), MIN_SQUISH);
		matrices.scale(1 / squish, squish, squish);

		this.model.setAngles(state);
		queue.submitModel(this.model, state, matrices, this.model.getLayer(getTexture(state)), state.light, OverlayTexture.DEFAULT_UV, state.outlineColor, null);

		matrices.pop();

		super.render(state, matrices, queue, cameraState);
	}


    public Identifier getTexture(BallRenderState state) {
        return state.texture.texturePath();
    }

	@Override
    public void updateRenderState(BallEntity bullet, BallRenderState state, float f) {
        super.updateRenderState(bullet, state, f);
        state.pitch = bullet.getLerpedPitch(f);
        state.yaw = bullet.getLerpedYaw(f);
        state.speed = bullet.getSpeed();
        state.texture = bullet.getTexture();
    }
}