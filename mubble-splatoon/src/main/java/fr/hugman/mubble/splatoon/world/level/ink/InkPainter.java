package fr.hugman.mubble.splatoon.world.level.ink;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Splashes ink around a point, the way a shot does when it lands.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkPainter {
    /**
     * How far behind its own plane a surface may be and still get ink, so that a splat landing exactly on a face
     * still paints it.
     */
    private static final double BEHIND_TOLERANCE = 0.05;
    private static final Direction[] DIRECTIONS = Direction.values();

    private InkPainter() {
    }

    /**
     * Paints every surface facing the given point within the given radius: the face that was hit, but also the floor
     * and walls around it, which is what lets a splat wrap around an edge or fill a corner.
     *
     * @return whether any cell changed
     */
    public static boolean splat(ServerLevel level, Vec3 center, double radius, InkStyle style) {
        if (radius <= 0) {
            return false;
        }
        double radiusSqr = radius * radius;
        boolean changed = false;

        var min = BlockPos.containing(center.x - radius, center.y - radius, center.z - radius);
        var max = BlockPos.containing(center.x + radius, center.y + radius, center.z + radius);
        for (var pos : BlockPos.betweenClosed(min, max)) {
            if (!level.isLoaded(pos)) {
                continue;
            }
            var state = level.getBlockState(pos);
            if (!InkLevel.isInkable(state)) {
                continue;
            }
            var surfaces = InkSurfaces.of(state);

            for (var side : DIRECTIONS) {
                long candidates = surfaces.surfaces(side);
                if (candidates == 0L) {
                    continue;
                }
                long cells = 0L;
                var axis = side.getAxis();
                int sign = side.getAxisDirection().getStep();

                for (int cell = 0; cell < InkGrid.CELLS; cell++) {
                    if (!InkGrid.has(candidates, cell)) {
                        continue;
                    }
                    float depth = surfaces.depth(side, cell);
                    double w = sign > 0 ? 1.0 - depth : depth;
                    double u = InkGrid.center(InkGrid.u(cell));
                    double v = InkGrid.center(InkGrid.v(cell));
                    double x = pos.getX() + (axis == Direction.Axis.X ? w : u);
                    double y = pos.getY() + (axis == Direction.Axis.Y ? w : v);
                    double z = pos.getZ() + (axis == Direction.Axis.Z ? w : axis == Direction.Axis.X ? u : v);

                    // only surfaces turned towards the splat get ink, not the back of the wall it landed on
                    double facing = (InkGrid.w(axis, center.x, center.y, center.z) - InkGrid.w(axis, x, y, z)) * sign;
                    if (facing < -BEHIND_TOLERANCE) {
                        continue;
                    }
                    if (Mth.lengthSquared(center.x - x, center.y - y, center.z - z) <= radiusSqr) {
                        cells |= 1L << cell;
                    }
                }

                if (cells != 0L) {
                    changed |= InkLevel.paint(level, pos.immutable(), side, cells, style);
                }
            }
        }
        return changed;
    }
}
