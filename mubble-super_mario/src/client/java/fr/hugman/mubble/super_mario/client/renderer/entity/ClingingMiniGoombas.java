package fr.hugman.mubble.super_mario.client.renderer.entity;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.hugman.mubble.super_mario.SuperMario;
import fr.hugman.mubble.super_mario.client.model.GoombaModel;
import fr.hugman.mubble.super_mario.client.model.SuperMarioModelLayers;
import fr.hugman.mubble.super_mario.client.renderer.entity.state.GoombaRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Draws mini goombas that are holding onto a player, wherever that player is seen from.
 * <p>
 * By the time they are drawn they are no longer entities — the player only carries a count — so every
 * one of them is placed from its index alone: on the surface of a body part in third person and on the
 * first-person arm, or around the edges of the screen for the player they are holding.
 */
@Environment(EnvType.CLIENT)
public final class ClingingMiniGoombas {
    public static final Identifier TEXTURE = new ClientAsset.ResourceTexture(SuperMario.id("entity/goomba/mini/normal")).texturePath();

    /**
     * Undoes the 24-unit anchor every entity model is built around, the way the parrot on a shoulder
     * does, so that a mini goomba stands on the spot it is moved to rather than under it.
     */
    private static final float MODEL_ANCHOR = -1.5F;
    /** Brings the middle of the model, rather than its feet, onto the spot, for the ones on the screen. */
    private static final float MODEL_MIDDLE = -1.14F;

    /** How small they are drawn against a player, rather than against a goomba. */
    private static final float BODY_SCALE = 0.45F;
    /** How far out from the surface of the body part they stand, in model pixels, so their face meets it. */
    private static final float GAP = 1.6F;
    /** How far below the spot their feet go, in model pixels, so that it is their middle that sits on it. */
    private static final float HALF_HEIGHT = 2.6F;
    /** How far they lean into the body part they are biting. */
    private static final float LEAN = 0.5F;
    /** The one on top of the head bends further over, to bite down into it. */
    private static final float TOP_LEAN = 0.9F;

    /** The gnawing: a quick nod into what they are biting and back. */
    private static final float CHEW_SPEED = 0.9F;
    private static final float CHEW_AMPLITUDE = 0.18F;
    /** Sets each one's gnawing apart from the next, so they do not all nod in step. */
    private static final float CHEW_PHASE = 1.7F;

    /** Half the height of the screen at one block away, for the 70° field of view hands are drawn with. */
    private static final float HAND_HALF_FOV_TAN = 0.7002075F;
    private static final float SCREEN_SCALE = 0.22F;
    /** How far they lean towards the camera, to bite at the screen. */
    private static final float SCREEN_LEAN = 0.35F;
    /** How far a goomba in front of the view sways from side to side, having no edge to cling to. */
    private static final float SCREEN_SWAY = 0.2F;

    /**
     * Where they hold on to the screen, in the order they take the spots: around the edges first, with
     * the last two in front of the view, so it only gets blocked once there are really many of them.
     * Positions run from -1 to 1 across the screen, and the edge ones sit half off it.
     */
    private static final List<ScreenSpot> SCREEN_SPOTS = List.of(
            ScreenSpot.edge(-0.88F, -0.72F),
            ScreenSpot.edge(0.86F, 0.66F),
            ScreenSpot.edge(-0.92F, 0.22F),
            ScreenSpot.edge(0.9F, -0.3F),
            ScreenSpot.edge(-0.4F, 0.9F),
            ScreenSpot.edge(0.25F, -0.92F),
            ScreenSpot.front(-0.3F, 0.18F),
            ScreenSpot.front(0.28F, -0.12F)
    );

    /** How far the ones on the first-person arm turn their face towards the camera, off the arm. */
    private static final float ARM_TWIST = Mth.PI / 3.0F;

    /**
     * Where they hold on to the right arm in first person, as it is held out with nothing in the hand.
     * The front and the outer side are the faces turned towards the camera there, and only the hand end
     * of the arm is on screen, with the hand end at the top of it, so they hang head up towards the
     * hand rather than towards the shoulder. Biting straight into either of them would only ever show their backs, so
     * each one turns its face part of the way towards the other, and the camera, as it gnaws. The left
     * arm takes the mirror image of these.
     */
    private static final List<Spot> RIGHT_ARM_SPOTS = List.of(
            Spot.side(-1.0F, 8.5F, -2.0F, 0.5F, Side.FRONT).twisted(-ARM_TWIST).headFirst(),
            Spot.side(-3.0F, 7.0F, 0.5F, 1.0F, Side.RIGHT).twisted(ARM_TWIST).headFirst(),
            Spot.side(-2.0F, 6.0F, -2.0F, 0.5F, Side.FRONT).twisted(-ARM_TWIST).headFirst()
    );

    @Nullable
    private static EntityModelSet bakedFrom;
    @Nullable
    private static GoombaModel model;

    private ClingingMiniGoombas() {
    }

    /** The model for the places that are not handed a model set of their own, rebaked when it reloads. */
    public static GoombaModel model() {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        if (model == null || bakedFrom != modelSet) {
            model = new GoombaModel(modelSet.bakeLayer(SuperMarioModelLayers.GOOMBA));
            bakedFrom = modelSet;
        }
        return model;
    }

    /** How many of the mini goombas on a player show on their arm in first person: a few, not all. */
    public static int onArm(int clinging) {
        return Math.min(RIGHT_ARM_SPOTS.size(), (clinging + 2) / 3);
    }

    public static Spot armSpot(HumanoidArm arm, int index) {
        Spot spot = RIGHT_ARM_SPOTS.get(index % RIGHT_ARM_SPOTS.size());
        return arm == HumanoidArm.RIGHT ? spot : spot.mirrored();
    }

    /**
     * Draws one mini goomba biting into a body part, the pose stack being in that part's own space.
     *
     * @param slim whether the part is a slim arm, a pixel thinner than the spots are laid out for
     */
    public static void submitOnSurface(GoombaModel model, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor, float ageInTicks, Spot spot, boolean slim, int index) {
        // Outwards from the surface the spot is on, which is the opposite of the way the goomba faces.
        float outX = Mth.sin(spot.yaw());
        float outZ = Mth.cos(spot.yaw());
        float gap = spot.onTop() ? 0.0F : GAP;
        float drop = spot.onTop() ? 0.0F : spot.flipped() ? -HALF_HEIGHT : HALF_HEIGHT;
        float x = spot.x() + (slim ? spot.slimShift() : 0.0F) + outX * gap;
        float y = spot.y() + drop;
        float z = spot.z() + outZ * gap;

        poseStack.pushPose();
        poseStack.translate(x / 16.0F, y / 16.0F, z / 16.0F);
        // Turned to face the body part, then tipped over from the feet so the head bites into it.
        poseStack.mulPose(Axis.YP.rotation(spot.yaw() + spot.twist()));
        if (spot.flipped()) {
            poseStack.mulPose(Axis.ZP.rotation(Mth.PI));
        }
        poseStack.mulPose(Axis.XP.rotation((spot.onTop() ? TOP_LEAN : LEAN) + chew(ageInTicks, index)));
        poseStack.scale(BODY_SCALE, BODY_SCALE, BODY_SCALE);
        poseStack.translate(0.0F, MODEL_ANCHOR, 0.0F);
        submit(model, poseStack, submitNodeCollector, lightCoords, outlineColor, ageInTicks);
        poseStack.popPose();
    }

    /**
     * Draws the mini goombas holding onto the player whose eyes the camera is in, around the edges of
     * their view. The pose stack has to be in camera space, as it is at the start of the hand pass.
     */
    public static void submitOnScreen(GoombaModel model, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float ageInTicks, int count) {
        Window window = Minecraft.getInstance().getWindow();
        float aspect = (float) window.getWidth() / Math.max(1, window.getHeight());
        for (int i = 0; i < count; i++) {
            ScreenSpot spot = SCREEN_SPOTS.get(i % SCREEN_SPOTS.size());
            // Past the last spot they start over, a little further back so they do not sit exactly in one another.
            float depth = spot.depth() + 0.1F * (i / SCREEN_SPOTS.size());
            float halfHeight = depth * HAND_HALF_FOV_TAN;
            float x = spot.u() * halfHeight * aspect;
            float y = spot.v() * halfHeight;
            float chew = chew(ageInTicks, i);
            // The ones on an edge climb in from it, their head turned towards the middle of the screen.
            float roll = spot.edge() ? (float) Mth.atan2(x, -y) : chew * SCREEN_SWAY / CHEW_AMPLITUDE;

            poseStack.pushPose();
            poseStack.translate(x, y, -depth);
            // Turned to look the eye in the face rather than straight ahead: off to the side of the screen,
            // straight ahead is seen from the side, and the edge ones would only show the top of their head.
            poseStack.mulPose(new Quaternionf().rotationTo(0.0F, 0.0F, 1.0F, -x, -y, depth));
            poseStack.mulPose(Axis.ZP.rotation(roll));
            poseStack.mulPose(Axis.YP.rotation(Mth.PI));
            poseStack.mulPose(Axis.XP.rotation(-(SCREEN_LEAN + chew)));
            poseStack.scale(SCREEN_SCALE, SCREEN_SCALE, SCREEN_SCALE);
            // The same turn the entity renderer gives every model, which is built upside down.
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0F, MODEL_MIDDLE, 0.0F);
            submit(model, poseStack, submitNodeCollector, lightCoords, 0, ageInTicks);
            poseStack.popPose();
        }
    }

    private static float chew(float ageInTicks, int index) {
        return Mth.sin(ageInTicks * CHEW_SPEED + index * CHEW_PHASE) * CHEW_AMPLITUDE;
    }

    private static void submit(GoombaModel model, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor, float ageInTicks) {
        // A fresh state per mini goomba: the model is only posed once the submitted node is drawn, so a
        // shared one would have every goomba drawn in the pose of the last. Its walk is left at rest: they
        // are holding on, not walking, however fast the player is.
        GoombaRenderState state = new GoombaRenderState();
        state.texture = TEXTURE;
        state.ageInTicks = ageInTicks;
        submitNodeCollector.submitModel(model, state, poseStack, TEXTURE, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, null);
    }

    /** Which face of a body part a spot is on, as the way out of it in the part's own model space. */
    public enum Side {
        FRONT(0.0F, -1.0F), BACK(0.0F, 1.0F), LEFT(1.0F, 0.0F), RIGHT(-1.0F, 0.0F);

        /** The turn that has a goomba on this face looking back into the part. */
        private final float yaw;

        Side(float outX, float outZ) {
            this.yaw = (float) Mth.atan2(outX, outZ);
        }
    }

    /**
     * One place on a body part a mini goomba can hold on, in that part's own model pixels, on its surface.
     *
     * @param slimShift how far the spot moves along x on a slim arm, which is a pixel thinner
     * @param yaw       the turn that has the goomba facing the body part it is on
     * @param twist     how far it then turns away from facing the part head-on
     * @param onTop     whether it stands on top of the part rather than hanging off one of its sides
     * @param flipped   whether it hangs head first along the part, towards its far end, instead of upright
     */
    public record Spot(float x, float y, float z, float slimShift, float yaw, float twist, boolean onTop, boolean flipped) {
        public static Spot side(float x, float y, float z, float slimShift, Side side) {
            return new Spot(x, y, z, slimShift, side.yaw, 0.0F, false, false);
        }

        /** Faces the way the part does, so it bends over the front of the part it stands on. */
        public static Spot top(float x, float y, float z) {
            return new Spot(x, y, z, 0.0F, 0.0F, 0.0F, true, false);
        }

        public Spot twisted(float twist) {
            return new Spot(this.x, this.y, this.z, this.slimShift, this.yaw, twist, this.onTop, this.flipped);
        }

        public Spot headFirst() {
            return new Spot(this.x, this.y, this.z, this.slimShift, this.yaw, this.twist, this.onTop, true);
        }

        /** The same spot on the other side of the body. */
        public Spot mirrored() {
            return new Spot(-this.x, this.y, this.z, -this.slimShift, -this.yaw, -this.twist, this.onTop, this.flipped);
        }
    }

    /**
     * One place on the screen, from -1 to 1 across and up it.
     *
     * @param edge whether it clings to the edge of the screen, or hangs in front of the view
     */
    private record ScreenSpot(float u, float v, float depth, boolean edge) {
        static ScreenSpot edge(float u, float v) {
            return new ScreenSpot(u, v, 0.5F, true);
        }

        static ScreenSpot front(float u, float v) {
            return new ScreenSpot(u, v, 0.7F, false);
        }
    }
}
