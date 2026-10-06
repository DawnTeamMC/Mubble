package fr.hugman.mubble.arcade;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * The geometry of the orbit camera that does not need a client: how far back it can sit, and what the
 * hands of the player reach out to meanwhile. Kept here so that tests can hold it against real blocks.
 */
public final class ArcadeAim {
    /** Half the size of the box the camera keeps clear around itself, as vanilla's third person camera does. */
    private static final double CAMERA_RADIUS = 0.1D;
    /** How far apart the spots the camera tries are, coming closer to get out of a block, in blocks. */
    private static final double CLEAR_STEP = 0.25D;
    /** How much wider than an entity what the hands pick ahead is, see {@link #pickAhead}. */
    private static final double AHEAD_LENIENCY = 0.5D;

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
     * How far behind {@code focus}, looking along {@code forward}, the camera can sit, at most
     * {@code distance}: all of it as long as the spot is clear of blocks, whatever stands between it
     * and the focus; otherwise only as much closer as it takes to get out of them. A pillar or a
     * wall between the camera and the player does not move it; the camera backing into one does.
     *
     * @param margin how much room the camera keeps around itself on top of its own box, so that it
     *               starts moving in before it reaches the block rather than once it is in it
     */
    public static double clearDistance(Level level, Entity entity, Vec3 focus, Vec3 forward, double distance, double margin) {
        var back = forward.normalize().scale(-1.0D);
        double radius = CAMERA_RADIUS + margin;
        for (double d = distance; d > 0.0D; d -= CLEAR_STEP) {
            if (cameraFits(level, entity, focus.add(back.scale(d)), radius)) {
                if (d == distance) {
                    return d;
                }
                // closer in by steps, then back out to the edge of the room it found
                double inside = d + CLEAR_STEP;
                for (int i = 0; i < 6; i++) {
                    double mid = (d + inside) / 2.0D;
                    if (cameraFits(level, entity, focus.add(back.scale(mid)), radius)) {
                        d = mid;
                    } else {
                        inside = mid;
                    }
                }
                return d;
            }
        }
        return 0.0D;
    }

    /** Whether the box of the camera, {@code radius} each way around {@code position}, is clear of blocks. */
    public static boolean cameraFits(Level level, Entity entity, Vec3 position, double radius) {
        return level.noBlockCollision(entity, new AABB(position, position).inflate(radius));
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

    /**
     * Where the camera can look from, trailing {@code focus} behind {@code target}, both seen
     * {@code height} above the feet: the focus itself when nothing stands in between, otherwise the
     * point of the way to it right before the first block. Pulling the focus in only as far as the
     * blocks require, rather than all the way, keeps the camera from jumping each time a step down
     * or a corner hides the trailing point for a tick.
     */
    public static Vec3 trail(Level level, Entity entity, Vec3 target, Vec3 focus, double height) {
        var from = target.add(0.0D, height, 0.0D);
        var to = focus.add(0.0D, height, 0.0D);
        var hit = level.clip(new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity));
        if (hit.getType() == HitResult.Type.MISS) {
            return focus;
        }
        double length = from.distanceTo(to);
        double free = Math.max(0.0D, from.distanceTo(hit.getLocation()) - CAMERA_RADIUS);
        return length < 1.0E-6D ? target : target.add(focus.subtract(target).scale(Math.min(1.0D, free / length)));
    }

    /**
     * What the hands of a player reach out to while the orbit camera is on: the nearest entity in
     * front of them, along {@code yaw} and level with the horizon, within the vanilla entity reach and
     * with no block in the way. A target a bit off to the side, above or below still counts, so that
     * hitting what stands in front needs no pixel-perfect facing.
     * <p>
     * Blocks are never picked: with the orbit camera, the hands hit and use the item they hold, they
     * do not mine nor build.
     */
    public static HitResult pickAhead(Player player, Vec3 eye, float yaw) {
        var level = player.level();
        var forward = Vec3.directionFromRotation(0.0F, yaw);
        double reach = player.entityInteractionRange();
        var end = eye.add(forward.scale(reach));
        var band = player.getBoundingBox();
        Entity best = null;
        Vec3 bestAt = null;
        double bestDistance = Double.MAX_VALUE;
        for (var entity : level.getEntities(player, new AABB(eye, end).inflate(AHEAD_LENIENCY + 1.0D), EntitySelector.CAN_BE_PICKED)) {
            var box = entity.getBoundingBox().inflate(entity.getPickRadius() + AHEAD_LENIENCY);
            if (box.maxY < band.minY || box.minY > band.maxY) {
                continue;
            }
            // level with the eyes when the target spans them, at the nearest height of it otherwise
            double y = Mth.clamp(eye.y, box.minY, box.maxY);
            var from = new Vec3(eye.x, y, eye.z);
            var at = box.contains(from) ? Optional.of(from) : box.clip(from, new Vec3(end.x, y, end.z));
            if (at.isEmpty()) {
                continue;
            }
            double distance = from.distanceToSqr(at.get());
            if (distance >= bestDistance) {
                continue;
            }
            var lineOfSight = level.clip(new ClipContext(eye, entity.getBoundingBox().getCenter(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if (lineOfSight.getType() != HitResult.Type.MISS && lineOfSight.getLocation().distanceToSqr(eye) < distance - 1.0E-6D) {
                continue;
            }
            best = entity;
            bestAt = at.get();
            bestDistance = distance;
        }
        return best != null ? new EntityHitResult(best, bestAt) : miss(end, forward);
    }

    private static BlockHitResult miss(Vec3 location, Vec3 forward) {
        return BlockHitResult.miss(location, Direction.getApproximateNearest(forward), BlockPos.containing(location));
    }
}
