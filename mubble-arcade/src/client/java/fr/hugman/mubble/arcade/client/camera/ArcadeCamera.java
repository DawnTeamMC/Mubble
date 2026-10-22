package fr.hugman.mubble.arcade.client.camera;

import fr.hugman.mubble.arcade.client.ArcadeClientConfig;
import fr.hugman.mubble.arcade.client.compat.ArcadeControllerBindings;
import fr.hugman.mubble.arcade.client.mixin.CameraInvoker;
import fr.hugman.mubble.arcade.ArcadeAim;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadeFraming;
import fr.hugman.mubble.arcade.ArcadeShoulder;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * The orbit camera of the arcade layer, in the third person view from the back.
 * <p>
 * The camera has its own yaw and pitch, moved by the mouse or the right stick, and the stick moves
 * the player relative to it: the body turns towards where it goes, the view does not follow, and the
 * head looks at the horizon. The camera frames the player with some room around them rather than nailing them to the middle (see {@link ArcadeFraming}), eases in when it backs into a
 * block (the player showing through whatever stands in between otherwise, see {@link ArcadeSilhouette}), swings back behind the player, level with the horizon and with the player in the middle, on recenter, follows them round as they move if set
 * to, and blends with the vanilla view when one takes over from the other. The hands do not follow the camera: they hit and use items where the
 * body faces, see {@link ArcadeAim#pickAhead}.
 * <p>
 * Holding a bow, a crossbow, a trident or another weapon that aims (see {@link ArcadeShoulder}),
 * the camera moves in close behind the right shoulder and the body turns to face the crosshair, as in
 * the first person view: the hands then reach out to what the crosshair points at.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeCamera {
    /** How long the camera takes to go from the vanilla view to the orbit and back, in seconds. */
    private static final float BLEND_SECONDS = 0.25F;
    /** How fast the head of the player goes back to looking at the horizon, in degrees per tick. */
    private static final float HEAD_LEVEL_SPEED = 8.0F;
    /** How much room the camera keeps around itself, on top of its own box, before it moves in from a block. */
    private static final double OBSTRUCTION_MARGIN = 0.3D;
    /** The box of the camera itself, which never goes inside a block. */
    private static final double CAMERA_BOX = 0.1D;
    /** How fast the camera eases in from a block, and back out once there is room: the share of the way left covered per second, as a rate. */
    private static final double MOVE_IN_RATE = 14.0D;
    private static final double MOVE_OUT_RATE = 3.0D;
    /** How long the camera lets the player turn it by hand before following them again, in seconds. */
    private static final float AUTO_CAMERA_DELAY = 0.75F;
    /** The slowest the player may go for the camera to follow them, in blocks per tick: standing still, it never moves. */
    private static final double AUTO_CAMERA_MIN_SPEED = 0.02D;
    /** How long the camera takes to go to the shoulder and back, in seconds. */
    private static final float AIM_SECONDS = 0.15F;
    /** Where the camera sits while aiming, in blocks at the scale of a player: behind the eyes, to their right, above them. */
    private static final double AIM_DISTANCE = 1.6D;
    private static final double AIM_SIDE = 0.6D;
    private static final double AIM_HEIGHT = 0.15D;
    /** How far the crosshair reaches while aiming, in blocks: past it, the body looks along the view. */
    private static final double AIM_RANGE = 96.0D;
    /** How far the view may look up or down while aiming, in degrees, as in the first person view. */
    private static final float AIM_PITCH = 90.0F;

    private static float yaw;
    private static float pitch;
    /** The rotation of the camera on the last tick, to tell how far it turned since. */
    private static float tickYaw;
    private static float tickPitch;
    private static boolean orbiting;
    private static boolean recentering;
    /** What the camera looks at, framing the feet of the player, see {@link ArcadeFraming}. */
    private static final ArcadeFraming FRAMING = new ArcadeFraming();
    /** The focus on the last two ticks: the camera interpolates between them. */
    @Nullable
    private static Vec3 focus;
    @Nullable
    private static Vec3 focusO;
    /** How far into the orbit the camera is, from the vanilla view (0) to the orbit (1). */
    private static float blend;
    /** How far the camera has moved to the shoulder, from framing the player (0) to aiming (1). */
    private static float aim;
    private static long lastFrameNanos;
    private static long lastManualTurnNanos;
    /** How far behind the focus the camera sits, easing towards where it can, or below 0 before the first frame. */
    private static double distance = -1.0D;

    private ArcadeCamera() {
    }

    /** Whether the orbit camera is the one in use. */
    public static boolean isOrbiting() {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        boolean now = player != null
                && ArcadeClientConfig.get().orbitCamera()
                && minecraft.options.getCameraType() == CameraType.THIRD_PERSON_BACK
                && ArcadeController.of(player).isDriving();
        if (now && !orbiting) {
            // the orbit starts where the vanilla camera was
            yaw = player.getYRot();
            pitch = player.getXRot();
        } else if (!now && orbiting && player != null) {
            // and hands back to vanilla looking where it looked, so that the view goes on from there
            player.setYRot(yaw);
            player.setXRot(pitch);
            player.yRotO = yaw;
            player.xRotO = pitch;
            player.setYHeadRot(yaw);
            player.yHeadRotO = yaw;
        }
        orbiting = now;
        return now;
    }

    /** Whether the orbit camera aims over the shoulder: the body then faces the crosshair, see {@link ArcadeShoulder}. */
    public static boolean isAiming() {
        var player = Minecraft.getInstance().player;
        return isOrbiting() && ArcadeShoulder.aims(player);
    }

    /** Whether the body of the player follows the view, as in the first person view: off the orbit, or aiming. */
    public static boolean coupled() {
        return !isOrbiting() || isAiming();
    }

    /** How far the camera has moved to the shoulder, from 0 to 1, for the debug HUD and the crosshair. */
    public static float aim() {
        return aim;
    }

    /** Whether the crosshair shows, as in the first person view: once the camera is most of the way to the shoulder. */
    public static boolean showsCrosshair() {
        return aim >= 0.5F && isAiming();
    }

    /**
     * Called every client tick: the focus takes one step after the player. Stepping once a tick and
     * interpolating in between keeps the trailing smooth whatever the frame rate.
     */
    public static void tick(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || (!isOrbiting() && blend <= 0.0F)) {
            FRAMING.reset();
            focus = null;
            focusO = null;
            return;
        }
        var target = player.position();
        focusO = focus == null ? target : focus;
        // however it turned (the mouse, the stick, following the player), turning lets go of the offset
        double turned = Math.abs(Mth.degreesDifference(tickYaw, yaw)) + Math.abs(pitch - tickPitch);
        tickYaw = yaw;
        tickPitch = pitch;
        focus = FRAMING.tick(target, supported(player), ArcadeClientConfig.get().cameraMargin(), turned);
        // the focus must not slip behind a block, which would put the camera inside it: going down
        // steps, the edge left behind hides it for a tick, and it comes closer only that much
        focus = ArcadeAim.trail(player.level(), player, target, focus, player.getEyeHeight() + ArcadeClientConfig.get().cameraHeight());
        FRAMING.set(focus);
    }

    /** Whether the height of the camera follows the player: on the ground, a ladder, a wall or in water, not in the air. */
    private static boolean supported(LocalPlayer player) {
        var controller = ArcadeController.of(player);
        if (controller.isDriving()) {
            var state = controller.state();
            return state.grounded || state.move.kind() != ArcadeMove.Kind.AIR;
        }
        return player.onGround() || player.isInWater() || player.onClimbable();
    }

    public static float yaw() {
        return yaw;
    }

    public static float pitch() {
        return pitch;
    }

    /** Turns the camera, the way {@code Entity.turn} turns the view: called with what the mouse or right stick moved. */
    public static void turn(double yawDelta, double pitchDelta) {
        var config = ArcadeClientConfig.get();
        // aiming turns the view as the first person view does
        double scale = 0.15D * Mth.lerp(aim, config.orbitSensitivity(), 1.0D);
        yaw = Mth.wrapDegrees((float) (yaw + yawDelta * scale));
        pitch = Mth.clamp((float) (pitch + pitchDelta * scale), minPitch(), maxPitch());
        if (yawDelta != 0.0D || pitchDelta != 0.0D) {
            // turning by hand takes over from a recenter and from following; a controller turns by nothing every frame its stick rests
            recentering = false;
            lastManualTurnNanos = System.nanoTime();
        }
    }

    /** Swings the camera behind the player, level with the horizon, and brings the player back to the middle of the screen. */
    public static void recenter() {
        recentering = true;
        FRAMING.recenter();
    }

    /** How far up the view may look: the orbit's limit, opening up to the first person one while aiming. */
    private static float minPitch() {
        return Mth.lerp(aim, (float) ArcadeClientConfig.get().minPitch(), -AIM_PITCH);
    }

    private static float maxPitch() {
        return Mth.lerp(aim, (float) ArcadeClientConfig.get().maxPitch(), AIM_PITCH);
    }

    /**
     * Puts the camera in place for the frame, once vanilla aligned it with the player: on its own
     * rotation, around the focus trailing the player, as far back as the blocks allow. Going in and
     * out of the orbit, it blends with the vanilla view rather than cutting to it.
     */
    public static void frame(Camera camera, LocalPlayer player, float partialTicks) {
        long now = System.nanoTime();
        float seconds = lastFrameNanos == 0L ? 0.0F : Math.min(0.25F, (now - lastFrameNanos) / 1.0E9F);
        lastFrameNanos = now;

        boolean orbit = isOrbiting();
        // only the third person view from the back is something to blend with
        boolean blendable = Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK;
        float targetBlend = orbit ? 1.0F : 0.0F;
        blend = blendable ? Mth.approach(blend, targetBlend, seconds / BLEND_SECONDS) : targetBlend;
        boolean aiming = orbit && ArcadeShoulder.aims(player);
        float previousAim = aim;
        aim = blend > 0.0F ? Mth.approach(aim, aiming ? 1.0F : 0.0F, seconds / AIM_SECONDS) : 0.0F;
        if (blend <= 0.0F) {
            return;
        }
        // back from aiming, the view leaves the first person's pitch for the orbit's, as quickly
        pitch = Mth.clamp(pitch, minPitch(), maxPitch());

        var config = ArcadeClientConfig.get();
        if (aiming) {
            recentering = false;
        }
        if (recentering && orbit) {
            float target = player.getYRot();
            float step = (float) (config.recenterSpeed() * seconds);
            yaw = Mth.approachDegrees(yaw, target, step);
            float horizon = (float) Mth.clamp(0.0D, config.minPitch(), config.maxPitch());
            pitch = Mth.approach(pitch, horizon, step);
            if (Math.abs(Mth.degreesDifference(yaw, target)) < 0.5F && Math.abs(pitch - horizon) < 0.5F) {
                recentering = false;
            }
        }

        if (orbit && !recentering && !aiming) {
            follow(player, now, seconds);
        }

        var invoker = (CameraInvoker) camera;
        var position = player.getPosition(partialTicks);
        var feet = focus != null && focusO != null
                ? focusO.lerp(focus, partialTicks)
                : position;
        // aiming, the camera leaves the framing for the player themselves
        feet = feet.lerp(position, aim);
        // the eye height vanilla smooths, so that changing pose (a roll, a dive) does not jerk the view
        float eye = Mth.lerp(partialTicks, invoker.mubble$getEyeHeightOld(), invoker.mubble$getEyeHeight());
        var center = feet.add(0.0D, eye + Mth.lerp(aim, config.cameraHeight(), AIM_HEIGHT * player.getScale()), 0.0D);
        var forward = Vec3.directionFromRotation(pitch, yaw);
        if (aim > 0.0F) {
            // to the right of the eyes, as far as the blocks let it
            var right = Vec3.directionFromRotation(0.0F, yaw + 90.0F);
            double side = ArcadeAim.clearDistance(player.level(), player, center, right.scale(-1.0D), AIM_SIDE * player.getScale() * aim, CAMERA_BOX);
            center = center.add(right.scale(side));
        }
        var orbitPosition = center.subtract(forward.scale(distance(player, center, forward, seconds, aim != previousAim)));

        if (blend >= 1.0F) {
            invoker.mubble$setRotation(yaw, pitch);
            invoker.mubble$setPosition(orbitPosition);
        } else {
            float t = blend * blend * (3.0F - 2.0F * blend);
            var vanillaPosition = camera.position();
            float vanillaYaw = camera.yRot();
            float vanillaPitch = camera.xRot();
            invoker.mubble$setRotation(vanillaYaw + Mth.degreesDifference(vanillaYaw, yaw) * t, Mth.lerp(t, vanillaPitch, pitch));
            invoker.mubble$setPosition(vanillaPosition.lerp(orbitPosition, t));
        }
        if (aiming) {
            aimBody(player, player.getEyePosition(partialTicks), camera.position(), Vec3.directionFromRotation(camera.xRot(), camera.yRot()));
        }
    }

    /**
     * How far behind the focus the camera sits this frame. Something between the camera and the
     * player does not move it, the player showing through instead (see {@link ArcadeSilhouette}); the
     * camera's own spot running into a block does, and the camera eases in, just far enough to stay
     * out of it, quickly, then back out slowly once there is room again. It only ever cuts to where it
     * can be when it would otherwise be inside a block.
     */
    private static double distance(LocalPlayer player, Vec3 center, Vec3 forward, float seconds, boolean aimMoving) {
        var level = player.level();
        double wanted = Mth.lerp(aim, ArcadeClientConfig.get().cameraDistance() * hintDistanceScale(player), AIM_DISTANCE) * player.getScale();
        double target = ArcadeAim.clearDistance(level, player, center, forward, wanted, OBSTRUCTION_MARGIN);
        // going to the shoulder and back is already eased
        if (distance < 0.0D || blend < 1.0F || aimMoving) {
            distance = target;
        } else {
            double rate = target < distance ? MOVE_IN_RATE : MOVE_OUT_RATE;
            distance += (target - distance) * (1.0D - Math.exp(-rate * seconds));
            if (!ArcadeAim.cameraFits(level, player, center.subtract(forward.scale(distance)), CAMERA_BOX)) {
                distance = Math.min(distance, ArcadeAim.clearDistance(level, player, center, forward, distance, 0.0D));
            }
        }
        return distance;
    }

    /**
     * Turns the body of the player to face what the crosshair points at, seen from the camera at
     * {@code camera}: the eyes look at it, so that what they shoot or reach out to is what the
     * crosshair shows, wherever the camera sits beside them. Too close to tell, they look along the view.
     */
    private static void aimBody(LocalPlayer player, Vec3 eye, Vec3 camera, Vec3 forward) {
        if (ArcadeController.of(player).state().move.facesItsOwnWay()) {
            // a roll or a dive turns the body its own way, see ArcadeInputFrame.AIMING
            return;
        }
        var target = ArcadeAim.shoulderTarget(player.level(), player, camera, forward, eye, AIM_RANGE);
        var look = target.subtract(eye);
        var rotation = look.lengthSqr() > 1.0D && look.dot(forward) > 0.0D
                ? ArcadeShoulder.lookAt(eye, target)
                : new Vec2(pitch, yaw);
        player.setXRot(rotation.x);
        player.setYRot(rotation.y);
        player.setYHeadRot(rotation.y);
    }

    /** Whether the camera follows the player round as they move: a setting for the keyboard and mouse, one for controllers. */
    public static boolean autoCamera() {
        var config = ArcadeClientConfig.get();
        var controller = ArcadeControllerBindings.Holder.instance;
        return controller != null && controller.usingController() ? config.autoCameraController() : config.autoCameraKeyboard();
    }

    /**
     * Swings the camera round behind the player as they move, as the cameras of Nintendo's games do:
     * only while they move, faster the faster they go, and the more they go sideways to the view, so
     * that running towards the camera never turns it around. Turning it by hand pauses it a moment.
     */
    private static void follow(LocalPlayer player, long now, float seconds) {
        if (!autoCamera() || seconds <= 0.0F || now - lastManualTurnNanos < AUTO_CAMERA_DELAY * 1.0E9F) {
            return;
        }
        var controller = ArcadeController.of(player);
        var profile = controller.profile();
        double speed = controller.state().horizontalSpeed();
        if (profile == null || speed < AUTO_CAMERA_MIN_SPEED) {
            return;
        }
        double share = Math.min(1.0D, speed / Math.max(1.0E-3D, profile.physics().ground().runSpeed()));
        float target = player.getYRot();
        double sideways = Math.abs(Math.sin(Mth.degreesDifference(yaw, target) * Mth.DEG_TO_RAD));
        yaw = Mth.approachDegrees(yaw, target, (float) (ArcadeClientConfig.get().autoCameraSpeed() * share * sideways * seconds));
    }

    private static double hintDistanceScale(LocalPlayer player) {
        var profile = ArcadeController.of(player).profile();
        return profile != null && ArcadeClientConfig.get().respectProfileHints() ? profile.camera().distanceScale() : 1.0D;
    }

    /** The extra field of view the speed of the player is worth. */
    public static float fovKick(LocalPlayer player) {
        var controller = ArcadeController.of(player);
        var profile = controller.profile();
        if (!controller.isDriving() || profile == null) {
            return 1.0F;
        }
        double run = Math.max(1.0E-3D, profile.physics().ground().runSpeed());
        double share = Math.min(1.5D, controller.state().horizontalSpeed() / run);
        double hint = ArcadeClientConfig.get().respectProfileHints() ? profile.camera().fovKickScale() : 1.0D;
        // aiming, the view keeps still: a drawn bow zooms in already
        return (float) (1.0D + ArcadeClientConfig.get().fovKick() * hint * share * (1.0F - aim));
    }

    /** What the hands of the player reach out to with the orbit camera on: what stands where they face, see {@link ArcadeAim#pickAhead}. */
    public static HitResult pick(LocalPlayer player, float partialTicks) {
        return ArcadeAim.pickAhead(player, player.getEyePosition(partialTicks), player.getYRot());
    }

    /**
     * Keeps the head of the player level with the horizon while the orbit camera is on: the hands
     * reach out where the body faces, whatever the camera looks at.
     */
    public static void levelHead(LocalPlayer player) {
        if (isOrbiting()) {
            player.setXRot(Mth.approach(player.getXRot(), 0.0F, HEAD_LEVEL_SPEED));
        }
    }
}
