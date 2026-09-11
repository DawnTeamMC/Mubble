package fr.hugman.mubble.super_mario.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.client.model.GoombaModel;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.GoombaRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;

/**
 * Draws the mini goombas hanging off a player.
 * <p>
 * They are no longer entities by the time they get here — the player only carries a count — so this
 * places that many of them around the torso from the count alone. Their spots are worked out from the
 * index rather than drawn at random, so a goomba stays where it was between two frames.
 */
@Environment(EnvType.CLIENT)
public class ClingingMiniGoombaLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/mini/normal")).texturePath();

    /**
     * Undoes the 24-unit anchor every entity model is built around, the way the parrot on a shoulder
     * does, so that a mini goomba stands on the spot it is moved to rather than under it.
     */
    private static final float MODEL_ANCHOR = -1.5F;

    /** How small they are drawn against a player, rather than against a goomba. */
    private static final float SCALE = 0.45F;

    /** How far out from the middle of the player they sit. */
    private static final float RADIUS = 0.3F;

    /** The band of the body they spread over, measured down from the shoulders. */
    private static final float TOP = 0.05F;
    private static final float DEPTH = 0.55F;
    /** Crouching brings the shoulders forward and down, as it does for a parrot. */
    private static final float CROUCH_LIFT = 0.2F;

    /** The turn between one mini goomba and the next, so a crowd of them spreads instead of stacking. */
    private static final float SPREAD_ANGLE = 2.3999632F;
    /** How far down the band each further mini goomba moves, wrapped back to the top. */
    private static final float SPREAD_DEPTH = 0.37F;

    private final GoombaModel model;

    public ClingingMiniGoombaLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new GoombaModel(modelSet.bakeLayer(SuperMarioModelLayers.GOOMBA));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        Integer count = state.getData(SuperMarioRenderStateDataKeys.CLINGING_MINI_GOOMBAS);
        if (count == null || count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            this.submitOne(poseStack, submitNodeCollector, lightCoords, state, i);
        }
    }

    private void submitOne(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState playerState, int index) {
        float angle = index * SPREAD_ANGLE;
        float depth = TOP + (index * SPREAD_DEPTH % 1.0F) * DEPTH - (playerState.isCrouching ? CROUCH_LIFT : 0.0F);

        poseStack.pushPose();
        // The layer works in the entity's own frame, where up is negative and one unit is one block.
        poseStack.translate(Mth.sin(angle) * RADIUS, depth, Mth.cos(angle) * RADIUS);
        // Faces away from the player, so whoever is looking at them sees the faces rather than the backs.
        poseStack.mulPose(Axis.YP.rotation(angle + Mth.PI));
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.translate(0.0F, MODEL_ANCHOR, 0.0F);

        // A fresh state per mini goomba: the model is only posed once the submitted node is drawn, so a
        // shared one would have every goomba drawn in the pose of the last.
        GoombaRenderState goombaState = new GoombaRenderState();
        goombaState.texture = TEXTURE;
        goombaState.ageInTicks = playerState.ageInTicks;
        // Offset per goomba so they scrabble out of step with each other instead of in lockstep.
        goombaState.walkAnimationPos = playerState.walkAnimationPos + index;
        goombaState.walkAnimationSpeed = Math.max(playerState.walkAnimationSpeed, 0.35F);

        submitNodeCollector.submitModel(
                this.model, goombaState, poseStack, TEXTURE, lightCoords, OverlayTexture.NO_OVERLAY, playerState.outlineColor, null
        );
        poseStack.popPose();
    }
}
