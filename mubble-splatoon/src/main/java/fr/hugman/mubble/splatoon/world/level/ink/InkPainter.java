package fr.hugman.mubble.splatoon.world.level.ink;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

/**
 * Splashes ink where something made of ink lands.
 *
 * <p>A splat is an ellipse lying on the surface that was hit, stretched in the direction the ink was going: it reaches
 * {@link Splat#depthHalf()} ahead and behind its middle, which sits a little forward of the impact, and
 * {@link Splat#widthHalf()} to the sides. On walls, where the ink came in head-on, it runs down instead. Its outline
 * is wobbly and a few droplets fly off it, all seeded, so that no two splats look the same.
 *
 * <p>The splat also has some thickness, so that it reaches the surfaces around the one that was hit: the floor at the
 * foot of a wall, the walls of a corner, the top of a wall hit near its edge, or the side of a step. It never reaches
 * surfaces turned away from it, the faces of blocks buried under the surface it landed on, nor surfaces out of its
 * sight, so it does not paint through walls.
 *
 * <p>Plants and other {@linkplain InkLevel#isCoatable coatable} blocks within the splat get coated, and so do the
 * surfaces under them. Nothing gets painted where the ink landed in a liquid that washes ink away.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkPainter {
    /**
     * How thick a splat is, relative to its width.
     */
    public static final double THICKNESS = 0.3;
    /**
     * How far, relative to the difference between its length and its width, the middle of a splat sits ahead of the
     * impact.
     */
    public static final double FORWARD_SHIFT = 0.5;

    private static final int WOBBLES = 6;
    private static final double MAX_WOBBLE = 0.12;
    /**
     * How far, relative to its size, anything of a splat reaches: its wobbly rim and the droplets around it.
     */
    private static final double MAX_REACH = 1.8;
    private static final int MIN_DROPLETS = 5;
    private static final int MAX_DROPLETS = 11;
    private static final double SIGHT_OFFSET = 0.05;
    private static final Direction[] DIRECTIONS = Direction.values();

    private InkPainter() {
    }

    /**
     * @param center    where the ink landed
     * @param normal    the direction the surface that was hit faces, as a unit vector
     * @param direction the direction the ink was going
     * @param widthHalf the half width of the splat, across its direction, in blocks
     * @param depthHalf the half length of the splat, along its direction, in blocks
     * @param seed      what makes this splat look unlike any other
     */
    public record Splat(Vec3 center, Vec3 normal, Vec3 direction, double widthHalf, double depthHalf, long seed) {
    }

    /**
     * @return whether anything changed
     */
    public static boolean splat(ServerLevel level, Splat splat, InkStyle style) {
        if (splat.widthHalf() <= 0 || splat.depthHalf() <= 0) {
            return false;
        }
        var landing = BlockPos.containing(splat.center().add(splat.normal().scale(SIGHT_OFFSET)));
        if (level.isLoaded(landing) && !level.getFluidState(landing).isEmpty() && InkLevel.liquidsWashInk(level, landing)) {
            return false;
        }
        var shape = new Shape(splat);
        var surfaceSide = Direction.getApproximateNearest(splat.normal());
        var eye = splat.center().add(splat.normal().scale(SIGHT_OFFSET));
        boolean changed = false;

        var reach = shape.reach();
        var min = BlockPos.containing(shape.middle.subtract(reach));
        var max = BlockPos.containing(shape.middle.add(reach));
        for (var pos : BlockPos.betweenClosed(min, max)) {
            if (!level.isLoaded(pos)) {
                continue;
            }
            var state = level.getBlockState(pos);

            if (InkLevel.isCoatable(state)) {
                var outline = state.getShape(level, pos);
                var bounds = outline.isEmpty() ? new AABB(pos) : outline.bounds().move(pos);
                var middle = bounds.getCenter();
                // how far the plant is from the surface that was hit, at its nearest
                var normal = splat.normal();
                double height = middle.subtract(splat.center()).dot(normal);
                double extent = (Math.abs(normal.x) * bounds.getXsize() + Math.abs(normal.y) * bounds.getYsize() + Math.abs(normal.z) * bounds.getZsize()) / 2.0;
                double nearest = Math.signum(height) * Math.max(0.0, Math.abs(height) - extent);
                if (shape.contains(middle, nearest) && inSight(level, eye, middle)) {
                    changed |= InkLevel.coat(level, pos.immutable(), style);
                }
                continue;
            }
            if (!InkLevel.isInkable(state)) {
                continue;
            }

            var surfaces = InkSurfaces.of(state);
            for (var side : DIRECTIONS) {
                if (surfaces.surfaces(side) == 0L) {
                    continue;
                }
                var sideNormal = Vec3.atLowerCornerOf(side.getUnitVec3i());
                // surfaces turned away from the splat never get any
                if (sideNormal.dot(splat.normal()) < -1.0E-3) {
                    continue;
                }
                long candidates = InkLevel.inkableCells(level, pos, side);
                if (candidates == 0L) {
                    continue;
                }

                long cells = 0L;
                boolean behind = false;
                double sumX = 0, sumY = 0, sumZ = 0;
                for (int cell = 0; cell < InkGrid.CELLS; cell++) {
                    if (!InkGrid.has(candidates, cell)) {
                        continue;
                    }
                    var point = cellPoint(pos, side, cell, surfaces.depth(side, cell));
                    double height = point.subtract(splat.center()).dot(splat.normal());
                    if (shape.contains(point, height)) {
                        cells |= 1L << cell;
                        behind |= height < -1.0E-3;
                        sumX += point.x;
                        sumY += point.y;
                        sumZ += point.z;
                    }
                }
                if (cells == 0L) {
                    continue;
                }

                if (behind) {
                    // below the surface that was hit, only the blocks making that surface get ink, which is how it
                    // wraps around edges without reaching into what lies under it
                    if (InkLevel.inkableCells(level, pos, surfaceSide) == 0L) {
                        continue;
                    }
                } else {
                    int count = Long.bitCount(cells);
                    var target = new Vec3(sumX / count, sumY / count, sumZ / count).add(sideNormal.scale(SIGHT_OFFSET));
                    if (!inSight(level, eye, target)) {
                        continue;
                    }
                }
                changed |= InkLevel.paint(level, pos.immutable(), side, cells, style);
            }
        }
        return changed;
    }

    private static Vec3 cellPoint(BlockPos pos, Direction side, int cell, float depth) {
        var axis = side.getAxis();
        double w = side.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 - depth : depth;
        double u = InkGrid.center(InkGrid.u(cell));
        double v = InkGrid.center(InkGrid.v(cell));
        return new Vec3(
                pos.getX() + (axis == Direction.Axis.X ? w : u),
                pos.getY() + (axis == Direction.Axis.Y ? w : v),
                pos.getZ() + (axis == Direction.Axis.Z ? w : axis == Direction.Axis.X ? u : v)
        );
    }

    private static boolean inSight(ServerLevel level, Vec3 eye, Vec3 target) {
        if (eye.distanceToSqr(target) < 1.0E-4) {
            return true;
        }
        var hit = level.clip(new ClipContext(eye, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
        return hit.getType() == HitResult.Type.MISS || hit.getLocation().distanceToSqr(target) < SIGHT_OFFSET * SIGHT_OFFSET * 4;
    }

    /**
     * The outline of a splat: a wobbly ellipse in the plane of the surface that was hit, a few droplets around it, and
     * some thickness.
     */
    private static final class Shape {
        private final Vec3 middle;
        private final Vec3 normal;
        private final Vec3 forward;
        private final Vec3 side;
        private final double widthHalf;
        private final double depthHalf;
        private final double thickness;
        private final double[] wobbleAmplitudes = new double[WOBBLES];
        private final double[] wobblePhases = new double[WOBBLES];
        private final double[][] droplets;

        private Shape(Splat splat) {
            var normal = splat.normal();
            this.normal = normal;
            var random = RandomSource.create(splat.seed());

            // the direction of the ink along the surface; ink coming in head-on runs down walls, or anywhere on floors
            var along = splat.direction().subtract(normal.scale(splat.direction().dot(normal)));
            if (along.lengthSqr() < 0.01) {
                var down = new Vec3(0, -1, 0);
                along = down.subtract(normal.scale(down.dot(normal)));
                if (along.lengthSqr() < 0.01) {
                    float angle = random.nextFloat() * Mth.TWO_PI;
                    along = new Vec3(Mth.cos(angle), 0, Mth.sin(angle));
                }
            }
            this.forward = along.normalize();
            this.side = normal.cross(this.forward).normalize();
            this.widthHalf = splat.widthHalf();
            this.depthHalf = Math.max(splat.depthHalf(), splat.widthHalf());
            this.thickness = this.widthHalf * THICKNESS;
            this.middle = splat.center().add(this.forward.scale((this.depthHalf - this.widthHalf) * FORWARD_SHIFT));

            for (int i = 0; i < WOBBLES; i++) {
                this.wobbleAmplitudes[i] = MAX_WOBBLE * (0.3 + 0.7 * random.nextDouble()) / (1 + i * 0.35);
                this.wobblePhases[i] = random.nextDouble() * Mth.TWO_PI;
            }
            // droplets fly off the rim, mostly forward, the farther the smaller
            int count = MIN_DROPLETS + random.nextInt(MAX_DROPLETS - MIN_DROPLETS + 1);
            this.droplets = new double[count][];
            for (int i = 0; i < count; i++) {
                double angle = random.nextGaussian() * 0.9;
                double distance = 0.95 + random.nextDouble() * 0.6;
                double radius = Math.max(0.05, 0.24 - (distance - 0.95) * 0.3) * (0.6 + 0.4 * random.nextDouble());
                this.droplets[i] = new double[]{Math.cos(angle) * distance, Math.sin(angle) * distance, radius};
            }
        }

        /**
         * @return how far from its middle the splat reaches along each axis, as the box around the flat, thick disk it
         * is
         */
        private Vec3 reach() {
            double forward = this.depthHalf * MAX_REACH;
            double side = this.widthHalf * MAX_REACH;
            return new Vec3(
                    forward * Math.abs(this.forward.x) + side * Math.abs(this.side.x) + this.thickness * Math.abs(this.normal.x),
                    forward * Math.abs(this.forward.y) + side * Math.abs(this.side.y) + this.thickness * Math.abs(this.normal.y),
                    forward * Math.abs(this.forward.z) + side * Math.abs(this.side.z) + this.thickness * Math.abs(this.normal.z)
            );
        }

        /**
         * @param height how far in front of the surface that was hit the point is
         */
        private boolean contains(Vec3 point, double height) {
            if (Math.abs(height) > this.thickness) {
                return false;
            }
            var offset = point.subtract(this.middle);
            // in the plane of the splat, with the ellipse turned into a unit circle
            double a = offset.dot(this.forward) / this.depthHalf;
            double b = offset.dot(this.side) / this.widthHalf;
            double radius = Math.sqrt(a * a + b * b);
            if (radius <= this.rim(Math.atan2(b, a))) {
                return true;
            }
            for (var droplet : this.droplets) {
                double da = a - droplet[0];
                double db = b - droplet[1];
                if (da * da + db * db <= droplet[2] * droplet[2]) {
                    return true;
                }
            }
            return false;
        }

        private double rim(double angle) {
            double rim = 1.0;
            for (int i = 0; i < WOBBLES; i++) {
                rim += this.wobbleAmplitudes[i] * Math.sin((i + 2) * angle + this.wobblePhases[i]);
            }
            return rim;
        }
    }
}
