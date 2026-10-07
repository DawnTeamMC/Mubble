package fr.hugman.mubble.arcade.client.camera;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.client.ArcadeClientConfig;
import java.util.Optional;
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
 * The model of the player is drawn twice more, only where something already drawn stands in front of
 * it (the depth test is reversed), without writing depth:
 * <ol>
 *     <li>among the solid models, before any entity's ({@link #ORDER}), with nothing but the world
 *     drawn yet: it marks where the world hides the player, in the alpha of the frame;</li>
 *     <li>among the translucent ones, once the player, what they wear and what they hold are drawn: it
 *     tints the marked pixels gray, and only those, and puts their alpha back.</li>
 * </ol>
 * Drawing it in one go, among the translucent models, would test it against the player as well: the
 * layers of their skin, their armor and their held items, a hair in front of the body, would show
 * the body through them. Translucent models are drawn after every solid one, whatever their order, so
 * the mark is what keeps those out.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeSilhouette {
    /** How much of the gray shows over what hides the player. */
    private static final int OPACITY = 120;
    /** Only behind what is drawn already: the depth buffer is reversed, nearer is greater. */
    private static final DepthStencilState BEHIND = new DepthStencilState(CompareOp.LESS_THAN, false);
    /** The first pass: writes nothing but the alpha, the share of what hides the player left showing. */
    public static final RenderPipeline MARK_PIPELINE = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(Mubble.id("pipeline/arcade_silhouette_mark"))
            .withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_ALPHA))
            .withDepthStencilState(BEHIND)
            .withCull(true)
            .build());
    /**
     * The second pass: the gray weighs as much as the mark leaves out ({@code 1 - alpha}), what is
     * drawn as much as the mark says ({@code alpha}); an alpha of 1, anywhere the first pass did not
     * mark, leaves the pixel as it is. The alpha goes back to 1, so a pixel is only ever tinted once.
     */
    public static final RenderPipeline TINT_PIPELINE = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(Mubble.id("pipeline/arcade_silhouette_tint"))
            .withColorTargetState(new ColorTargetState(new BlendFunction(BlendFactor.ONE_MINUS_DST_ALPHA, BlendFactor.DST_ALPHA, BlendFactor.ONE, BlendFactor.ZERO)))
            .withDepthStencilState(BEHIND)
            .withCull(true)
            .build());
    /** Without blending, so among the solid models. */
    public static final RenderType MARK = RenderType.create("mubble_arcade_silhouette_mark", RenderSetup.builder(MARK_PIPELINE).createRenderSetup());
    /** With blending, so among the translucent models. */
    public static final RenderType TINT = RenderType.create("mubble_arcade_silhouette_tint", RenderSetup.builder(TINT_PIPELINE).createRenderSetup());
    /** The alpha the mark leaves, its color does not matter. */
    private static final int MARK_COLOR = ARGB.color(255 - OPACITY, 255, 255, 255);
    /** A light gray. */
    private static final int TINT_COLOR = ARGB.color(255, 190, 190, 190);
    /** Whether the render state of a player asks for its silhouette. */
    public static final RenderStateDataKey<Boolean> SHOWN = RenderStateDataKey.create(() -> "Mubble arcade silhouette");
    /** Before the submissions of the entities, at order 0, so that the mark only sees the world. */
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
            var submits = collector.order(ORDER);
            submits.submitModel(model, state, poseStack, MARK, state.lightCoords, OverlayTexture.NO_OVERLAY, MARK_COLOR, null, 0, null);
            submits.submitModel(model, state, poseStack, TINT, state.lightCoords, OverlayTexture.NO_OVERLAY, TINT_COLOR, null, 0, null);
        }
    }
}
