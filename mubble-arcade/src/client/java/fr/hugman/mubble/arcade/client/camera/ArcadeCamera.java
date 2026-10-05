package fr.hugman.mubble.arcade.client.camera;

import fr.hugman.mubble.arcade.client.ArcadeClientConfig;
import fr.hugman.mubble.arcade.client.mixin.CameraInvoker;
import fr.hugman.mubble.arcade.ArcadeAim;
import fr.hugman.mubble.arcade.ArcadeController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * The orbit camera of the arcade layer, in the third person view from the back.
 * <p>
 * The camera has its own yaw and pitch, moved by the mouse or the right stick, and the stick moves
 * the player relative to it: the body turns towards where it goes, the view does not follow, and the
 * head looks at the horizon. The camera trails the player a little, pulls in rather than going
 * through blocks, swings back behind the player on recenter, and blends with the vanilla view when
 * one takes over from the other. The hands do not follow the camera: they hit and use items where the
 * body faces, see {@link ArcadeAim#pickAhead}.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeCamera {
    /** The farthest the focus trails behind the player, in blocks: past it, the camera keeps up. */
    private static final double MAX_FOLLOW_GAP = 1.5D;
    /** A jump of the player farther than this, in blocks, is a teleport, and the camera snaps to it. */
    private static final double SNAP_DISTANCE = 8.0D;
    /** How long the camera takes to go from the vanilla view to the orbit and back, in seconds. */
    private static final float BLEND_SECONDS = 0.25F;
    /** How fast the head of the player goes back to looking at the horizon, in degrees per tick. */
    private static final float HEAD_LEVEL_SPEED = 8.0F;

    private static float yaw;
    private static float pitch;
    private static boolean orbiting;
    private static boolean recentering;
    /** Where the feet of the player trail, on the last two ticks: the camera interpolates between them. */
    @Nullable
    private static Vec3 focus;
    @Nullable
    private static Vec3 focusO;
    /** How far into the orbit the camera is, from the vanilla view (0) to the orbit (1). */
    private static float blend;
    private static long lastFrameNanos;

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

    /**
     * Called every client tick: the focus takes one step after the player. Stepping once a tick and
     * interpolating in between keeps the trailing smooth whatever the frame rate.
     */
    public static void tick(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || (!isOrbiting() && blend <= 0.0F)) {
            focus = null;
            focusO = null;
            return;
        }
        var target = player.position();
        focusO = focus == null ? target : focus;
        double lag = ArcadeClientConfig.get().followLag();
        double keep = lag > 0.0D ? Math.exp(-0.05D / lag) : 0.0D;
        focus = ArcadeAim.follow(focusO, target, keep, MAX_FOLLOW_GAP, SNAP_DISTANCE);
        // the trailing point must not slip behind a block, which would put the camera inside it: going
        // down steps, the edge left behind hides it for a tick, and it comes closer only that much
        focus = ArcadeAim.trail(player.level(), player, target, focus, player.getEyeHeight() + ArcadeClientConfig.get().cameraHeight());
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
        double scale = 0.15D * config.orbitSensitivity();
        yaw = Mth.wrapDegrees((float) (yaw + yawDelta * scale));
        pitch = (float) Mth.clamp(pitch + pitchDelta * scale, config.minPitch(), config.maxPitch());
        if (yawDelta != 0.0D || pitchDelta != 0.0D) {
            // turning by hand takes over from a recenter; a controller turns by nothing every frame its stick rests
            recentering = false;
        }
    }

    public static void recenter() {
        recentering = true;
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
        if (blend <= 0.0F) {
            return;
        }

        var config = ArcadeClientConfig.get();
        if (recentering && orbit) {
            float target = player.getYRot();
            yaw = Mth.approachDegrees(yaw, target, (float) (config.recenterSpeed() * seconds));
            if (Math.abs(Mth.degreesDifference(yaw, target)) < 0.5F) {
                recentering = false;
            }
        }

        var invoker = (CameraInvoker) camera;
        var feet = focus != null && focusO != null
                ? focusO.lerp(focus, partialTicks)
                : player.getPosition(partialTicks);
        // the eye height vanilla smooths, so that changing pose (a roll, a dive) does not jerk the view
        float eye = Mth.lerp(partialTicks, invoker.mubble$getEyeHeightOld(), invoker.mubble$getEyeHeight());
        var center = feet.add(0.0D, eye + config.cameraHeight(), 0.0D);
        var forward = Vec3.directionFromRotation(pitch, yaw);
        double distance = config.cameraDistance() * hintDistanceScale(player) * player.getScale();
        var orbitPosition = center.subtract(forward.scale(ArcadeAim.cameraDistance(player.level(), player, center, forward, distance)));

        if (blend >= 1.0F) {
            invoker.mubble$setRotation(yaw, pitch);
            invoker.mubble$setPosition(orbitPosition);
            return;
        }
        float t = blend * blend * (3.0F - 2.0F * blend);
        var vanillaPosition = camera.position();
        float vanillaYaw = camera.yRot();
        float vanillaPitch = camera.xRot();
        invoker.mubble$setRotation(vanillaYaw + Mth.degreesDifference(vanillaYaw, yaw) * t, Mth.lerp(t, vanillaPitch, pitch));
        invoker.mubble$setPosition(vanillaPosition.lerp(orbitPosition, t));
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
        return (float) (1.0D + ArcadeClientConfig.get().fovKick() * hint * share);
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
