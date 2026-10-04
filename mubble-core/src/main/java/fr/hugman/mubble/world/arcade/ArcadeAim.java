package fr.hugman.mubble.world.arcade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * The geometry of the orbit camera that does not need a client: how far back it can sit, and what its
 * crosshair is allowed to pick. Kept here so that tests can hold it against real blocks.
 */
public final class ArcadeAim {
    /** Half the size of the box the camera keeps clear around itself, as vanilla's third person camera does. */
    private static final double CAMERA_RADIUS = 0.1D;

    private ArcadeAim() {
    }

    /**
     * How far behind {@code focus}, looking along {@code forward}, the camera can sit without any of
     * the corners of its box going through a block: at most {@code distance}.
     * <p>
     * Vanilla's third person camera casts the same eight rays, but measures from the focus to where a
     * corner hit, which lets the camera itself end up a hair inside the block; this measures how far
     * each corner travelled instead, so the whole box stays out.
     */
    public static double cameraDistance(Level level, Entity entity, Vec3 focus, Vec3 forward, double distance) {
        var back = forward.normalize().scale(-1.0D);
        for (int i = 0; i < 8; i++) {
            double ox = ((i & 1) * 2 - 1) * CAMERA_RADIUS;
            double oy = ((i >> 1 & 1) * 2 - 1) * CAMERA_RADIUS;
            double oz = ((i >> 2 & 1) * 2 - 1) * CAMERA_RADIUS;
            var from = focus.add(ox, oy, oz);
            var to = from.add(back.scale(distance));
            var hit = level.clip(new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity));
            if (hit.getType() != HitResult.Type.MISS) {
                distance = Math.max(0.0D, Math.min(distance, hit.getLocation().subtract(from).dot(back)));
            }
        }
        return distance;
    }

    /**
     * One tick of a point trailing {@code target}, as the orbit camera's focus trails the player: the
     * gap left behind shrinks to {@code keep} of itself every tick, and never grows past
     * {@code maxGap}, so that at any speed it settles on a steady distance rather than snapping back
     * now and then. Only a jump of more than {@code snapDistance}, a teleport, resets it.
     */
    public static Vec3 follow(Vec3 focus, Vec3 target, double keep, double maxGap, double snapDistance) {
        var gap = focus.subtract(target);
        double length = gap.length();
        if (length > snapDistance) {
            return target;
        }
        gap = gap.scale(keep);
        length *= keep;
        if (length > maxGap) {
            gap = gap.scale(maxGap / length);
        }
        return target.add(gap);
    }

    /** Whether a block stands between where the camera means to look from and where it lags behind. */
    public static boolean blocked(Level level, Entity entity, Vec3 target, Vec3 focus) {
        return level.clip(new ClipContext(target, focus, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity)).getType() != HitResult.Type.MISS;
    }

    /**
     * What the crosshair of a camera at {@code origin} looking along {@code forward} points at, if the
     * player could reach it from {@code eye}: within the vanilla reach, and with nothing in the way.
     * The ray starts level with the player, so that what stands between the camera and the player is
     * never picked.
     */
    public static HitResult pick(Player player, Vec3 origin, Vec3 forward, Vec3 eye) {
        var level = player.level();
        double blockRange = player.blockInteractionRange();
        double entityRange = player.entityInteractionRange();
        double reach = Math.max(blockRange, entityRange);
        double startAlong = Math.max(0.0D, eye.subtract(origin).dot(forward) - 0.5D);
        var start = origin.add(forward.scale(startAlong));
        var end = start.add(forward.scale(reach + 1.0D));

        HitResult hit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        double hitDistance = hit.getLocation().distanceToSqr(start);
        var box = player.getBoundingBox().expandTowards(forward.scale(reach + 1.0D + startAlong)).inflate(1.0D).minmax(new AABB(start, end));
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, start, end, box, EntitySelector.CAN_BE_PICKED, hitDistance);
        if (entityHit != null && entityHit.getEntity() != player) {
            hit = entityHit;
        }

        // checked from the eyes: the same reach as vanilla, and no seeing through walls
        double range = hit instanceof EntityHitResult ? entityRange : blockRange;
        var location = hit.getLocation();
        if (hit.getType() == HitResult.Type.MISS || location.distanceToSqr(eye) > range * range) {
            return miss(location, forward);
        }
        var lineOfSight = level.clip(new ClipContext(eye, location, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if (lineOfSight.getType() == HitResult.Type.BLOCK) {
            boolean sameBlock = hit instanceof BlockHitResult blockHit && lineOfSight.getBlockPos().equals(blockHit.getBlockPos());
            if (!sameBlock) {
                return miss(location, forward);
            }
        }
        return hit;
    }

    private static BlockHitResult miss(Vec3 location, Vec3 forward) {
        return BlockHitResult.miss(location, Direction.getApproximateNearest(forward), BlockPos.containing(location));
    }
}
