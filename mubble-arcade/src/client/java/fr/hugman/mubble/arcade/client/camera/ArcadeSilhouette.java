package fr.hugman.mubble.arcade.client.camera;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.client.ArcadeClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;

/**
 * The silhouette of the player through what hides them from the orbit camera: the camera does not
 * move in for what stands between it and the player, so the player shows through it instead, in a
 * light translucent gray, as in Super Mario Odyssey.
 * <p>
 * The model of the player is drawn a second time, flat gray, only where something already drawn
 * stands in front of it (the depth test is reversed), without writing depth. It is drawn before the
 * model itself, so that the player never shows through their own arms and legs: the model then covers
 * the silhouette wherever it is in sight.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeSilhouette {
    /** Flat colors, only behind what is drawn already: the depth buffer is reversed, nearer is greater. */
    public static final RenderPipeline PIPELINE = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(Mubble.id("pipeline/arcade_silhouette"))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN, false))
            .withCull(true)
            .build());
    public static final RenderType RENDER_TYPE = RenderType.create("mubble_arcade_silhouette", RenderSetup.builder(PIPELINE).createRenderSetup());
    /** A light gray, half see-through. */
    public static final int COLOR = ARGB.color(120, 190, 190, 190);
    /** Whether the render state of a player asks for its silhouette. */
    public static final RenderStateDataKey<Boolean> SHOWN = RenderStateDataKey.create(() -> "Mubble arcade silhouette");
    /** Before the submissions of the entities, at order 0. */
    private static final int ORDER = -1;

    private ArcadeSilhouette() {
    }

    public static void init() {
    }

    /** Whether {@code entity} shows through what hides it: the player of this client, under the orbit camera, if set to. */
    public static boolean shown(Entity entity) {
        return entity == Minecraft.getInstance().player && !entity.isInvisible() && ArcadeClientConfig.get().silhouette() && ArcadeCamera.isOrbiting();
    }

    /** Submits the silhouette of the model about to be submitted, if its render state asks for it. */
    public static <S extends LivingEntityRenderState> void submit(Model<? super S> model, S state, PoseStack poseStack, SubmitNodeCollector collector) {
        if (Boolean.TRUE.equals(state.getData(SHOWN))) {
            collector.order(ORDER).submitModel(model, state, poseStack, RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, COLOR, null, 0, null);
        }
    }
}
