package fr.hugman.mubble.client.arcade.camera;

import fr.hugman.mubble.client.arcade.ArcadeClientConfig;
import fr.hugman.mubble.client.mixin.CameraInvoker;
import fr.hugman.mubble.world.arcade.ArcadeController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * The orbit camera of the arcade layer, in the third person view from the back.
 * <p>
 * The camera has its own yaw and pitch, moved by the mouse or the right stick, and the stick moves
 * the player relative to it: the body turns towards where it goes, the view does not follow. The
 * camera trails the player a little, pulls in rather than going through blocks (the vanilla check),
 * and swings back behind the player on recenter. Aiming, mining, placing and attacking go where its
 * crosshair is, as long as the player could reach it from their own eyes.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeCamera {
    private static float yaw;
    private static float pitch;
    private static boolean orbiting;
    private static boolean recentering;
    @Nullable
    private static Vec3 focus;
    private static long lastFrameMillis;

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
            focus = null;
        }
        orbiting = now;
        return now;
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
        recentering = false;
    }

    public static void recenter() {
        recentering = true;
    }

    /**
     * Puts the camera in place for the frame, once vanilla aligned it with the player: on its own
     * rotation, around a focus point trailing the player, as far back as the blocks allow.
     */
    public static void apply(Camera camera, LocalPlayer player, float partialTicks) {
        var config = ArcadeClientConfig.get();
        long now = Util.getMillis();
        float seconds = lastFrameMillis == 0L ? 0.0F : Math.min(0.25F, (now - lastFrameMillis) / 1000.0F);
        lastFrameMillis = now;

        if (recentering) {
            float target = player.getYRot();
            yaw = Mth.approachDegrees(yaw, target, (float) (config.recenterSpeed() * seconds));
            if (Math.abs(Mth.degreesDifference(yaw, target)) < 0.5F) {
                recentering = false;
            }
        }

        var target = new Vec3(
                Mth.lerp(partialTicks, player.xo, player.getX()),
                Mth.lerp(partialTicks, player.yo, player.getY()) + player.getEyeHeight() + config.cameraHeight(),
                Mth.lerp(partialTicks, player.zo, player.getZ()));
        if (focus == null || focus.distanceToSqr(target) > 4.0D || blocked(player, target, focus)) {
            focus = target;
        } else if (config.followLag() > 0.0D) {
            double t = 1.0D - Math.exp(-seconds / config.followLag());
            focus = focus.add(target.subtract(focus).scale(t));
        } else {
            focus = target;
        }

        var invoker = (CameraInvoker) camera;
        invoker.mubble$setRotation(yaw, pitch);
        invoker.mubble$setPosition(focus);
        double hint = hintDistanceScale(player);
        float distance = (float) (config.cameraDistance() * hint * player.getScale());
        invoker.mubble$move(-invoker.mubble$getMaxZoom(distance), 0.0F, 0.0F);
    }

    /** Whether the focus trailing behind has slipped behind a block, which would put the camera inside it. */
    private static boolean blocked(LocalPlayer player, Vec3 target, Vec3 focus) {
        return player.level().clip(new ClipContext(target, focus, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player)).getType() != HitResult.Type.MISS;
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

    /**
     * What the crosshair of the orbit camera points at, if the player could reach it from their own
     * eyes: within the vanilla reach, and with nothing in the way. The ray starts level with the
     * player, so that what stands between the camera and the player is never picked.
     */
    public static HitResult pick(Minecraft minecraft, LocalPlayer player, float partialTicks) {
        var camera = minecraft.gameRenderer.mainCamera();
        var origin = camera.position();
        var forward = Vec3.directionFromRotation(camera.xRot(), camera.yRot());
        var eye = player.getEyePosition(partialTicks);
        double blockRange = player.blockInteractionRange();
        double entityRange = player.entityInteractionRange();
        double reach = Math.max(blockRange, entityRange);
        double startAlong = Math.max(0.0D, eye.subtract(origin).dot(forward) - 0.5D);
        var start = origin.add(forward.scale(startAlong));
        var end = start.add(forward.scale(reach + 1.0D));

        HitResult hit = player.level().clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        double hitDistance = hit.getLocation().distanceToSqr(start);
        var box = player.getBoundingBox().expandTowards(forward.scale(reach + 1.0D + startAlong)).inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, start, end, box.minmax(new net.minecraft.world.phys.AABB(start, end)), EntitySelector.CAN_BE_PICKED, hitDistance);
        if (entityHit != null && entityHit.getEntity() != player) {
            hit = entityHit;
        }

        // checked from the eyes: the same reach as vanilla, and no seeing through walls
        double range = hit instanceof EntityHitResult ? entityRange : blockRange;
        var location = hit.getLocation();
        if (hit.getType() == HitResult.Type.MISS || location.distanceToSqr(eye) > range * range) {
            return BlockHitResult.miss(location, net.minecraft.core.Direction.getApproximateNearest(forward), net.minecraft.core.BlockPos.containing(location));
        }
        var lineOfSight = player.level().clip(new ClipContext(eye, location, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if (lineOfSight.getType() == HitResult.Type.BLOCK) {
            boolean sameBlock = hit instanceof BlockHitResult blockHit && lineOfSight.getBlockPos().equals(blockHit.getBlockPos());
            if (!sameBlock) {
                return BlockHitResult.miss(location, net.minecraft.core.Direction.getApproximateNearest(forward), net.minecraft.core.BlockPos.containing(location));
            }
        }
        return hit;
    }

    /**
     * Turns the player towards what the crosshair shows while they attack or use something, so that a
     * bow shoots, and a block faces, where the camera looks rather than where the body runs.
     */
    public static void aimIfNeeded(Minecraft minecraft, LocalPlayer player) {
        if (!isOrbiting()) {
            return;
        }
        var options = minecraft.options;
        if (!options.keyUse.isDown() && !options.keyAttack.isDown() && !player.isUsingItem()) {
            return;
        }
        var camera = minecraft.gameRenderer.mainCamera();
        var forward = Vec3.directionFromRotation(camera.xRot(), camera.yRot());
        var hit = minecraft.hitResult;
        var aim = hit != null && hit.getType() != HitResult.Type.MISS ? hit.getLocation() : camera.position().add(forward.scale(64.0D));
        var direction = aim.subtract(player.getEyePosition());
        double horizontal = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
        player.setYRot((float) (Mth.atan2(direction.z, direction.x) * Mth.RAD_TO_DEG) - 90.0F);
        player.setXRot((float) -(Mth.atan2(direction.y, horizontal) * Mth.RAD_TO_DEG));
    }
}
