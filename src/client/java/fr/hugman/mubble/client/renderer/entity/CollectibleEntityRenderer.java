package fr.hugman.mubble.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.client.renderer.entity.state.CollectibleEntityRenderState;
import fr.hugman.mubble.world.entity.item.CollectibleEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.AABB;

public class CollectibleEntityRenderer extends EntityRenderer<CollectibleEntity, CollectibleEntityRenderState> {
	private final ItemModelResolver itemModelResolver;

	public CollectibleEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemModelResolver = context.getItemModelResolver();
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	@Override
	public CollectibleEntityRenderState createRenderState() {
		return new CollectibleEntityRenderState();
	}

	@Override
	public void extractRenderState(CollectibleEntity entity, CollectibleEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.NONE, entity);
	}

	@Override
	public void submit(final CollectibleEntityRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
		if (!state.item.isEmpty()) {
			poseStack.pushPose();
			AABB boundingBox = state.item.getModelBoundingBox();
			poseStack.scale(state.boundingBoxWidth, state.boundingBoxHeight, state.boundingBoxWidth);
			poseStack.translate(0.0F, -boundingBox.minY, 0.0F);
			poseStack.mulPose(Axis.YP.rotation(state.ageInTicks / 5.0F));
			state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
			poseStack.popPose();
			super.submit(state, poseStack, submitNodeCollector, camera);
		}
	}
}
