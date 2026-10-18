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
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws mini goombas that are holding onto a player, wherever that player is seen from.
 * <p>
 * By the time they are drawn they are no longer entities — the player only carries a count — so every
 * one of them is placed from its index alone: on the surface of a body part in third person, or around
 * the edges of the screen for the player they are holding.
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

    /** How long one takes to leap onto the screen, and to be flung back off it, in ticks. */
    private static final float ENTER_TICKS = 10.0F;
    private static final float EXIT_TICKS = 12.0F;
    /**
     * How far past their spot the ones leaping on overshoot before settling, and how far the ones being
     * flung off wind back first. Enough for a bit of give, kept short of a rubber band.
     */
    private static final float BACK_OVERSHOOT = 1.4F;
    /** How much they squash out sideways as they slam into place. */
    private static final float LANDING_SQUASH = 0.2F;
    /** How long they wobble for once there, as a share of the leap, and how far. */
    private static final float LANDING_WOBBLE = 0.35F;
    /** How far they stretch, and how many half turns they spin, on their way off. */
    private static final float FLUNG_STRETCH = 0.3F;
    private static final float FLUNG_SPIN = 1.0F;
    /** How far out of the middle a spot is pushed to be clear off the screen, from -1 to 1 across it. */
    private static final float OFF_SCREEN = 1.45F;

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

    /**
     * The mini goombas on the screen, spot by spot, as far as their leap on or off has got. A spot is
     * only let go of once the one in it has been flung all the way off, so letting go is seen too.
     */
    private static final List<OnScreen> ON_SCREEN = new ArrayList<>();

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
        float drop = spot.onTop() ? 0.0F : HALF_HEIGHT;
        float x = spot.x() + (slim ? spot.slimShift() : 0.0F) + outX * gap;
        float y = spot.y() + drop;
        float z = spot.z() + outZ * gap;

        poseStack.pushPose();
        poseStack.translate(x / 16.0F, y / 16.0F, z / 16.0F);
        // Turned to face the body part, then tipped over from the feet so the head bites into it.
        poseStack.mulPose(Axis.YP.rotation(spot.yaw()));
        poseStack.mulPose(Axis.XP.rotation((spot.onTop() ? TOP_LEAN : LEAN) + chew(ageInTicks, index)));
        poseStack.scale(BODY_SCALE, BODY_SCALE, BODY_SCALE);
        poseStack.translate(0.0F, MODEL_ANCHOR, 0.0F);
        submit(model, poseStack, submitNodeCollector, lightCoords, outlineColor, ageInTicks);
        poseStack.popPose();
    }

    /**
     * Draws the mini goombas holding onto the player whose eyes the camera is in, around the edges of
     * their view. The pose stack has to be in camera space, as it is at the start of the hand pass.
     * <p>
     * Meant to be called every first-person frame, none holding on included: a mini goomba that was just
     * shaken off is still on its way off the screen.
     *
     * @param time the player's age in ticks, partial tick included, which the leaps are timed against
     */
    public static void submitOnScreen(GoombaModel model, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float time, int count) {
        settleOnScreen(time, count);
        if (ON_SCREEN.isEmpty()) {
            return;
        }
        Window window = Minecraft.getInstance().getWindow();
        float aspect = (float) window.getWidth() / Math.max(1, window.getHeight());
        for (int i = 0; i < ON_SCREEN.size(); i++) {
            OnScreen goomba = ON_SCREEN.get(i);
            ScreenSpot spot = SCREEN_SPOTS.get(i % SCREEN_SPOTS.size());
            // Past the last spot they start over, a little further back so they do not sit exactly in one another.
            float depth = spot.depth() + 0.1F * (i / SCREEN_SPOTS.size());
            float halfHeight = depth * HAND_HALF_FOV_TAN;
            float chew = chew(time, i);

            // The way out of the screen from the spot, along which they leap on and are flung off.
            float outLength = Mth.sqrt(spot.u() * spot.u() + spot.v() * spot.v());
            float outU = outLength > 1.0E-3F ? spot.u() / outLength : 0.0F;
            float outV = outLength > 1.0E-3F ? spot.v() / outLength : -1.0F;
            float clear = (OFF_SCREEN - Math.max(Math.abs(spot.u()), Math.abs(spot.v()))) / Math.max(Math.abs(outU), Math.abs(outV));

            float push;
            float roll = 0.0F;
            float squashX = 1.0F;
            float squashY = 1.0F;
            if (goomba.leaving) {
                float progress = Mth.clamp((time - goomba.since) / EXIT_TICKS, 0.0F, 1.0F);
                if (progress >= 1.0F) {
                    continue;
                }
                // A short wind-up towards the middle, then off it goes, spinning and stretched by the speed.
                push = easeInBack(progress) * clear;
                roll = progress * progress * FLUNG_SPIN * Mth.PI * (i % 2 == 0 ? 1.0F : -1.0F);
                squashY = 1.0F + FLUNG_STRETCH * progress;
                squashX = 1.0F - FLUNG_STRETCH * 0.5F * progress;
            } else {
                float progress = Mth.clamp((time - goomba.since) / ENTER_TICKS, 0.0F, 1.0F);
                // Leaps in from off the screen, a little too far, and settles back with a wobble.
                push = (1.0F - easeOutBack(progress)) * clear;
                roll = Mth.sin(progress * 3.0F * Mth.PI) * (1.0F - progress) * LANDING_WOBBLE;
                float squash = Mth.sin(progress * Mth.PI) * LANDING_SQUASH;
                squashX = 1.0F + squash;
                squashY = 1.0F - squash;
            }

            float x = (spot.u() + outU * push) * halfHeight * aspect;
            float y = (spot.v() + outV * push) * halfHeight;
            // The ones on an edge climb in from it, their head turned towards the middle of the screen.
            float settledX = spot.u() * halfHeight * aspect;
            float settledY = spot.v() * halfHeight;
            roll += spot.edge() ? (float) Mth.atan2(settledX, -settledY) : chew * SCREEN_SWAY / CHEW_AMPLITUDE;

            poseStack.pushPose();
            poseStack.translate(x, y, -depth);
            // Turned to look the eye in the face rather than straight ahead: off to the side of the screen,
            // straight ahead is seen from the side, and the edge ones would only show the top of their head.
            poseStack.mulPose(new Quaternionf().rotationTo(0.0F, 0.0F, 1.0F, -x, -y, depth));
            poseStack.mulPose(Axis.ZP.rotation(roll));
            poseStack.scale(squashX, squashY, 1.0F);
            poseStack.mulPose(Axis.YP.rotation(Mth.PI));
            poseStack.mulPose(Axis.XP.rotation(-(SCREEN_LEAN + chew)));
            poseStack.scale(SCREEN_SCALE, SCREEN_SCALE, SCREEN_SCALE);
            // The same turn the entity renderer gives every model, which is built upside down.
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0F, MODEL_MIDDLE, 0.0F);
            submit(model, poseStack, submitNodeCollector, lightCoords, 0, time);
            poseStack.popPose();
        }
    }

    /**
     * Brings the goombas on the screen in line with how many are holding on: new ones leap on into the
     * first free spots, and the ones in the last spots are flung off as they are shaken loose.
     */
    private static void settleOnScreen(float time, int count) {
        for (OnScreen goomba : ON_SCREEN) {
            // The player's age starts over with a new player entity, after dying or changing dimension.
            if (time < goomba.since) {
                goomba.since = time - Math.max(ENTER_TICKS, EXIT_TICKS);
            }
        }
        for (int i = 0; i < count; i++) {
            if (i >= ON_SCREEN.size()) {
                ON_SCREEN.add(new OnScreen(time));
                continue;
            }
            OnScreen goomba = ON_SCREEN.get(i);
            if (goomba.leaving) {
                // Caught again on the way off: it leaps back from wherever it had got to.
                float gone = Mth.clamp((time - goomba.since) / EXIT_TICKS, 0.0F, 1.0F);
                goomba.leaving = false;
                goomba.since = time - (1.0F - gone) * ENTER_TICKS;
            }
        }
        for (int i = count; i < ON_SCREEN.size(); i++) {
            OnScreen goomba = ON_SCREEN.get(i);
            if (!goomba.leaving) {
                goomba.leaving = true;
                goomba.since = time;
            }
        }
        while (!ON_SCREEN.isEmpty()) {
            OnScreen last = ON_SCREEN.getLast();
            if (!last.leaving || time - last.since < EXIT_TICKS) {
                break;
            }
            ON_SCREEN.removeLast();
        }
    }

    private static float easeOutBack(float progress) {
        float t = progress - 1.0F;
        return 1.0F + (BACK_OVERSHOOT + 1.0F) * t * t * t + BACK_OVERSHOOT * t * t;
    }

    private static float easeInBack(float progress) {
        return (BACK_OVERSHOOT + 1.0F) * progress * progress * progress - BACK_OVERSHOOT * progress * progress;
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
     * @param onTop     whether it stands on top of the part rather than hanging off one of its sides
     */
    public record Spot(float x, float y, float z, float slimShift, float yaw, boolean onTop) {
        public static Spot side(float x, float y, float z, float slimShift, Side side) {
            return new Spot(x, y, z, slimShift, side.yaw, false);
        }

        /** Faces the way the part does, so it bends over the front of the part it stands on. */
        public static Spot top(float x, float y, float z) {
            return new Spot(x, y, z, 0.0F, 0.0F, true);
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

    /** A mini goomba on the screen, and when it started leaping onto it or being flung off. */
    private static final class OnScreen {
        private float since;
        private boolean leaving;

        private OnScreen(float since) {
            this.since = since;
        }
    }
}
