package fr.hugman.mubble.super_mario.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.client.model.GoombaModel;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.references.SuperMarioRenderStateDataKeys;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.GoombaRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.PlayerModelType;

import java.util.List;

/**
 * Draws the mini goombas hanging off a player, gnawing at them.
 * <p>
 * They are no longer entities by the time they get here — the player only carries a count — so each one
 * is given a spot from a fixed list, the first ones spread over the head, the torso, the arms and the legs
 * so that even a couple of them do not bunch up. Every spot is held by a body part, which keeps them
 * stuck to that part through the walk swing, crouching and looking around.
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

    /** How far out from the surface of the body part they stand, in model pixels, so their face meets it. */
    private static final float GAP = 1.6F;
    /** How far below the spot their feet go, in model pixels, so that it is their middle that sits on it. */
    private static final float HALF_HEIGHT = 2.6F;

    /** How far they lean into the body part they are biting. */
    private static final float LEAN = 0.5F;
    /** The one on top of the head bends further over, to bite down into it. */
    private static final float TOP_LEAN = 0.9F;
    /** The gnawing: a quick nod into the body part and back. */
    private static final float CHEW_SPEED = 0.9F;
    private static final float CHEW_AMPLITUDE = 0.18F;
    /** Sets each one's gnawing apart from the next, so they do not all nod in step. */
    private static final float CHEW_PHASE = 1.7F;

    /**
     * Where they hold on, in the order they take the spots. Coordinates are in the body part's own model
     * pixels, on the surface of the part, for the wide arm model; the arm spots move in for slim arms.
     */
    private static final List<Spot> SPOTS = List.of(
            Spot.side(Limb.BODY, 1.5F, 4.0F, -2.0F, 0.0F, Side.FRONT),
            Spot.top(Limb.HEAD, 0.5F, -8.0F, 0.0F),
            Spot.side(Limb.LEFT_ARM, 3.0F, 4.0F, 0.0F, -1.0F, Side.LEFT),
            Spot.side(Limb.RIGHT_LEG, 0.0F, 5.0F, -2.0F, 0.0F, Side.FRONT),
            Spot.side(Limb.BODY, -1.5F, 6.0F, 2.0F, 0.0F, Side.BACK),
            Spot.side(Limb.RIGHT_ARM, -3.0F, 6.0F, 0.0F, 1.0F, Side.RIGHT),
            Spot.side(Limb.LEFT_LEG, 2.0F, 9.5F, 0.0F, 0.0F, Side.LEFT),
            Spot.side(Limb.HEAD, -4.0F, -3.5F, 0.5F, 0.0F, Side.RIGHT),
            Spot.side(Limb.RIGHT_LEG, 0.0F, 10.0F, 2.0F, 0.0F, Side.BACK),
            Spot.side(Limb.HEAD, 1.5F, -2.5F, -4.0F, 0.0F, Side.FRONT),
            Spot.side(Limb.LEFT_ARM, 1.0F, 7.0F, -2.0F, -0.5F, Side.FRONT),
            Spot.side(Limb.LEFT_LEG, 0.0F, 10.5F, -2.0F, 0.0F, Side.FRONT)
    );

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
        Spot spot = SPOTS.get(index % SPOTS.size());
        PlayerModel parent = this.getParentModel();
        boolean slim = playerState.skin != null && playerState.skin.model() == PlayerModelType.SLIM;

        // Outwards from the surface the spot is on, which is the opposite of the way the goomba faces.
        float outX = Mth.sin(spot.yaw());
        float outZ = Mth.cos(spot.yaw());
        float gap = spot.onTop() ? 0.0F : GAP;
        float drop = spot.onTop() ? 0.0F : HALF_HEIGHT;
        float x = spot.x() + (slim ? spot.slimShift() : 0.0F) + outX * gap;
        float y = spot.y() + drop;
        float z = spot.z() + outZ * gap;

        float chew = Mth.sin(playerState.ageInTicks * CHEW_SPEED + index * CHEW_PHASE) * CHEW_AMPLITUDE;
        float lean = (spot.onTop() ? TOP_LEAN : LEAN) + chew;

        poseStack.pushPose();
        parent.root().translateAndRotate(poseStack);
        spot.limb().of(parent).translateAndRotate(poseStack);
        poseStack.translate(x / 16.0F, y / 16.0F, z / 16.0F);
        // Turned to face the body part, then tipped over from the feet so the head bites into it.
        poseStack.mulPose(Axis.YP.rotation(spot.yaw()));
        poseStack.mulPose(Axis.XP.rotation(lean));
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.translate(0.0F, MODEL_ANCHOR, 0.0F);

        // A fresh state per mini goomba: the model is only posed once the submitted node is drawn, so a
        // shared one would have every goomba drawn in the pose of the last. Its walk is left at rest: they
        // are holding on, not walking, however fast the player is.
        GoombaRenderState goombaState = new GoombaRenderState();
        goombaState.texture = TEXTURE;
        goombaState.ageInTicks = playerState.ageInTicks;

        submitNodeCollector.submitModel(
                this.model, goombaState, poseStack, TEXTURE, lightCoords, OverlayTexture.NO_OVERLAY, playerState.outlineColor, null
        );
        poseStack.popPose();
    }

    private enum Limb {
        HEAD, BODY, RIGHT_ARM, LEFT_ARM, RIGHT_LEG, LEFT_LEG;

        ModelPart of(PlayerModel model) {
            return switch (this) {
                case HEAD -> model.head;
                case BODY -> model.body;
                case RIGHT_ARM -> model.rightArm;
                case LEFT_ARM -> model.leftArm;
                case RIGHT_LEG -> model.rightLeg;
                case LEFT_LEG -> model.leftLeg;
            };
        }
    }

    /** Which face of a body part a spot is on, as the way out of it in the part's own model space. */
    private enum Side {
        FRONT(0.0F, -1.0F), BACK(0.0F, 1.0F), LEFT(1.0F, 0.0F), RIGHT(-1.0F, 0.0F);

        /** The turn that has a goomba on this face looking back into the part. */
        private final float yaw;

        Side(float outX, float outZ) {
            this.yaw = (float) Mth.atan2(outX, outZ);
        }
    }

    /**
     * One place a mini goomba can hold on.
     *
     * @param slimShift how far the spot moves along x on a slim-armed player, whose arms are a pixel thinner
     * @param yaw       the turn that has the goomba facing the body part it is on
     * @param onTop     whether it stands on top of the part rather than hanging off one of its sides
     */
    private record Spot(Limb limb, float x, float y, float z, float slimShift, float yaw, boolean onTop) {
        static Spot side(Limb limb, float x, float y, float z, float slimShift, Side side) {
            return new Spot(limb, x, y, z, slimShift, side.yaw, false);
        }

        /** Faces the way the player does, so it bends over the front of the part it stands on. */
        static Spot top(Limb limb, float x, float y, float z) {
            return new Spot(limb, x, y, z, 0.0F, 0.0F, true);
        }
    }
}
