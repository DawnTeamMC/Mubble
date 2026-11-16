package fr.hugman.mubble.super_mario.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.BoomerangRenderState;
import fr.hugman.mubble.super_mario.world.entity.projectile.Boomerang;
import fr.hugman.mubble.util.SquashStretch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class BoomerangRenderer extends EntityRenderer<Boomerang, BoomerangRenderState> {
    private static final float ITEM_SCALE = 0.5F;
    /** Speed at which the boomerang is stretched the most. */
    private static final double FULL_STRETCH_SPEED = 1.0D;
    private static final float MAX_STRETCH = 0.4F;
    private static final float HANG_SQUASH = 0.25F;
    private static final float HANG_TICKS = 4.0F;
    private static final float REBOUND_SQUASH = 0.45F;
    private static final float REBOUND_TICKS = 8.0F;
    private static final float REBOUND_SWINGS = 2.0F;
    private static final float GRAB_SQUASH = 0.3F;
    private static final float GRAB_TICKS = 5.0F;

    private final ItemModelResolver itemModelResolver;

    public BoomerangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public BoomerangRenderState createRenderState() {
        return new BoomerangRenderState();
    }

    @Override
    public void extractRenderState(Boomerang boomerang, BoomerangRenderState state, float partialTicks) {
        super.extractRenderState(boomerang, state, partialTicks);
        this.itemModelResolver.updateForNonLiving(state.item, boomerang.getItem(), ItemDisplayContext.NONE, boomerang);
        state.spin = boomerang.getSpin(partialTicks);

        Vec3 motion = boomerang.getDeltaMovement();
        state.headingYaw = (float) (Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG);
        state.headingPitch = (float) (Mth.atan2(motion.y, motion.horizontalDistance()) * Mth.RAD_TO_DEG);
        float inPhase = boomerang.ticksInPhase(partialTicks);
        state.stretch = switch (boomerang.getPhase()) {
            // the turn-around: it bunches up before heading home
            case Boomerang.HANG -> -HANG_SQUASH * SquashStretch.pulse(inPhase / HANG_TICKS);
            case Boomerang.CAUGHT -> -0.5F * Math.min(1.0F, inPhase / Boomerang.CATCH_TICKS);
            default -> SquashStretch.fromSpeed(motion.length(), FULL_STRETCH_SPEED, MAX_STRETCH);
        };
        state.scale = boomerang.getPhase() == Boomerang.CAUGHT ? 1.0F - Ease.inBack(Math.min(1.0F, inPhase / Boomerang.CATCH_TICKS)) : 1.0F;

        float sinceRebound = boomerang.ticksSinceRebound(partialTicks);
        state.rebound = sinceRebound < 0.0F ? 0.0F : -REBOUND_SQUASH * SquashStretch.wobble(sinceRebound / REBOUND_TICKS, REBOUND_SWINGS);
        state.reboundAxis = boomerang.getReboundAxis();
        float sinceGrab = boomerang.ticksSinceGrab(partialTicks);
        state.pulse = sinceGrab < 0.0F ? 0.0F : -GRAB_SQUASH * SquashStretch.pulse(sinceGrab / GRAB_TICKS);
    }

    @Override
    public void submit(BoomerangRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.item.isEmpty() || state.scale <= 0.0F) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0F, state.boundingBoxHeight / 2.0F, 0.0F);

        scale(poseStack, switch (state.reboundAxis) {
            case X -> SquashStretch.alongX(state.rebound);
            case Y -> SquashStretch.alongY(state.rebound);
            case Z -> SquashStretch.alongZ(state.rebound);
        });
        // stretched along the heading without tilting the spin plane
        poseStack.mulPose(Axis.YP.rotationDegrees(state.headingYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-state.headingPitch));
        scale(poseStack, SquashStretch.alongZ(state.stretch));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.headingPitch));
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.headingYaw));
        scale(poseStack, SquashStretch.alongY(state.pulse));

        poseStack.mulPose(Axis.YP.rotationDegrees(state.spin));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        float size = ITEM_SCALE * state.scale;
        poseStack.scale(size, size, size);
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static void scale(PoseStack poseStack, Vector3f scale) {
        poseStack.scale(scale.x(), scale.y(), scale.z());
    }
}
