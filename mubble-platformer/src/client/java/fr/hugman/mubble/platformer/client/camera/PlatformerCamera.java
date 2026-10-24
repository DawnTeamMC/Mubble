package fr.hugman.mubble.platformer.client.camera;

import fr.hugman.mubble.platformer.client.PlatformerClientConfig;
import fr.hugman.mubble.platformer.client.compat.PlatformerControllerBindings;
import fr.hugman.mubble.platformer.client.mixin.CameraInvoker;
import fr.hugman.mubble.platformer.PlatformerAim;
import fr.hugman.mubble.platformer.PlatformerCameraFollow;
import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerFraming;
import fr.hugman.mubble.platformer.PlatformerShoulder;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * The orbit camera of the platformer layer, in the third person view from the back.
 * <p>
 * The camera has its own yaw and pitch, moved by the mouse or the right stick, and the stick moves
 * the player relative to it: the body turns towards where it goes, the view does not follow, and the
 * head looks at the horizon. The camera frames the player with some room around them rather than nailing them to the middle (see {@link PlatformerFraming}), eases in when it backs into a
 * block (the player showing through whatever stands in between otherwise, see {@link PlatformerSilhouette}), swings back behind the player, level with the horizon and with the player in the middle, on recenter, follows them round as they move if set
 * to, slowly and late (see {@link PlatformerCameraFollow}), and blends with the vanilla view when one takes over from the other. The hands do not follow the camera: they hit and use items where the
 * body faces, see {@link PlatformerAim#pickAhead}.
 * <p>
 * Holding a bow, a crossbow, a trident or another weapon that aims (see {@link PlatformerShoulder}),
 * the camera moves in close behind the right shoulder and shows a crosshair. The body still faces
 * where it goes, as in the orbit; using the weapon (drawing the bow, shooting the loaded crossbow)
 * turns it to face the crosshair, as in the first person view, until a moment after the last use,
 * see {@link #facesCrosshair}: the hands then reach out to what the crosshair points at.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerCamera {
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
    /** How long the camera takes to go to the shoulder and back, in seconds. */
    private static final float AIM_SECONDS = 0.15F;
    /** Where the camera sits while aiming, in blocks at the scale of a player: behind the eyes, to their right, above them. */
    private static final double AIM_DISTANCE = 1.6D;
    private static final double AIM_SIDE = 0.6D;
    private static final double AIM_HEIGHT = 0.15D;
    /** How far the crosshair reaches while aiming, in blocks: past it, the body looks along the view. */
    private static final double AIM_RANGE = 96.0D;
    /** How long the body keeps facing the crosshair once the weapon was last used, in seconds, so that shots in a row do not swing it back and forth. */
    private static final float AIM_LINGER_SECONDS = 0.5F;
    /** How far the view may look up or down while aiming, in degrees, as in the first person view. */
    private static final float AIM_PITCH = 90.0F;

    private static float yaw;
    private static float pitch;
    /** The rotation of the camera on the last tick, to tell how far it turned since. */
    private static float tickYaw;
    private static float tickPitch;
    private static boolean orbiting;
    private static boolean recentering;
    /** What the camera looks at, framing the feet of the player, see {@link PlatformerFraming}. */
    private static final PlatformerFraming FRAMING = new PlatformerFraming();
    /** How the camera swings round behind the player on its own, see {@link PlatformerCameraFollow}. */
    private static final PlatformerCameraFollow FOLLOW = new PlatformerCameraFollow();
    /** The focus on the last two ticks: the camera interpolates between them. */
    @Nullable
    private static Vec3 focus;
    @Nullable
    private static Vec3 focusO;
    /** How far into the orbit the camera is, from the vanilla view (0) to the orbit (1). */
    private static float blend;
    /** How far the camera has moved to the shoulder, from framing the player (0) to aiming (1). */
    private static float aim;
    private static long lastUseNanos;
    private static long lastFrameNanos;
    /** How far behind the focus the camera sits, easing towards where it can, or below 0 before the first frame. */
    private static double distance = -1.0D;

    private PlatformerCamera() {
    }

    /** Whether the orbit camera is the one in use. */
    public static boolean isOrbiting() {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        boolean now = player != null
                && PlatformerClientConfig.get().orbitCamera()
                && minecraft.options.getCameraType() == CameraType.THIRD_PERSON_BACK
                && PlatformerController.of(player).isDriving();
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

    /** Whether the orbit camera aims over the shoulder, a weapon that aims in hand, see {@link PlatformerShoulder}. */
    public static boolean isAiming() {
        var player = Minecraft.getInstance().player;
        return isOrbiting() && PlatformerShoulder.aims(player);
    }

    /**
     * Whether the body faces the crosshair, as in the first person view: aiming, while the weapon is
     * in use (a bow drawn, a crossbow loading, a trident raised) and for a moment after it last was.
     * The rest of the time, it faces where it goes, as in the orbit.
     */
    public static boolean facesCrosshair() {
        var player = Minecraft.getInstance().player;
        return isAiming() && (player.isUsingItem() || System.nanoTime() - lastUseNanos < AIM_LINGER_SECONDS * 1.0E9F);
    }

    /** Whether the body of the player follows the view, as in the first person view: off the orbit, or facing the crosshair. */
    public static boolean coupled() {
        return !isOrbiting() || facesCrosshair();
    }

    /**
     * Called right before the hands use what they hold: aiming, the body turns to the crosshair at
     * once, so that what shoots on the click, a loaded crossbow, shoots where the crosshair points.
     *
     * @return whether the body turned, and what the hands reach out to has to be picked again
     */
    public static boolean onUse(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || !isAiming()) {
            return false;
        }
        lastUseNanos = System.nanoTime();
        var camera = minecraft.gameRenderer.mainCamera();
        aimBody(player, player.getEyePosition(), camera.position(), Vec3.directionFromRotation(camera.xRot(), camera.yRot()));
        return true;
    }

    /**
     * Called right before a power-up is triggered: one that shoots, a fire flower, turns the body to
     * the crosshair as a weapon does, and the server hears of the turn before the trigger, so that it
     * shoots where the crosshair points.
     */
    public static void beforePowerUp(LocalPlayer player, PowerUp powerUp) {
        if (powerUp.aims() && onUse(Minecraft.getInstance())) {
            player.connection.send(new ServerboundMovePlayerPacket.Rot(player.getYRot(), player.getXRot(), player.onGround(), player.horizontalCollision));
        }
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
        focus = FRAMING.tick(target, supported(player), PlatformerClientConfig.get().cameraMargin(), turned);
        // the focus must not slip behind a block, which would put the camera inside it: going down
        // steps, the edge left behind hides it for a tick, and it comes closer only that much
        focus = PlatformerAim.trail(player.level(), player, target, focus, player.getEyeHeight() + PlatformerClientConfig.get().cameraHeight());
        FRAMING.set(focus);
    }

    /** Whether the height of the camera follows the player: on the ground, a ladder, a wall or in water, not in the air. */
    private static boolean supported(LocalPlayer player) {
        var controller = PlatformerController.of(player);
        if (controller.isDriving()) {
            var state = controller.state();
            return state.grounded || state.move.kind() != PlatformerMove.Kind.AIR;
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
        var config = PlatformerClientConfig.get();
        // aiming turns the view as the first person view does
        double scale = 0.15D * Mth.lerp(aim, config.orbitSensitivity(), 1.0D);
        yaw = Mth.wrapDegrees((float) (yaw + yawDelta * scale));
        pitch = Mth.clamp((float) (pitch + pitchDelta * scale), minPitch(), maxPitch());
        if (yawDelta != 0.0D || pitchDelta != 0.0D) {
            // turning by hand takes over from a recenter and from following; a controller turns by nothing every frame its stick rests
            recentering = false;
            FOLLOW.touch();
        }
    }

    /** Swings the camera behind the player, level with the horizon, and brings the player back to the middle of the screen. */
    public static void recenter() {
        recentering = true;
        FRAMING.recenter();
    }

    /** How far up the view may look: the orbit's limit, opening up to the first person one while aiming. */
    private static float minPitch() {
        return Mth.lerp(aim, (float) PlatformerClientConfig.get().minPitch(), -AIM_PITCH);
    }

    private static float maxPitch() {
        return Mth.lerp(aim, (float) PlatformerClientConfig.get().maxPitch(), AIM_PITCH);
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
        boolean aiming = orbit && PlatformerShoulder.aims(player);
        float previousAim = aim;
        aim = blend > 0.0F ? Mth.approach(aim, aiming ? 1.0F : 0.0F, seconds / AIM_SECONDS) : 0.0F;
        if (blend <= 0.0F) {
            return;
        }
        // back from aiming, the view leaves the first person's pitch for the orbit's, as quickly
        pitch = Mth.clamp(pitch, minPitch(), maxPitch());

        var config = PlatformerClientConfig.get();
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
            follow(player, seconds);
        } else {
            FOLLOW.halt();
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
            double side = PlatformerAim.clearDistance(player.level(), player, center, right.scale(-1.0D), AIM_SIDE * player.getScale() * aim, CAMERA_BOX);
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
        // a roll or a dive turns the body its own way meanwhile, see PlatformerInputFrame.AIMING
        if (facesCrosshair() && !PlatformerController.of(player).state().move.facesItsOwnWay()) {
            aimBody(player, player.getEyePosition(partialTicks), camera.position(), Vec3.directionFromRotation(camera.xRot(), camera.yRot()));
        }
    }

    /**
     * How far behind the focus the camera sits this frame. Something between the camera and the
     * player does not move it, the player showing through instead (see {@link PlatformerSilhouette}); the
     * camera's own spot running into a block does, and the camera eases in, just far enough to stay
     * out of it, quickly, then back out slowly once there is room again. It only ever cuts to where it
     * can be when it would otherwise be inside a block.
     */
    private static double distance(LocalPlayer player, Vec3 center, Vec3 forward, float seconds, boolean aimMoving) {
        var level = player.level();
        double wanted = Mth.lerp(aim, PlatformerClientConfig.get().cameraDistance() * hintDistanceScale(player), AIM_DISTANCE) * player.getScale();
        double target = PlatformerAim.clearDistance(level, player, center, forward, wanted, OBSTRUCTION_MARGIN);
        // going to the shoulder and back is already eased
        if (distance < 0.0D || blend < 1.0F || aimMoving) {
            distance = target;
        } else {
            double rate = target < distance ? MOVE_IN_RATE : MOVE_OUT_RATE;
            distance += (target - distance) * (1.0D - Math.exp(-rate * seconds));
            if (!PlatformerAim.cameraFits(level, player, center.subtract(forward.scale(distance)), CAMERA_BOX)) {
                distance = Math.min(distance, PlatformerAim.clearDistance(level, player, center, forward, distance, 0.0D));
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
        var target = PlatformerAim.shoulderTarget(player.level(), player, camera, forward, eye, AIM_RANGE);
        var look = target.subtract(eye);
        var rotation = look.lengthSqr() > 1.0D && look.dot(forward) > 0.0D
                ? PlatformerShoulder.lookAt(eye, target)
                : new Vec2(pitch, yaw);
        player.setXRot(rotation.x);
        player.setYRot(rotation.y);
        player.setYHeadRot(rotation.y);
    }

    /** Whether the camera follows the player round as they move: a setting for the keyboard and mouse, one for controllers. */
    public static boolean autoCamera() {
        var config = PlatformerClientConfig.get();
        var controller = PlatformerControllerBindings.Holder.instance;
        return controller != null && controller.usingController() ? config.autoCameraController() : config.autoCameraKeyboard();
    }

    /**
     * Swings the camera round behind the player as they move, as the cameras of Nintendo's games do,
     * so gently that the player hardly notices: see {@link PlatformerCameraFollow}.
     */
    private static void follow(LocalPlayer player, float seconds) {
        var controller = PlatformerController.of(player);
        var profile = controller.profile();
        if (!autoCamera() || profile == null) {
            FOLLOW.reset();
            return;
        }
        double speed = controller.state().horizontalSpeed();
        double run = profile.physics().ground().runSpeed();
        yaw = Mth.wrapDegrees(yaw + FOLLOW.step(yaw, player.getYRot(), speed, run, PlatformerClientConfig.get().autoCameraSpeed(), seconds));
    }

    private static double hintDistanceScale(LocalPlayer player) {
        var profile = PlatformerController.of(player).profile();
        return profile != null && PlatformerClientConfig.get().respectProfileHints() ? profile.camera().distanceScale() : 1.0D;
    }

    /** The extra field of view the speed of the player is worth. */
    public static float fovKick(LocalPlayer player) {
        var controller = PlatformerController.of(player);
        var profile = controller.profile();
        if (!controller.isDriving() || profile == null) {
            return 1.0F;
        }
        double run = Math.max(1.0E-3D, profile.physics().ground().runSpeed());
        double share = Math.min(1.5D, controller.state().horizontalSpeed() / run);
        double hint = PlatformerClientConfig.get().respectProfileHints() ? profile.camera().fovKickScale() : 1.0D;
        // aiming, the view keeps still: a drawn bow zooms in already
        return (float) (1.0D + PlatformerClientConfig.get().fovKick() * hint * share * (1.0F - aim));
    }

    /** What the hands of the player reach out to with the orbit camera on: what stands where they face, see {@link PlatformerAim#pickAhead}. */
    public static HitResult pick(LocalPlayer player, float partialTicks) {
        return PlatformerAim.pickAhead(player, player.getEyePosition(partialTicks), player.getYRot());
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
